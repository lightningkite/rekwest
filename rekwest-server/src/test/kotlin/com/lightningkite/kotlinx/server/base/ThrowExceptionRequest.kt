package com.lightningkite.kotlinx.server.base

import com.lightningkite.kotlinx.reflection.ExternalReflection
import com.lightningkite.kotlinx.reflection.Throws
import com.lightningkite.kotlinx.server.ServerFunction

@ExternalReflection
@Throws("ForbiddenException")
class ThrowExceptionRequest(): ServerFunction<Unit>