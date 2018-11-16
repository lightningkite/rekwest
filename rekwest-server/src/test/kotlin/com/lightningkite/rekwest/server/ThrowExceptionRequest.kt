package com.lightningkite.rekwest.server

import com.lightningkite.mirror.info.ThrowsTypes
import com.lightningkite.rekwest.ServerFunction

@ThrowsTypes("ForbiddenException")
class ThrowExceptionRequest(): ServerFunction<Unit>