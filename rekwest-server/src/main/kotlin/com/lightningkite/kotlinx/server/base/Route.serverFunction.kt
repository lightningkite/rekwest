package com.lightningkite.kotlinx.server.base

import com.lightningkite.kotlinx.exception.stackTraceString
import com.lightningkite.kotlinx.persistence.use
import com.lightningkite.kotlinx.reflection.*
import com.lightningkite.kotlinx.server.RemoteExceptionData
import com.lightningkite.kotlinx.server.ServerFunction
import com.lightningkite.kotlinx.server.returnType
import com.lightningkite.kotlinx.server.throws
import io.ktor.application.call
import io.ktor.application.feature
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.pipeline.ContextDsl
import io.ktor.routing.Route
import io.ktor.routing.application
import io.ktor.routing.post


@ContextDsl
fun Route.serverFunction(path: String, supressStackTrace: Boolean = true): Route {
    return post(path) { _ ->
        try {
            val sf = call.receive<ServerFunction<*>>(ServerFunction::class.kxType)
            try {
                val result = listOf(sf).transaction(call.unwrappedPrincipal()).use {
                    sf.invoke(it)!!
                }
                call.respond(sf.returnType, result)
            } catch (e: Throwable) {
                e.printStackTrace()
                val isExpected = sf.throws?.contains(e.javaClass.simpleName) ?: false
                call.respond(
                        status = if (isExpected) HttpStatusCode.BadRequest else HttpStatusCode.InternalServerError,
                        type = RemoteExceptionData::class.kxType,
                        value = RemoteExceptionData(
                                type = e.javaClass.simpleName,
                                message = e.message ?: "",
                                trace = if (supressStackTrace) "" else e.stackTraceString(),
                                data = null
                        )
                )
            }
        } catch (e: Throwable) {

        }
    }

}

@ContextDsl
fun Route.serverFunctions(path: String, supressStackTrace: Boolean = true): Route {
    return post(path) { _ ->
        try {
            val sfs = call.receive<List<ServerFunction<*>>>(ServerFunction::class.kxType.list())
            try {
                val result = sfs.transaction(call.unwrappedPrincipal()).use { txn ->
                    sfs.map { it.invoke(txn) }
                }
                call.respond(AnyReflection.kxTypeNullable.list(), result)
            } catch (e: Throwable) {
                e.printStackTrace()
                val isExpected = sfs.any { sf -> sf.throws?.contains(e.javaClass.simpleName) ?: false }
                call.respond(
                        status = if (isExpected) HttpStatusCode.BadRequest else HttpStatusCode.InternalServerError,
                        type = RemoteExceptionData::class.kxType,
                        value = RemoteExceptionData(
                                type = e.javaClass.simpleName,
                                message = e.message ?: "",
                                trace = if (supressStackTrace) "" else e.stackTraceString(),
                                data = null
                        )
                )
            }
        } catch (e: Throwable) {

        }
    }

}
