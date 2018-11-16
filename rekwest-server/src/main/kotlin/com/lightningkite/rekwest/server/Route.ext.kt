package com.lightningkite.rekwest.server

import com.lightningkite.mirror.info.Type
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import java.util.*

private val ApplicationCallReceiveType = WeakHashMap<ApplicationCall, Type<*>>()
var ApplicationCall.receiveType: Type<*>?
    get() = ApplicationCallReceiveType[this]
    set(value){
        ApplicationCallReceiveType[this] = value
    }
suspend fun <T : Any> ApplicationCall.receive(type: Type<T>): T {
    this.receiveType = type
    @Suppress("UNCHECKED_CAST")
    return receive(type.kClass) as T
}

object NullPlaceholder

private val ApplicationCallRespondType = WeakHashMap<ApplicationCall, Type<*>>()
var ApplicationCall.respondType: Type<*>?
    get() = ApplicationCallRespondType[this]
    set(value){
        ApplicationCallRespondType[this] = value
    }
suspend fun <T> ApplicationCall.respond(type: Type<T>, value: T) {
    this.respondType = type
    @Suppress("UNCHECKED_CAST")
    return respond(value ?: NullPlaceholder)
}

suspend fun <T> ApplicationCall.respond(status: HttpStatusCode, type: Type<T>, value: T) {
    this.respondType = type
    @Suppress("UNCHECKED_CAST")
    return respond(status, value ?: NullPlaceholder)
}
