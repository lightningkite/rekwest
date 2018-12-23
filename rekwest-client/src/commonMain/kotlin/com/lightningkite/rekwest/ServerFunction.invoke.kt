package com.lightningkite.rekwest

import com.lightningkite.kommunicate.HttpBody
import com.lightningkite.kommunicate.HttpClient
import com.lightningkite.kommunicate.HttpException
import com.lightningkite.kommunicate.HttpMethod
import com.lightningkite.mirror.info.Type
import com.lightningkite.mirror.info.TypeProjection
import com.lightningkite.mirror.info.allImplements
import com.lightningkite.mirror.info.type
import com.lightningkite.mirror.serialization.ByteArraySerializer
import com.lightningkite.mirror.serialization.SerializationException
import com.lightningkite.mirror.serialization.StringSerializer
import com.lightningkite.mirror.serialization.json.JsonSerializer

suspend fun <T> ServerFunction<T>.invoke(
        onEndpoint: String,
        headers: Map<String, List<String>> = mapOf(),
        serializer: StringSerializer
): T {
    return HttpClient.callStringDetail(
            onEndpoint,
            HttpMethod.POST,
            HttpBody.string(serializer.contentType, serializer.write(this, Type(ServerFunction::class, listOf(TypeProjection.STAR))).also{ println("Serialized: " + it) }),
            headers
    ).let {
        it.failure?.let { it as? HttpException }?.let {
            throw RemoteExceptionData.Thrown(serializer.read(it.message!!, RemoteExceptionData::class.type))
        }
        try {
            val returnType = serializer.registry.classInfoRegistry[this::class]!!
                    .allImplements(serializer.registry.classInfoRegistry)
                    .find { it.kClass == ServerFunction::class }!!
                    .typeParameters.first().type
            @Suppress("UNCHECKED_CAST")
            serializer.read(it.result!!, returnType as Type<T>)
        } catch (e: Exception) {
            throw SerializationException("Failed to read $it", e)
        }
    }
}

suspend fun <T> ServerFunction<T>.invoke(
        onEndpoint: String,
        headers: Map<String, List<String>> = mapOf(),
        serializer: ByteArraySerializer
): T {
    return HttpClient.callByteArrayDetail(
            onEndpoint,
            HttpMethod.POST,
            HttpBody.byteArray(serializer.contentType, serializer.write(this, Type(ServerFunction::class, listOf(TypeProjection.STAR)))),
            headers
    ).let {
        it.failure?.let { it as? HttpException }?.let {
            //TODO: Catch 'n throw
        }
        try {
            val returnType = serializer.registry.classInfoRegistry.getOrThrow(this::class)
                    .allImplements(serializer.registry.classInfoRegistry)
                    .find { it.kClass == ServerFunction::class }!!
                    .typeParameters.first().type
            @Suppress("UNCHECKED_CAST")
            serializer.read(it.result!!, returnType as Type<T>)
        } catch (e: Exception) {
            throw SerializationException("Failed to read $it", e)
        }
    }
}