package com.lightningkite.kotlinx.server.base

import com.lightningkite.kotlinx.reflection.KxType
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import java.util.*

private val ApplicationCallReceiveKxType = WeakHashMap<ApplicationCall, KxType>()
var ApplicationCall.receiveKxType: KxType?
    get() = ApplicationCallReceiveKxType[this]
    set(value){
        ApplicationCallReceiveKxType[this] = value
    }
suspend fun <T : Any> ApplicationCall.receive(type: KxType): T {
    this.receiveKxType = type
    @Suppress("UNCHECKED_CAST")
    return receive(type.base.kclass) as T
}


private val ApplicationCallRespondKxType = WeakHashMap<ApplicationCall, KxType>()
var ApplicationCall.respondKxType: KxType?
    get() = ApplicationCallRespondKxType[this]
    set(value){
        ApplicationCallRespondKxType[this] = value
    }
suspend fun <T : Any> ApplicationCall.respond(type: KxType, value: T) {
    this.respondKxType = type
    @Suppress("UNCHECKED_CAST")
    return respond(value)
}

suspend fun <T : Any> ApplicationCall.respond(status: HttpStatusCode, type: KxType, value: T) {
    this.respondKxType = type
    @Suppress("UNCHECKED_CAST")
    return respond(status, value)
}
