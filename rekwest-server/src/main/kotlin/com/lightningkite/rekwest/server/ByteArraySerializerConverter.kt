package com.lightningkite.rekwest.server

import com.lightningkite.mirror.info.Type
import com.lightningkite.mirror.info.type
import com.lightningkite.mirror.serialization.ByteArraySerializer
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.content.ByteArrayContent
import io.ktor.content.TextContent
import io.ktor.features.ContentConverter
import io.ktor.http.ContentType
import io.ktor.request.ApplicationReceiveRequest
import io.ktor.request.contentCharset
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.io.jvm.javaio.toInputStream

class ByteArraySerializerConverter(val serializer: ByteArraySerializer) : ContentConverter {
    val contentType = ContentType.parse(serializer.contentType)

    override suspend fun convertForReceive(context: PipelineContext<ApplicationReceiveRequest, ApplicationCall>): Any? {
        val request = context.subject
        val type = context.context.receiveType ?: request.type.type
        val value = request.value as? ByteReadChannel ?: return null
        val text = value.toInputStream().use { it.readBytes() }
        return serializer.read(text, type)
    }

    override suspend fun convertForSend(context: PipelineContext<Any, ApplicationCall>, contentType: ContentType, value: Any): Any? {
        val type = context.context.respondType ?: value::class.type
        @Suppress("UNCHECKED_CAST") val text = serializer.write(
                if(value != NullPlaceholder) value else null,
                type as Type<Any?>
        )
        return ByteArrayContent(text, contentType)
    }
}