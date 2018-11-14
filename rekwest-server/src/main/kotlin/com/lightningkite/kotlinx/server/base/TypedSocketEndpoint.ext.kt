package com.lightningkite.kotlinx.server.base

import com.lightningkite.kotlinx.httpclient.HttpWebSocket
import com.lightningkite.kotlinx.server.TypedSocketEndpoint
import java.util.*
import kotlin.reflect.KClass

private val KClassTypedSocketEndpoint_Invocation = HashMap<KClass<*>, suspend (Any, HttpWebSocket<*, *>) -> Unit>()
@Suppress("UNCHECKED_CAST")
var <TSE : TypedSocketEndpoint<CLIENT, SERVER>, CLIENT, SERVER> KClass<TSE>.invocation: suspend TSE.(HttpWebSocket<SERVER, CLIENT>) -> Unit
    set(value) {
        KClassTypedSocketEndpoint_Invocation[this] = value as suspend (Any, HttpWebSocket<*, *>) -> Unit
    }
    get() {
        return KClassTypedSocketEndpoint_Invocation[this] as suspend TSE.(HttpWebSocket<SERVER, CLIENT>) -> Unit
    }

@Suppress("UNCHECKED_CAST")
suspend operator fun <TSE : TypedSocketEndpoint<CLIENT, SERVER>, CLIENT, SERVER> TSE.invoke(socket: HttpWebSocket<SERVER, CLIENT>): Unit
    = KClassTypedSocketEndpoint_Invocation[this::class]!!.invoke(this, socket)