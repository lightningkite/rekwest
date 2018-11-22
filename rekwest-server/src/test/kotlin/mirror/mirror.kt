package com.lightningkite.rekwest.server

import com.lightningkite.kommon.native.SharedImmutable
import com.lightningkite.mirror.info.*
import kotlin.reflect.KClass

@SharedImmutable
val TestRegistry = ClassInfoRegistry(
    com.lightningkite.rekwest.RemoteExceptionDataClassInfo,
    com.lightningkite.rekwest.server.ThrowExceptionRequestClassInfo,
    com.lightningkite.rekwest.server.UserClassInfo,
    com.lightningkite.rekwest.server.UserRoleClassInfo,
    com.lightningkite.rekwest.server.UserSessionClassInfo,
    com.lightningkite.rekwest.server.UserGetClassInfo,
    com.lightningkite.rekwest.server.UserInsertClassInfo,
    com.lightningkite.rekwest.server.UserUpdateClassInfo,
    com.lightningkite.rekwest.server.UserModifyClassInfo,
    com.lightningkite.rekwest.server.UserQueryClassInfo,
    com.lightningkite.rekwest.server.UserDeleteClassInfo,
    com.lightningkite.rekwest.server.UserResetPasswordClassInfo,
    com.lightningkite.rekwest.server.UserLoginClassInfo,
    com.lightningkite.rekwest.ServerFunctionClassInfo,
    com.lightningkite.lokalize.TimeStampClassInfo,
    com.lightningkite.mirror.archive.ConditionOnItemClassInfo,
    com.lightningkite.mirror.archive.ConditionOnItemNeverClassInfo,
    com.lightningkite.mirror.archive.ConditionOnItemAlwaysClassInfo,
    com.lightningkite.mirror.archive.ConditionOnItemAndClassInfo,
    com.lightningkite.mirror.archive.ConditionOnItemOrClassInfo,
    com.lightningkite.mirror.archive.ConditionOnItemNotClassInfo,
    com.lightningkite.mirror.archive.ConditionOnItemEqualClassInfo,
    com.lightningkite.mirror.archive.ConditionOnItemEqualToOneClassInfo,
    com.lightningkite.mirror.archive.ConditionOnItemNotEqualClassInfo,
    com.lightningkite.mirror.archive.ConditionOnItemLessThanClassInfo,
    com.lightningkite.mirror.archive.ConditionOnItemGreaterThanClassInfo,
    com.lightningkite.mirror.archive.ConditionOnItemLessThanOrEqualClassInfo,
    com.lightningkite.mirror.archive.ConditionOnItemGreaterThanOrEqualClassInfo,
    com.lightningkite.mirror.archive.ConditionOnItemTextSearchClassInfo,
    com.lightningkite.mirror.archive.ConditionOnItemRegexTextSearchClassInfo,
    com.lightningkite.mirror.archive.ModificationOnItemClassInfo,
    com.lightningkite.mirror.archive.ModificationOnItemSetClassInfo,
    com.lightningkite.mirror.archive.ModificationOnItemAddClassInfo,
    com.lightningkite.mirror.archive.ModificationOnItemMultiplyClassInfo,
    com.lightningkite.mirror.archive.SortOnItemClassInfo,
    com.lightningkite.mirror.archive.QueryResultClassInfo,
    com.lightningkite.mirror.archive.ReferenceClassInfo
)