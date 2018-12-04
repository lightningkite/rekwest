package com.lightningkite.rekwest.server

import com.lightningkite.kommon.exception.stackTraceString
import com.lightningkite.mirror.archive.Transaction
import com.lightningkite.mirror.archive.use
import com.lightningkite.mirror.info.*
import com.lightningkite.rekwest.RemoteExceptionData
import com.lightningkite.rekwest.ServerFunction
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.util.pipeline.ContextDsl
import java.util.HashMap
import kotlin.reflect.KClass

class ServerFunctionHandler(
        val classInfoRegistry: ClassInfoRegistry
) {

    @ContextDsl
    fun Route.serverFunction(path: String, suppressStackTrace: Boolean = true): Route {
        return post(path) { _ ->
            try {
                val sf = call.receive(ServerFunction::class.type)
                try {
                    val result = listOf(sf).transaction(call.unwrappedPrincipal()).use {
                        sf.invoke(it)
                    }
                    @Suppress("UNCHECKED_CAST")
                    call.respond(sf::class.returnType as Type<Any?>, result)
                } catch (e: Throwable) {
                    e.printStackTrace()
                    val isExpected = sf::class.throwsTypes.contains(e.javaClass.simpleName)
                    call.respond(
                            status = if (isExpected) HttpStatusCode.BadRequest else HttpStatusCode.InternalServerError,
                            type = RemoteExceptionData::class.type,
                            value = RemoteExceptionData(
                                    type = e.javaClass.simpleName,
                                    message = e.message ?: "",
                                    trace = if (suppressStackTrace) "" else e.stackTraceString(),
                                    data = null
                            )
                    )
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

    }

    @ContextDsl
    fun Route.serverFunctions(path: String, suppressStackTrace: Boolean = true): Route {
        return post(path) { _ ->
            try {
                val sfs = call.receive(ServerFunction::class.type.list)
                try {
                    val result = sfs.transaction(call.unwrappedPrincipal()).use { txn ->
                        sfs.map { it.invoke(txn) }
                    }
                    call.respond(Any::class.typeNullable.list, result)
                } catch (e: Throwable) {
                    e.printStackTrace()
                    val isExpected = sfs.any { sf -> sf::class.throwsTypes.contains(e.javaClass.simpleName) }
                    call.respond(
                            status = if (isExpected) HttpStatusCode.BadRequest else HttpStatusCode.InternalServerError,
                            type = RemoteExceptionData::class.type,
                            value = RemoteExceptionData(
                                    type = e.javaClass.simpleName,
                                    message = e.message ?: "",
                                    trace = if (suppressStackTrace) "" else e.stackTraceString(),
                                    data = null
                            )
                    )
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

    }

    private val KClassServerFunction_Returns = HashMap<KClass<*>, Type<*>>()
    val KClass<out ServerFunction<*>>.returnType: Type<*>
        get() {
            return KClassServerFunction_Returns.getOrPut(this) {
                classInfoRegistry.getOrThrow(this)
                        .allImplements(classInfoRegistry)
                        .find { it.kClass == ServerFunction::class }!!
                        .typeParameters.first().type
            }
        }

    private val KClassServerFunction_Throws = HashMap<KClass<*>, List<String>>()
    @Suppress("UNCHECKED_CAST")
    val KClass<out ServerFunction<*>>.throwsTypes: List<String>
        get() {
            return KClassServerFunction_Throws.getOrPut(this) {
                classInfoRegistry[this]!!
                        .implementsTree(classInfoRegistry)
                        .pathTo(ServerFunction::class)
                        ?.asSequence()
                        ?.flatMap {
                            it.info.annotations
                                    .find { it.name.endsWith("ThrowsTypes") }
                                    ?.arguments
                                    ?.asSequence()
                                    ?.mapNotNull { it as? String } ?: sequenceOf()
                        }
                        ?.toList() ?: listOf()
            }
        }

    private val KClassServerFunction_RequiresWrite = HashMap<KClass<*>, Boolean>()
    val KClass<out ServerFunction<*>>.requiresWrite: Boolean
        get() {
            return KClassServerFunction_RequiresWrite.getOrPut(this) {
                classInfoRegistry[this]!!
                        .implementsTree(classInfoRegistry)
                        .pathTo(ServerFunction::class)
                        ?.asSequence()
                        ?.any {
                            it.info.annotations.any { it.name.endsWith("Mutates") } ?: false
                        } ?: false
            }
        }

    private val KClassServerFunction_RequiresAtomicTransaction = HashMap<KClass<*>, Boolean>()
    var KClass<out ServerFunction<*>>.requiresAtomicTransaction: Boolean
        set(value) {
            KClassServerFunction_RequiresAtomicTransaction[this] = value
        }
        get() {
            return KClassServerFunction_RequiresAtomicTransaction.getOrPut(this) {
                classInfoRegistry[this]!!
                        .implementsTree(classInfoRegistry)
                        .pathTo(ServerFunction::class)
                        ?.asSequence()
                        ?.any {
                            it.info.annotations.any { it.name.endsWith("RequiresAtomicTransaction") } ?: false
                        } ?: false
            }
        }

    private val KClassServerFunction_Invocation = HashMap<KClass<*>, suspend (Any, Transaction) -> Any?>()
    @Suppress("UNCHECKED_CAST")
    var <SF : ServerFunction<R>, R> KClass<SF>.invocation: suspend SF.(Transaction) -> R
        set(value) {
            KClassServerFunction_Invocation[this] = value as suspend (Any, Transaction) -> Any?
        }
        get() {
            return KClassServerFunction_Invocation.getOrPut(this) {
                classInfoRegistry[this]!!
                        .implementsTree(classInfoRegistry)
                        .pathTo(ServerFunction::class)
                        ?.asSequence()
                        ?.mapNotNull { KClassServerFunction_Invocation[it.info.kClass] }
                        ?.firstOrNull()
                        ?: throw IllegalArgumentException("No invocation could be found for the server function type $this")
            } as suspend SF.(Transaction) -> R
        }

    @Suppress("UNCHECKED_CAST")
    suspend operator fun <R> ServerFunction<R>.invoke(transaction: Transaction): R = KClassServerFunction_Invocation[this::class]!!.invoke(this, transaction) as R

    fun List<ServerFunction<*>>.transaction(untypedUser: Any?): Transaction = Transaction(
            untypedUser = untypedUser,
            atomic = any { it::class.requiresAtomicTransaction },
            readOnly = none { it::class.requiresWrite }
    )

    @Suppress("UNCHECKED_CAST")
    suspend fun <R> ServerFunction<R>.invokeWithUser(untypedUser: Any?): R = invoke(listOf(this).transaction(untypedUser))
}