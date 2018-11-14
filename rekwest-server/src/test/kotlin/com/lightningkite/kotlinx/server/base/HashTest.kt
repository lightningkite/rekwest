package com.lightningkite.kotlinx.server.base

import com.lightningkite.kotlin.crossplatform.kotlinxServerBaseTestReflections
import com.lightningkite.kotlinx.persistence.Transaction
import com.lightningkite.kotlinx.persistence.use
import com.lightningkite.kotlinx.reflection.ExternalReflection
import com.lightningkite.kotlinx.serialization.CommonSerialization
import kotlinx.coroutines.runBlocking
import org.junit.Test

class HashTest {

    init{
        kotlinxServerBaseTestReflections.forEach { CommonSerialization.ExternalNames.register(it) }
    }

    @Test fun test(){

        val testUser = User(email = "test@test.com", password = "please hash me")

        runBlocking {
            Transaction(null, false, false).use{
                val result = TestUserTable.insert(it, testUser.copy())
                assert(result.password != testUser.password)
            }
        }
    }
}