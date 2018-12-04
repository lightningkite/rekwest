package com.lightningkite.rekwest.server

import com.lightningkite.kommon.exception.ExceptionNames
import com.lightningkite.mirror.archive.*
import com.lightningkite.mirror.info.Mutates
import com.lightningkite.mirror.info.ThrowsTypes
import com.lightningkite.rekwest.ServerFunction

@ThrowsTypes(ExceptionNames.NoSuchElementException)
class DatabaseGetServerFunction<T: HasId>(val id: Reference<T>) : ServerFunction<T?>

@Mutates
@ThrowsTypes(ExceptionNames.ForbiddenException)
class DatabaseInsertServerFunction<T: HasId>(val value: T) : ServerFunction<T>

@Mutates
@ThrowsTypes(ExceptionNames.ForbiddenException, ExceptionNames.NoSuchElementException)
class DatabaseUpdateServerFunction<T: HasId>(val value: T) : ServerFunction<T>

@Mutates
@ThrowsTypes(ExceptionNames.ForbiddenException, ExceptionNames.NoSuchElementException)
class DatabaseModifyServerFunction<T: HasId>(val id: Reference<T>, val modifications: List<ModificationOnItem<T, *>>) : ServerFunction<T>

@ThrowsTypes(ExceptionNames.ForbiddenException)
class DatabaseQueryServerFunction<T: HasId>(
        val condition: ConditionOnItem<T> = ConditionOnItem.Always(),
        val sortedBy: List<SortOnItem<T, *>> = listOf(),
        val continuationToken: String? = null,
        val count: Int = 100
) : ServerFunction<QueryResult<T>>

@Mutates
@ThrowsTypes(ExceptionNames.ForbiddenException, ExceptionNames.NoSuchElementException)
class DatabaseDeleteServerFunction<T: HasId>(val id: Reference<T>) : ServerFunction<Unit>