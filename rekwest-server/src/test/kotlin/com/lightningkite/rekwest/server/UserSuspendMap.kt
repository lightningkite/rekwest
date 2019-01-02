package com.lightningkite.rekwest.server

import com.lightningkite.kommon.exception.ForbiddenException
import com.lightningkite.mirror.archive.database.RAMSuspendMap
import com.lightningkite.mirror.archive.database.secure.SecureSuspendMap
import com.lightningkite.mirror.archive.model.Condition
import com.lightningkite.mirror.archive.model.Id
import com.lightningkite.mirror.archive.model.Operation
import com.lightningkite.mirror.info.FieldInfo
import com.lightningkite.mirror.info.type
import com.lightningkite.rekwest.server.security.HasPassword

val UserSuspendMap = SecureSuspendMap<Id, User, User>(
        underlying = Settings.provider.suspendMap(Id::class.type, User::class.type),
        rules = HasPassword.rules()
)

fun ServerFunctionHandler<User>.userSuspendMapSetup() {
    suspendMap(UserSuspendMap) {
        get<User.Get>()
        put<User.Put>()
        modify<User.Modify>()
        remove<User.Remove>()
        query<User.Query>()
        invocation(User.Login::class) {
            val u = UserSuspendMap.underlying.find(
                    condition = Condition.Field(
                            field = UserClassInfo.fieldEmail,
                            condition = Condition.Equal(email)
                    )
            ) ?: throw ForbiddenException()
            User.Session(
                    user = u.second,
                    token = Tokens.generate(u.second)
            )
        }
        invocation(User.ResetPassword::class){
            val u = UserSuspendMap.underlying.find(
                    condition = Condition.Field(
                            field = UserClassInfo.fieldEmail,
                            condition = Condition.Equal(email)
                    )
            ) ?: throw ForbiddenException()
            val newPassword = (1..20).joinToString("") { ('a'..'z').random().toString() }
            UserSuspendMap.underlying.modify(
                    key = u.first,
                    operation = Operation.Fields(
                            classInfo = UserClassInfo,
                            changes = mapOf<FieldInfo<User, *>, Operation<*>>(
                                    UserClassInfo.fieldPassword to Operation.Set(newPassword)
                            )
                    )
            )
        }
    }
}
