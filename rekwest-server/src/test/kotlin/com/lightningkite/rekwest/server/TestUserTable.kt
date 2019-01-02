package com.lightningkite.rekwest.server

import com.lightningkite.kommon.exception.stackTraceString
import com.lightningkite.kommunicate.HttpClient
import com.lightningkite.kommunicate.HttpMethod
import com.lightningkite.kommunicate.callString
import com.lightningkite.lokalize.TimeStamp
import com.lightningkite.lokalize.now
import com.lightningkite.mirror.archive.model.Id
import com.lightningkite.mirror.info.implementsTree
import com.lightningkite.mirror.info.type
import com.lightningkite.mirror.serialization.DefaultRegistry
import com.lightningkite.mirror.serialization.json.JsonSerializer
import com.lightningkite.rekwest.ServerFunction
import com.lightningkite.rekwest.SuspendMapFunctions
import com.lightningkite.rekwest.invoke
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.AuthenticationProvider
import io.ktor.auth.basic
import io.ktor.auth.jwt.jwt
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.httpMethod
import io.ktor.request.uri
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.cio.CIO
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.testing.withTestApplication
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.concurrent.TimeUnit
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class TestUserTable {
    val registry = DefaultRegistry + TestRegistry
    val handler = ServerFunctionHandler<User>(registry.classInfoRegistry)
    val serializer = JsonSerializer(registry)

    init {
        handler.userSuspendMapSetup()
    }

    var server: ApplicationEngine? = null

    fun Application.app() {
        install(ContentNegotiation) {
            val converter = StringSerializerConverter(serializer)
            register(converter.contentType, converter)
        }
        install(StatusPages) {
            status(HttpStatusCode.NotFound) {
                call.respond(HttpStatusCode.NotFound, "Nothing here at ${call.request.httpMethod} ${call.request.uri}")
            }
            exception<Exception> {
                call.respond("Throwing error:\n ${it.stackTraceString()}")
            }
        }
        install(Authentication) {
            register(object : AuthenticationProvider("manual") {

            })
            jwt("jwt") {
                realm = "com.lightningkite"
                verifier(Tokens.verifier)
                validate {
                    it.payload.claims["user"]?.asString()?.let { id ->
                        PrincipalWrapper(UserSuspendMap.underlying.get(Id.fromUUIDString(id)))
                    }?.also {
                        println("Authenticated as $it")
                    }
                }
            }
        }
        routing {
            println("Setting up server function")
            get("hello") {
                call.respondText("HYPE", ContentType.Text.Plain, HttpStatusCode.Accepted)
            }
            with(handler) {
                serverFunction("function", false)
            }
        }
    }

    @KtorExperimentalAPI
    @BeforeTest
    fun before() {
        server = embeddedServer(CIO, port = 8080) {
            app()
        }.start(false)
        Thread.sleep(100L)
    }

    @AfterTest
    fun after() {
        server?.stop(0L, 100L, TimeUnit.MILLISECONDS)
    }

    @Test
    fun serverWorks() {
        runBlocking {
            assertEquals("HYPE", HttpClient.callString("http://localhost:8080/hello", HttpMethod.GET))
        }
    }

    @Test
    fun testAll() {
        runBlocking {
            val newUser = User(email = "josephivie@gmail.com", password = "testpasswordofnightmares")
            User.Put(newUser.id, newUser)
                    .invoke(
                            onEndpoint = "http://localhost:8080/function/",
                            serializer = serializer
                    )
                    .also { println(it) }
            val session = User.Login(newUser.email, newUser.password)
                    .invoke(
                            onEndpoint = "http://localhost:8080/function/",
                            serializer = serializer
                    )
                    .also { println(it) }
            User.Get(session.user.id)
                    .invoke(
                            onEndpoint = "http://localhost:8080/function/",
                            headers = mapOf("Authorization" to listOf(session.token)),
                            serializer = serializer
                    )
                    .also { println(it) }
        }
    }
}