package com.lightningkite.rekwest.server

import com.lightningkite.kommon.exception.stackTraceString
import com.lightningkite.mirror.archive.use
import com.lightningkite.mirror.info.Type
import com.lightningkite.mirror.info.list
import com.lightningkite.mirror.info.type
import com.lightningkite.mirror.info.typeNullable
import com.lightningkite.rekwest.*
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.util.pipeline.ContextDsl
