package com.lightningkite.rekwest.server

import com.lightningkite.kommon.exception.ExceptionNames
import com.lightningkite.lokalize.TimeStamp
import com.lightningkite.mirror.archive.model.*
import com.lightningkite.mirror.info.Indexed
import com.lightningkite.mirror.info.ThrowsTypes
import com.lightningkite.rekwest.*
import com.lightningkite.rekwest.server.security.HasPassword


data class User(
        override var id: Id = Id.key(),
        @Indexed var email: String,
        override var password: String,
        var role: User.Role = User.Role.Citizen,
        var rejectTokensBefore: TimeStamp = TimeStamp(0)
) : HasId, HasPassword {

    fun getIdentifiers(): List<String> = listOf(email)


    //region Helper Data

    enum class Role {
        Admin,
        Citizen
    }

    data class Session(
            val user: User,
            val token: String
    )

    //endregion

    //region Server Functions

    @ThrowsTypes(ExceptionNames.ForbiddenException)
    data class Get(override val key: Id) : SuspendMapGetServerFunction<Id, User>

    @ThrowsTypes(ExceptionNames.ForbiddenException)
    data class Put(
            override val key: Id,
            override val value: User,
            override val conditionIfExists: Condition<User> = Condition.Always(),
            override val create: Boolean = true
    ) : SuspendMapPutServerFunction<Id, User>

    @ThrowsTypes(ExceptionNames.ForbiddenException)
    data class Modify(
            override val key: Id,
            override val operation: Operation<User>,
            override val condition: Condition<User> = Condition.Always()
    ) : SuspendMapModifyServerFunction<Id, User>

    @ThrowsTypes(ExceptionNames.ForbiddenException)
    data class Remove(
            override val key: Id,
            override val condition: Condition<User> = Condition.Always()
    ) : SuspendMapRemoveServerFunction<Id, User>

    @ThrowsTypes(ExceptionNames.ForbiddenException)
    data class Query(
            override val condition: Condition<User> = Condition.Always(),
            override val keyCondition: Condition<Id> = Condition.Always(),
            override val sortedBy: Sort<User>? = null,
            override val after: Pair<Id, User>? = null,
            override val count: Int = 100
    ) : SuspendMapQueryServerFunction<Id, User>

    @ThrowsTypes(ExceptionNames.ForbiddenException, ExceptionNames.NoSuchElementException)
    data class ResetPassword(val email: String) : ServerFunction<Unit>

    @ThrowsTypes(ExceptionNames.ForbiddenException, ExceptionNames.NoSuchElementException)
    data class Login(val email: String, val password: String) : ServerFunction<User.Session>

    //endregion
}
