package com.lightningkite.rekwest.server

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

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