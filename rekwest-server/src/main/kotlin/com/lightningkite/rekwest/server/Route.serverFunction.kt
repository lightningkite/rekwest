package com.lightningkite.rekwest.server

import com.lightningkite.kommon.exception.stackTraceString
import com.lightningkite.mirror.archive.use
import com.lightningkite.mirror.info.Type
import com.lightningkite.mirror.info.list
import com.lightningkite.mirror.info.type
import com.lightningkite.mirror.info.typeNullable
import com.lightningkite.rekwest.*
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.util.pipeline.ContextDsl


@ContextDsl
fun Route.serverFunction(path: String, supressStackTrace: Boolean = true): Route {
    return post(path) { _ ->
        try {
            val sf = call.receive<ServerFunction<*>>(ServerFunction::class.type)
            try {
                val result = listOf(sf).transaction(call.unwrappedPrincipal()).use {
                    sf.invoke(it)
                }
                @Suppress("UNCHECKED_CAST")
                call.respond(sf.returnType as Type<Any?>, result)
            } catch (e: Throwable) {
                e.printStackTrace()
                val isExpected = sf.throwsTypes?.contains(e.javaClass.simpleName) ?: false
                call.respond(
                        status = if (isExpected) HttpStatusCode.BadRequest else HttpStatusCode.InternalServerError,
                        type = RemoteExceptionData::class.type,
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
            val sfs = call.receive(ServerFunction::class.type.list)
            try {
                val result = sfs.transaction(call.unwrappedPrincipal()).use { txn ->
                    sfs.map { it.invoke(txn) }
                }
                call.respond(Any::class.typeNullable.list, result)
            } catch (e: Throwable) {
                e.printStackTrace()
                val isExpected = sfs.any { sf -> sf.throwsTypes?.contains(e.javaClass.simpleName) ?: false }
                call.respond(
                        status = if (isExpected) HttpStatusCode.BadRequest else HttpStatusCode.InternalServerError,
                        type = RemoteExceptionData::class.type,
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
