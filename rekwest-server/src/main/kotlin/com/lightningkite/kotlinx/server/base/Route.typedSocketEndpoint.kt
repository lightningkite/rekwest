package com.lightningkite.kotlinx.server.base

import com.lightningkite.kotlinx.httpclient.HttpWebSocket
import com.lightningkite.kotlinx.reflection.kxType
import com.lightningkite.kotlinx.serialization.StringSerializer
import com.lightningkite.kotlinx.serialization.mapSerializer
import com.lightningkite.kotlinx.server.TypedSocketEndpoint
import com.lightningkite.kotlinx.server.fromServerType
import com.lightningkite.kotlinx.server.invoke
import com.lightningkite.kotlinx.server.toServerType
import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import io.ktor.pipeline.ContextDsl
import io.ktor.routing.Route
import io.ktor.websocket.webSocket
import kotlinx.coroutines.channels.mapNotNull
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.mapNotNull


@ContextDsl
fun Route.typedSocketEndpoint(path: String, serializer: StringSerializer) {
    webSocket(path) {

        val request = incoming.mapNotNull { frame ->
            if (frame is Frame.Text) {
                serializer.read(frame.readText(), TypedSocketEndpoint::class.kxType) as TypedSocketEndpoint<*, *>
            } else null
        }.receive() as TypedSocketEndpoint<Any?, Any?>

        val socket = object : HttpWebSocket<String, String> {
            override var onMessage: (String) -> Unit = {}

            override var onDisconnect: (closureCode: Int?, closureReason: String?, closureThrowable: Throwable?) -> Unit = { _, _, _ -> }

            override fun send(data: String) {
                async {
                    send(Frame.Text(data))
                }
            }

            override fun close(code: Int, reason: String) {
                async {
                    close(CloseReason(code.toShort(), reason))
                }
            }
        }

        try {
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    socket.onMessage.invoke(frame.readText())
                }
            }
        } finally {
        }

        request.invoke(socket.mapSerializer(serializer, request.fromServerType, serializer, request.toServerType))
    }
}
