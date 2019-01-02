package com.lightningkite.rekwest

import com.lightningkite.mirror.archive.model.Condition
import com.lightningkite.mirror.archive.model.Operation
import com.lightningkite.mirror.archive.model.Sort

interface SuspendMapServerFunction<K, V: Any> {

}

interface SuspendMapGetServerFunction<K, V: Any> : ServerFunction<V?>, SuspendMapServerFunction<K, V> {
    val key: K
}

interface SuspendMapPutServerFunction<K, V: Any> : ServerFunction<Boolean>, SuspendMapServerFunction<K, V> {
    val key: K
    val value: V
    val conditionIfExists: Condition<V>
    val create: Boolean
}

interface SuspendMapModifyServerFunction<K, V: Any> : ServerFunction<V?>, SuspendMapServerFunction<K, V> {
    val key: K
    val operation: Operation<V>
    val condition: Condition<V>
}

interface SuspendMapRemoveServerFunction<K, V: Any> : ServerFunction<Boolean>, SuspendMapServerFunction<K, V> {
    val key: K
    val condition: Condition<V>
}

interface SuspendMapQueryServerFunction<K, V: Any> : ServerFunction<List<Pair<K, V>>>, SuspendMapServerFunction<K, V> {
    val condition: Condition<V>
    val keyCondition: Condition<K>
    val sortedBy: Sort<V>?
    val after: Pair<K, V>?
    val count: Int
}

interface SuspendMapGetNewKeyServerFunction<K, V: Any> : ServerFunction<K>, SuspendMapServerFunction<K, V>

interface SuspendMapFindServerFunction<K, V: Any>: ServerFunction<Pair<K, V>?>, SuspendMapServerFunction<K, V> {
    val condition: Condition<V>
    val sortedBy: Sort<V>?
}

interface SuspendMapGetManyServerFunction<K, V: Any>: ServerFunction<Map<K, V?>>, SuspendMapServerFunction<K, V> {
    val keys: List<K>
}

interface SuspendMapPutManyServerFunction<K, V: Any>: ServerFunction<Unit>, SuspendMapServerFunction<K, V> {
    val map: Map<K, V>
}

interface SuspendMapRemoveManyServerFunction<K, V: Any>: ServerFunction<Unit>, SuspendMapServerFunction<K, V> {
    val keys: List<K>
}