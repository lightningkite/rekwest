package com.lightningkite.rekwest.server

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.lightningkite.kommon.exception.ElementAlreadyExistsException
import com.lightningkite.kommon.exception.ForbiddenException
import com.lightningkite.mirror.archive.*
import com.lightningkite.mirror.archive.secure.PropertySecureTable
import com.lightningkite.mirror.info.FieldInfo
import com.lightningkite.mirror.serialization.DefaultRegistry
import com.lightningkite.rekwest.server.security.HashedFieldRules


object UserTable : PropertySecureTable<User>(
        classInfo = UserClassInfo,
        underlying = InMemoryDatabase(FullRegistry).table(User::class)
) {

    object Tokens {

        const val claimUserKey = "user"
        val issuer = "com.lightningkite"

        val algorithm: Algorithm by lazy {
            Algorithm.HMAC512("test secret".toByteArray())
        }
        val verifier by lazy {
            JWT.require(algorithm)
                    .acceptExpiresAt(Long.MAX_VALUE)
                    .withIssuer(issuer)
                    .build()
        }

        fun generate(user: User): String = JWT.create()
                .withIssuedAt(java.util.Date())
                .withClaim("user", user.id.toUUIDString())
                .withIssuer(issuer)
                .sign(algorithm)
    }

    val passwordRules = HashedFieldRules(
            variable = UserClassInfo.Fields.password,
            getIdentifiers = { listOf(it.email) },
            atLeastEntropy = { 5 }
    )
    val emailRules = object : PropertyRules<User, String> {
        override val variable: FieldInfo<User, String> = UserClassInfo.Fields.email
        override suspend fun query(untypedUser: Any?) {}
        override suspend fun read(untypedUser: Any?, justInserted: Boolean, currentState: User) = currentState.email
        override suspend fun write(untypedUser: Any?, currentState: User?, newState: String): String {
            //Make sure emails are unique
            val existing = Transaction(null, false, true).use {
                underlying.queryOne(it, ConditionOnItem.Equal(UserClassInfo.Fields.email, newState))
            }
            if (existing != null) throw ElementAlreadyExistsException("A user already exists with the email $newState.")
            return newState
        }

    }
    val roleRules = object : PropertyRules<User, User.Role> {
        override val variable: FieldInfo<User, User.Role> = UserClassInfo.Fields.role
        override suspend fun query(untypedUser: Any?) {}
        override suspend fun read(untypedUser: Any?, justInserted: Boolean, currentState: User) = currentState.role
        override suspend fun write(untypedUser: Any?, currentState: User?, newState: User.Role): User.Role {
            if (untypedUser?.let { it as? User }?.role ?: User.Role.Citizen < newState) {
                throw ForbiddenException("You cannot elevate a role to one higher than yours.")
            }
            return newState
        }
    }
    override val propertyRules: Map<FieldInfo<User, *>, PropertyRules<User, *>> = listOf<PropertyRules<User, *>>(
            passwordRules,
            emailRules,
            roleRules
    ).associate { it.variable to it }

    override suspend fun wholeQuery(untypedUser: Any?) = ConditionOnItem.Always<User>()
    override suspend fun wholeRead(untypedUser: Any?, justInserted: Boolean, currentState: User): Boolean = true
    override suspend fun wholeWrite(untypedUser: Any?, isDelete: Boolean, currentState: User?) {
        when {
            currentState == null -> {
                //Making users is OK
            }
            currentState.id == (untypedUser as? User)?.id -> {
                //You can modify yourself
            }
            (untypedUser as? User)?.role ?: User.Role.Citizen >= User.Role.Admin -> {
                //Admins can do whatever they please
            }
            else -> throw ForbiddenException("You do not have permission to modify this user.")
        }
    }

    fun setup(handler: ServerFunctionHandler) = with(handler) {
        User.Get::class.invocation = { get(it, this.id.id) }
        User.Insert::class.invocation = {
            insert(it, value).let {
                User.Session(
                        user = it,
                        token = Tokens.generate(it)
                )
            }
        }
        User.Update::class.invocation = { update(it, value) }
        User.Modify::class.invocation = { modify(it, id.id, modifications) }
        User.Query::class.invocation = { query(it, condition, sortedBy, continuationToken, count) }
        User.Delete::class.invocation = { delete(it, id.id) }
        User.Login::class.invocation = {
            val raw = underlying.queryOne(
                    transaction = it,
                    condition = ConditionOnItem.Equal(UserClassInfo.Fields.email, email)
            ) ?: throw NoSuchElementException()
            if (passwordRules.check(raw, password)) {
                User.Session(
                        user = raw,
                        token = Tokens.generate(raw)
                )
            } else {
                throw ForbiddenException("Wrong password")
            }
        }
        User.ResetPassword::class.invocation = {
            val raw = underlying.queryOne(
                    transaction = it,
                    condition = ConditionOnItem.Equal(UserClassInfo.Fields.email, email)
            ) ?: throw NoSuchElementException()
            val newPassword = (1..20).joinToString("") { ('a'..'z').random().toString() }
            modify(
                    transaction = it,
                    id = raw.id!!,
                    modifications = listOf(
                            ModificationOnItem.Set(UserClassInfo.Fields.password, newPassword)
                    )
            )
//            Email.send(
//                    to = email,
//                    subject = "Password Reset",
//                    body = "Your password has been reset to $newPassword."
//            )
        }
    }
}