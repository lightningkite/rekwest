package com.lightningkite.kotlinx.server.base

import com.lightningkite.kotlin.crossplatform.kotlinxServerBaseTestReflections
import com.lightningkite.kotlinx.exception.ForbiddenException
import com.lightningkite.kotlinx.exception.stackTraceString
import com.lightningkite.kotlinx.kotlinxCommonReflections
import com.lightningkite.kotlinx.reflection.kxReflect
import com.lightningkite.kotlinx.reflection.kxType
import com.lightningkite.kotlinx.serialization.CommonSerialization
import com.lightningkite.kotlinx.serialization.json.JsonSerializer
import com.lightningkite.kotlinx.server.RemoteExceptionData
import com.lightningkite.kotlinx.server.ServerFunction
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.basic
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import org.junit.Test
import kotlin.test.assertEquals

class ExceptionThrownTest {

    init{
        kotlinxCommonReflections.forEach { CommonSerialization.ExternalNames.register(it) }
        kotlinxServerBaseTestReflections.forEach { CommonSerialization.ExternalNames.register(it) }
        ThrowExceptionRequest::class.invocation = {
            println("I'm going to die here.")
            throw ForbiddenException("NOPE")
        }
    }

    @Test
    fun throwing() = withTestApplication({
        install(ContentNegotiation) {
            val converter = StringSerializerConverter(JsonSerializer)
            register(converter.contentType, converter)
        }
        install(StatusPages) {
            status(HttpStatusCode.NotFound) {
                call.respond("Nothing here")
            }
            exception<Exception> {
                call.respond("Throwing error:\n ${it.stackTraceString()}")
            }
        }
        install(Authentication){
            basic {
                validate {
                    PrincipalWrapper(Unit)
                }
            }
        }
        routing {
            println("Setting up server function")
            get("hello"){
                call.respondText("HYPE", ContentType.Text.Plain, HttpStatusCode.Accepted)
            }
            serverFunction("function", false)
        }
    }){
        println("Beginning test")
        with(handleRequest(HttpMethod.Post, "/function"){
            val body = JsonSerializer.write(ThrowExceptionRequest(), ServerFunction::class.kxType)
            println(body)
            this.setBody(body)
        }) {
            println(response.status())
            println(response.content)
            val out = JsonSerializer.read(response.content ?: "", RemoteExceptionData::class.kxType)
            println(out)
        }
    }
}