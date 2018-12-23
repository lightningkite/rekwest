package com.lightningkite.rekwest.server

import com.lightningkite.mirror.archive.Transaction
import com.lightningkite.mirror.archive.use
import kotlinx.coroutines.runBlocking
import org.junit.Test

class HashTest {

    @Test
    fun test() {

        val testUser = User(email = "test@test.com", password = "please hash me")

        runBlocking {
            val result = UserTable.insert(testUser.copy())
            assert(result.password != testUser.password)

        }
    }
}