package com.lightningkite.rekwest.server

import com.lightningkite.mirror.archive.database.SuspendMap
import com.lightningkite.mirror.archive.database.secure.SecureSuspendMap
import com.lightningkite.mirror.archive.model.Condition
import com.lightningkite.mirror.archive.model.Operation
import com.lightningkite.mirror.archive.model.Sort
import com.lightningkite.rekwest.*
import kotlin.reflect.KClass

class ServerFunctionHandlerSuspendMapBuilder<USER, K, V : Any>(
        val handler: ServerFunctionHandler<USER>,
        val suspendMapGet: (USER?)-> SuspendMap<K, V>
) {
    inline fun <reified SF: SuspendMapGetServerFunction<K, V>> get() = with(handler)  {
        SF::class.invocation = { user -> suspendMapGet(user).get(key) }
    }
    inline fun <reified SF: SuspendMapPutServerFunction<K, V>> put() = with(handler)  {
        SF::class.invocation = { user -> suspendMapGet(user).put(key, value, conditionIfExists, create) }
    }
    inline fun <reified SF: SuspendMapModifyServerFunction<K, V>> modify() = with(handler)  {
        SF::class.invocation = { user -> suspendMapGet(user).modify(key, operation, condition) }
    }
    inline fun <reified SF: SuspendMapRemoveServerFunction<K, V>> remove() = with(handler)  {
        SF::class.invocation = { user -> suspendMapGet(user).remove(key, condition) }
    }
    inline fun <reified SF: SuspendMapQueryServerFunction<K, V>> query() = with(handler)  {
        SF::class.invocation = { user -> suspendMapGet(user).query(condition, keyCondition, sortedBy, after, count) }
    }
    inline fun <reified SF: SuspendMapGetNewKeyServerFunction<K, V>> getNewKey() = with(handler)  {
        SF::class.invocation = { user -> suspendMapGet(user).getNewKey() }
    }
    inline fun <reified SF: SuspendMapFindServerFunction<K, V>> find() = with(handler)  {
        SF::class.invocation = { user -> suspendMapGet(user).find(condition, sortedBy) }
    }
    inline fun <reified SF: SuspendMapGetManyServerFunction<K, V>> getMany() = with(handler)  {
        SF::class.invocation = { user -> suspendMapGet(user).getMany(keys) }
    }
    inline fun <reified SF: SuspendMapPutManyServerFunction<K, V>> putMany() = with(handler)  {
        SF::class.invocation = { user -> suspendMapGet(user).putMany(map) }
    }
    inline fun <reified SF: SuspendMapRemoveManyServerFunction<K, V>> removeMany() = with(handler)  {
        SF::class.invocation = { user -> suspendMapGet(user).removeMany(keys) }
    }
}

fun <USER, K, V : Any> ServerFunctionHandler<USER>.suspendMap(
        suspendMapGet: SecureSuspendMap<K, V, USER>,
        setup: ServerFunctionHandlerSuspendMapBuilder<USER, K, V>.()->Unit
) = ServerFunctionHandlerSuspendMapBuilder(this) { suspendMapGet.forUser(it) }.apply(setup)