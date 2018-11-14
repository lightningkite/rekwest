package com.lightningkite.kotlinx.server.base

import com.lightningkite.kotlinx.exception.ExceptionNames
import com.lightningkite.kotlinx.locale.TimeStamp
import com.lightningkite.kotlinx.persistence.*
import com.lightningkite.kotlinx.reflection.*
import com.lightningkite.kotlinx.server.ServerFunction

@ExternalReflection
data class User(
        override var id: Long? = null,
        @Indexed var email: String,
        var password: String,
        var role: User.Role = User.Role.Citizen,
        var rejectTokensBefore: TimeStamp = TimeStamp(0)
) : Model<Long> {

    fun getIdentifiers(): List<String> = listOf(email)


    //region Helper Data

    @ExternalReflection
    enum class Role {
        Admin,
        Citizen
    }

    @ExternalReflection
    data class Session(
            val user: User,
            val token: String
    )

    //endregion

    //region Server Functions

    @ExternalReflection
    @Throws(ExceptionNames.NoSuchElementException)
    class Get(val id: Reference<User, Long>) : ServerFunction<User>

    @ExternalReflection
    @Mutates
    @Throws(ExceptionNames.ForbiddenException)
    class Insert(val value: User) : ServerFunction<User.Session>

    @ExternalReflection
    @Mutates
    @Throws(ExceptionNames.ForbiddenException, ExceptionNames.NoSuchElementException)
    class Update(val value: User) : ServerFunction<User>

    @ExternalReflection
    @Mutates
    @Throws(ExceptionNames.ForbiddenException, ExceptionNames.NoSuchElementException)
    class Modify(val id: Reference<User, Long>, val modifications: List<ModificationOnItem<User, *>>) : ServerFunction<User>

    @ExternalReflection
    @Throws(ExceptionNames.ForbiddenException)
    class Query(
            val condition: ConditionOnItem<User> = ConditionOnItem.Always(),
            val sortedBy: List<SortOnItem<User, *>> = listOf(),
            val continuationToken: String? = null,
            val count: Int = 100
    ) : ServerFunction<QueryResult<User>>

    @ExternalReflection
    @Mutates
    @Throws(ExceptionNames.ForbiddenException, ExceptionNames.NoSuchElementException)
    class Delete(val id: Reference<User, Long>) : ServerFunction<Unit>

    @ExternalReflection
    @Throws(ExceptionNames.ForbiddenException, ExceptionNames.NoSuchElementException)
    class ResetPassword(val email: String): ServerFunction<Unit>

    @ExternalReflection
    @Throws(ExceptionNames.ForbiddenException, ExceptionNames.NoSuchElementException)
    class Login(val email: String, val password: String): ServerFunction<User.Session>

    //endregion
}
