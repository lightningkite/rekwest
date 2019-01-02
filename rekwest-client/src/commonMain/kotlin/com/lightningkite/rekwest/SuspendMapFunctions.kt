package com.lightningkite.rekwest

import com.lightningkite.mirror.archive.database.SuspendMap
import com.lightningkite.mirror.archive.model.Condition
import com.lightningkite.mirror.archive.model.Operation
import com.lightningkite.mirror.archive.model.Sort
import com.lightningkite.mirror.info.ClassInfo
import com.lightningkite.mirror.info.ClassInfoRegistry
import com.lightningkite.mirror.info.Type
import com.lightningkite.mirror.info.allImplements

class SuspendMapFunctions<K, V : Any>(
        val keyType: Type<K>,
        val valueType: Type<V>,
        val howToInvoke: suspend (ServerFunction<*>) -> Any?,
        val getInfo: ClassInfo<out SuspendMapGetServerFunction<K, V>>? = null,
        val putInfo: ClassInfo<out SuspendMapPutServerFunction<K, V>>? = null,
        val modifyInfo: ClassInfo<out SuspendMapModifyServerFunction<K, V>>? = null,
        val removeInfo: ClassInfo<out SuspendMapRemoveServerFunction<K, V>>? = null,
        val queryInfo: ClassInfo<out SuspendMapQueryServerFunction<K, V>>? = null,
        val getNewKeyInfo: ClassInfo<out SuspendMapGetNewKeyServerFunction<K, V>>? = null,
        val findInfo: ClassInfo<out SuspendMapFindServerFunction<K, V>>? = null,
        val getManyInfo: ClassInfo<out SuspendMapGetManyServerFunction<K, V>>? = null,
        val putManyInfo: ClassInfo<out SuspendMapPutManyServerFunction<K, V>>? = null,
        val removeManyInfo: ClassInfo<out SuspendMapRemoveManyServerFunction<K, V>>? = null
) : SuspendMap<K, V> {

    class Builder<K, V : Any>(
            val keyType: Type<K>,
            val valueType: Type<V>,
            val howToInvoke: suspend (ServerFunction<*>) -> Any?,
            var get: ClassInfo<out SuspendMapGetServerFunction<K, V>>? = null,
            var put: ClassInfo<out SuspendMapPutServerFunction<K, V>>? = null,
            var modify: ClassInfo<out SuspendMapModifyServerFunction<K, V>>? = null,
            var remove: ClassInfo<out SuspendMapRemoveServerFunction<K, V>>? = null,
            var query: ClassInfo<out SuspendMapQueryServerFunction<K, V>>? = null,
            var getNewKey: ClassInfo<out SuspendMapGetNewKeyServerFunction<K, V>>? = null,
            var find: ClassInfo<out SuspendMapFindServerFunction<K, V>>? = null,
            var getMany: ClassInfo<out SuspendMapGetManyServerFunction<K, V>>? = null,
            var putMany: ClassInfo<out SuspendMapPutManyServerFunction<K, V>>? = null,
            var removeMany: ClassInfo<out SuspendMapRemoveManyServerFunction<K, V>>? = null
    ) {
        fun build(
        ): SuspendMapFunctions<K, V> = SuspendMapFunctions(
                keyType = keyType,
                valueType = valueType,
                howToInvoke = howToInvoke,
                getInfo = get,
                putInfo = put,
                modifyInfo = modify,
                removeInfo = remove,
                queryInfo = query,
                getNewKeyInfo = getNewKey,
                findInfo = find,
                getManyInfo = getMany,
                putManyInfo = putMany,
                removeManyInfo = removeMany
        )
    }

    suspend inline fun <T> ServerFunction<T>.invoke(): T {
        @Suppress("UNCHECKED_CAST")
        return howToInvoke.invoke(this) as T
    }

    override suspend fun find(condition: Condition<V>, sortedBy: Sort<V>?): Pair<K, V>? {
        return if (findInfo != null) {
            findInfo.construct(mapOf(
                    "condition" to condition,
                    "sortedBy" to sortedBy
            )).invoke()
        } else if (queryInfo != null) {
            queryInfo.construct(mapOf(
                    "condition" to condition,
                    "keyCondition" to Condition.Always<K>(),
                    "sortedBy" to sortedBy,
                    "after" to null,
                    "count" to 1
            )).invoke().firstOrNull()
        } else throw UnsupportedOperationException()
    }

    override suspend fun get(key: K): V? {
        return if (getInfo != null) {
            getInfo.construct(mapOf(
                    "key" to key
            )).invoke()
        } else throw UnsupportedOperationException()
    }

    override suspend fun getMany(keys: List<K>): Map<K, V?> {
        return if (getManyInfo != null) {
            getManyInfo.construct(mapOf(
                    "keys" to keys.toList()
            )).invoke()
        } else super.getMany(keys)
    }

    override suspend fun getNewKey(): K {
        return if (getNewKeyInfo != null) {
            getNewKeyInfo.construct(mapOf()).invoke()
        } else throw UnsupportedOperationException()
    }

    override suspend fun modify(key: K, operation: Operation<V>, condition: Condition<V>): V? {
        return if (modifyInfo != null) {
            modifyInfo.construct(mapOf(
                    "key" to key,
                    "operation" to operation,
                    "condition" to condition
            )).invoke()
        } else super.modify(key, operation, condition)
    }

    override suspend fun put(key: K, value: V, conditionIfExists: Condition<V>, create: Boolean): Boolean {
        return if (putInfo != null) {
            putInfo.construct(mapOf(
                    "key" to key,
                    "value" to value,
                    "conditionIfExists" to conditionIfExists,
                    "create" to create
            )).invoke()
        } else throw UnsupportedOperationException()
    }

    override suspend fun putMany(map: Map<K, V>) {
        return if (putManyInfo != null) {
            putManyInfo.construct(mapOf(
                    "map" to map
            )).invoke()
        } else super.putMany(map)
    }

    override suspend fun query(
            condition: Condition<V>,
            keyCondition: Condition<K>,
            sortedBy: Sort<V>?,
            after: Pair<K, V>?,
            count: Int
    ): List<Pair<K, V>> {
        return if (queryInfo != null) {
            queryInfo.construct(mapOf(
                    "condition" to condition,
                    "keyCondition" to keyCondition,
                    "sortedBy" to sortedBy,
                    "after" to after,
                    "count" to count
            )).invoke()
        } else throw UnsupportedOperationException()
    }

    override suspend fun remove(key: K, condition: Condition<V>): Boolean {
        return if (removeInfo != null) {
            removeInfo.construct(mapOf(
                    "key" to key,
                    "condition" to condition
            )).invoke()
        } else throw UnsupportedOperationException()
    }

    override suspend fun removeMany(keys: List<K>) {
        return if (removeManyInfo != null) {
            removeManyInfo.construct(mapOf(
                    "keys" to keys.toList()
            )).invoke()
        } else super.removeMany(keys)
    }

    companion object {

        fun all(
                howToInvoke: suspend (ServerFunction<*>) -> Any?,
                classInfoRegistry: ClassInfoRegistry
        ): Map<Pair<Type<Any?>, Type<Any>>, SuspendMapFunctions<Any?, Any>> {

            val map = HashMap<Type<*>, HashMap<Type<*>, SuspendMapFunctions.Builder<Any?, Any>>>()
            classInfoRegistry.values.forEach { classInfo ->
                classInfo.allImplements(classInfoRegistry).forEach { implements ->
                    when (implements.kClass) {
                        SuspendMapGetServerFunction::class -> {
                            map.getOrPut(implements.typeParameters[0].type) { HashMap() }
                                    .getOrPut(implements.typeParameters[1].type) {
                                        SuspendMapFunctions.Builder(
                                                keyType = implements.typeParameters[0].type as Type<Any?>,
                                                valueType = implements.typeParameters[1].type as Type<Any>,
                                                howToInvoke = howToInvoke
                                        )
                                    }
                                    .let { builder ->
                                        @Suppress("UNCHECKED_CAST")
                                        builder.get = classInfo as ClassInfo<out SuspendMapGetServerFunction<Any?, Any>>
                                    }
                        }
                        SuspendMapPutServerFunction::class -> {
                            map.getOrPut(implements.typeParameters[0].type) { HashMap() }
                                    .getOrPut(implements.typeParameters[1].type) {
                                        SuspendMapFunctions.Builder(
                                                keyType = implements.typeParameters[0].type as Type<Any?>,
                                                valueType = implements.typeParameters[1].type as Type<Any>,
                                                howToInvoke = howToInvoke
                                        )
                                    }
                                    .let { builder ->
                                        @Suppress("UNCHECKED_CAST")
                                        builder.put = classInfo as ClassInfo<out SuspendMapPutServerFunction<Any?, Any>>
                                    }
                        }
                        SuspendMapModifyServerFunction::class -> {
                            map.getOrPut(implements.typeParameters[0].type) { HashMap() }
                                    .getOrPut(implements.typeParameters[1].type) {
                                        SuspendMapFunctions.Builder(
                                                keyType = implements.typeParameters[0].type as Type<Any?>,
                                                valueType = implements.typeParameters[1].type as Type<Any>,
                                                howToInvoke = howToInvoke
                                        )
                                    }
                                    .let { builder ->
                                        @Suppress("UNCHECKED_CAST")
                                        builder.modify = classInfo as ClassInfo<out SuspendMapModifyServerFunction<Any?, Any>>
                                    }
                        }
                        SuspendMapRemoveServerFunction::class -> {
                            map.getOrPut(implements.typeParameters[0].type) { HashMap() }
                                    .getOrPut(implements.typeParameters[1].type) {
                                        SuspendMapFunctions.Builder(
                                                keyType = implements.typeParameters[0].type as Type<Any?>,
                                                valueType = implements.typeParameters[1].type as Type<Any>,
                                                howToInvoke = howToInvoke
                                        )
                                    }
                                    .let { builder ->
                                        @Suppress("UNCHECKED_CAST")
                                        builder.remove = classInfo as ClassInfo<out SuspendMapRemoveServerFunction<Any?, Any>>
                                    }
                        }
                        SuspendMapQueryServerFunction::class -> {
                            map.getOrPut(implements.typeParameters[0].type) { HashMap() }
                                    .getOrPut(implements.typeParameters[1].type) {
                                        SuspendMapFunctions.Builder(
                                                keyType = implements.typeParameters[0].type as Type<Any?>,
                                                valueType = implements.typeParameters[1].type as Type<Any>,
                                                howToInvoke = howToInvoke
                                        )
                                    }
                                    .let { builder ->
                                        @Suppress("UNCHECKED_CAST")
                                        builder.query = classInfo as ClassInfo<out SuspendMapQueryServerFunction<Any?, Any>>
                                    }
                        }
                        SuspendMapGetNewKeyServerFunction::class -> {
                            map.getOrPut(implements.typeParameters[0].type) { HashMap() }
                                    .getOrPut(implements.typeParameters[1].type) {
                                        SuspendMapFunctions.Builder(
                                                keyType = implements.typeParameters[0].type as Type<Any?>,
                                                valueType = implements.typeParameters[1].type as Type<Any>,
                                                howToInvoke = howToInvoke
                                        )
                                    }
                                    .let { builder ->
                                        @Suppress("UNCHECKED_CAST")
                                        builder.getNewKey = classInfo as ClassInfo<out SuspendMapGetNewKeyServerFunction<Any?, Any>>
                                    }
                        }
                        SuspendMapFindServerFunction::class -> {
                            map.getOrPut(implements.typeParameters[0].type) { HashMap() }
                                    .getOrPut(implements.typeParameters[1].type) {
                                        SuspendMapFunctions.Builder(
                                                keyType = implements.typeParameters[0].type as Type<Any?>,
                                                valueType = implements.typeParameters[1].type as Type<Any>,
                                                howToInvoke = howToInvoke
                                        )
                                    }
                                    .let { builder ->
                                        @Suppress("UNCHECKED_CAST")
                                        builder.find = classInfo as ClassInfo<out SuspendMapFindServerFunction<Any?, Any>>
                                    }
                        }
                        SuspendMapGetManyServerFunction::class -> {
                            map.getOrPut(implements.typeParameters[0].type) { HashMap() }
                                    .getOrPut(implements.typeParameters[1].type) {
                                        SuspendMapFunctions.Builder(
                                                keyType = implements.typeParameters[0].type as Type<Any?>,
                                                valueType = implements.typeParameters[1].type as Type<Any>,
                                                howToInvoke = howToInvoke
                                        )
                                    }
                                    .let { builder ->
                                        @Suppress("UNCHECKED_CAST")
                                        builder.getMany = classInfo as ClassInfo<out SuspendMapGetManyServerFunction<Any?, Any>>
                                    }
                        }
                        SuspendMapPutManyServerFunction::class -> {
                            map.getOrPut(implements.typeParameters[0].type) { HashMap() }
                                    .getOrPut(implements.typeParameters[1].type) {
                                        SuspendMapFunctions.Builder(
                                                keyType = implements.typeParameters[0].type as Type<Any?>,
                                                valueType = implements.typeParameters[1].type as Type<Any>,
                                                howToInvoke = howToInvoke
                                        )
                                    }
                                    .let { builder ->
                                        @Suppress("UNCHECKED_CAST")
                                        builder.putMany = classInfo as ClassInfo<out SuspendMapPutManyServerFunction<Any?, Any>>
                                    }
                        }
                        SuspendMapRemoveManyServerFunction::class -> {
                            map.getOrPut(implements.typeParameters[0].type) { HashMap() }
                                    .getOrPut(implements.typeParameters[1].type) {
                                        SuspendMapFunctions.Builder(
                                                keyType = implements.typeParameters[0].type as Type<Any?>,
                                                valueType = implements.typeParameters[1].type as Type<Any>,
                                                howToInvoke = howToInvoke
                                        )
                                    }
                                    .let { builder ->
                                        @Suppress("UNCHECKED_CAST")
                                        builder.removeMany = classInfo as ClassInfo<out SuspendMapRemoveManyServerFunction<Any?, Any>>
                                    }
                        }
                        else -> {
                        }
                    }
                }
            }
            return map.values.asSequence()
                    .flatMap { it.values.asSequence() }
                    .map { it.build() }
                    .associate { (it.keyType to it.valueType) to it }
        }
    }
}