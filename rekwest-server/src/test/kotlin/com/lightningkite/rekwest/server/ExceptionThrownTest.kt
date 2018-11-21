package com.lightningkite.rekwest.server

import com.lightningkite.kommon.exception.ForbiddenException
import com.lightningkite.kommon.exception.stackTraceString
import com.lightningkite.mirror.info.PrimitiveClassInfoRegistry
import com.lightningkite.mirror.info.type
import com.lightningkite.mirror.serialization.DefaultRegistry
import com.lightningkite.mirror.serialization.json.JsonSerializer
import com.lightningkite.rekwest.RemoteExceptionData
import com.lightningkite.rekwest.ServerFunction
import com.lightningkite.rekwest.server.PrincipalWrapper
import com.lightningkite.rekwest.server.StringSerializerConverter
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

    val registry = DefaultRegistry + TestRegistry
    val handler = ServerFunctionHandler(registry.classInfoRegistry)
    val serializer = JsonSerializer(registry)

    init{
        with(handler){
            ThrowExceptionRequest::class.invocation = {
                println("I'm going to die here.")
                throw ForbiddenException("NOPE")
            }
        }
    }

    @Test
    fun throwing() = withTestApplication({
        install(ContentNegotiation) {
            val converter = StringSerializerConverter(serializer)
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
            with(handler){
                serverFunction("function", false)
            }
        }
    }){
        println("Beginning test")
        with(handleRequest(HttpMethod.Post, "/function"){
            val body = serializer.write(ThrowExceptionRequest(), ServerFunction::class.type)
            println(body)
            this.setBody(body)
        }) {
            println("out: " + response.status())
            println("out: " + response.content)
            val out = serializer.read(response.content ?: "", RemoteExceptionData::class.type)
            println("out: " + out)
        }
    }
}