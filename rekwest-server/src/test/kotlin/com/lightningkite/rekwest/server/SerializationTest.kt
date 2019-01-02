package com.lightningkite.rekwest.server

import com.lightningkite.mirror.info.Type
import com.lightningkite.mirror.info.type
import com.lightningkite.mirror.serialization.DefaultRegistry
import com.lightningkite.mirror.serialization.json.JsonSerializer
import com.lightningkite.rekwest.ServerFunction
import org.junit.Test

class SerializationTest {

    val registry = DefaultRegistry + TestRegistry
    val serializer = JsonSerializer(registry)

    fun <T> test(value: T, type: Type<T>) {
        val result = serializer.write(value, type)
        println(result)
        val back = serializer.read(result, type)
    }

    @Test fun test(){
        val newUser = User(email = "josephivie@gmail.com", password = "testpasswordofnightmares")
        test(User.Put(newUser.id, newUser), User.Put::class.type)
        test(User.Put(newUser.id, newUser), ServerFunction::class.type)
    }
}