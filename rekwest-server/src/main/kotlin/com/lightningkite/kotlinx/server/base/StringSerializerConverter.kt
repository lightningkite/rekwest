package com.lightningkite.kotlinx.server.base

import com.lightningkite.kotlinx.reflection.kxType
import com.lightningkite.kotlinx.serialization.StringSerializer
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.content.ByteArrayContent
import io.ktor.content.TextContent
import io.ktor.features.ContentConverter
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.pipeline.PipelineContext
import io.ktor.request.ApplicationReceiveRequest
import io.ktor.request.contentCharset
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.io.jvm.javaio.toInputStream

class StringSerializerConverter(val serializer: StringSerializer): ContentConverter {
    val contentType = ContentType.parse(serializer.contentType)

    override suspend fun convertForReceive(context: PipelineContext<ApplicationReceiveRequest, ApplicationCall>): Any? {
        val request = context.subject
        val type = context.context.receiveKxType ?: request.type.kxType
        val value = request.value as? ByteReadChannel ?: return null
        val text = value.toInputStream().reader(context.call.request.contentCharset() ?: Charsets.UTF_8).use { it.readText() }
        return serializer.read(text, type)
    }

    override suspend fun convertForSend(context: PipelineContext<Any, ApplicationCall>, contentType: ContentType, value: Any): Any? {
        val type = context.context.respondKxType ?: value::class.kxType
        val text = serializer.write(value, type)
        return  TextContent(text, contentType)
    }
}