package com.lightningkite.kotlinx.server.base.security

import com.lightningkite.kotlinx.persistence.*
import com.lightningkite.kotlinx.reflection.KxField
import com.lightningkite.kotlinx.reflection.KxVariable
import java.lang.IllegalArgumentException

abstract class PropertySecureTable<T : Model<ID>, ID>(val underlying: DatabaseTable<T, ID>) : DatabaseTable<T, ID> {
    interface PropertyRules<T : Any, V> {
        val variable: KxField<T, V>
        suspend fun query(untypedUser: Any?)
        suspend fun read(untypedUser: Any?, justInserted: Boolean, currentState: T): Boolean
        suspend fun write(untypedUser: Any?, currentState: T?, newState: V): V
    }

    @Suppress("UNCHECKED_CAST")
    val PropertyRules<T, *>.untyped
        get() = this as PropertyRules<T, Any?>

    abstract val propertyRules: Map<KxField<T, *>, PropertyRules<T, *>>
    abstract suspend fun wholeQuery(untypedUser: Any?)
    abstract suspend fun wholeRead(untypedUser: Any?, justInserted: Boolean, currentState: T): Boolean
    abstract suspend fun wholeWrite(untypedUser: Any?, isDelete: Boolean, currentState: T?)

    private suspend fun readAllowed(transaction: Transaction, item: T, isJustInserted: Boolean): Boolean {
        return wholeRead(transaction.untypedUser, isJustInserted, item) && propertyRules.values.all { it.read(transaction.untypedUser, isJustInserted, item) }
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun handleWholeItemUpdate(transaction: Transaction, old: T?, item: T?) {
        wholeWrite(transaction.untypedUser, item == null, old)
        if (item != null) {
            for (it in propertyRules.values) {
                val untyped = it.untyped
                val insecureEdit = untyped.variable.get(item)
                val securedEdit = untyped.write(transaction.untypedUser, old, insecureEdit)
                untyped.variable.set?.invoke(item, securedEdit)
            }
        }
    }


    override suspend fun get(transaction: Transaction, id: ID): T = underlying.get(transaction, id)
            .also { item ->
                val allowed = readAllowed(transaction, item, false)
                if (!allowed) throw IllegalAccessException()
            }

    override suspend fun getMany(transaction: Transaction, ids: Iterable<ID>): List<T> = underlying.getMany(transaction, ids)
            .filter { readAllowed(transaction, it, false) }

    override suspend fun insert(transaction: Transaction, model: T): T = underlying.insert(
            transaction,
            model.also {
                handleWholeItemUpdate(transaction, null, model)
            }
    ).also { item ->
        val allowed = readAllowed(transaction, item, true)
        if (!allowed) throw IllegalAccessException()
    }

    override suspend fun insertMany(transaction: Transaction, models: Collection<T>): Collection<T> = underlying.insertMany(
            transaction,
            models.also {
                it.forEach {
                    handleWholeItemUpdate(transaction, null, it)
                }
            }
    ).filter { readAllowed(transaction, it, true) }

    override suspend fun update(transaction: Transaction, model: T): T = underlying.update(transaction, model.also {
        handleWholeItemUpdate(transaction, underlying.get(transaction, model.id!!), it)
    }).also { item ->
        val allowed = readAllowed(transaction, item, true)
        if (!allowed) throw IllegalAccessException()
    }

    override suspend fun modify(transaction: Transaction, id: ID, modifications: List<ModificationOnItem<T, *>>): T {
        val old = underlying.get(transaction, id)
        wholeWrite(transaction.untypedUser, false, old)
        val newModifications = modifications.map { typedMod ->
            val untypedRules = propertyRules[typedMod.field]?.untyped ?: return@map typedMod
            @Suppress("UNCHECKED_CAST") val untypedMod = typedMod as ModificationOnItem<T, Any?>
            val originalSet = untypedMod.invokeOnSub(untypedRules.variable.get(old))
            val newSet = untypedRules.write(
                    untypedUser = transaction.untypedUser,
                    currentState = old,
                    newState = originalSet
            )
            if (originalSet != newSet) {
                if (untypedMod is ModificationOnItem.Set) {
                    untypedMod.value = newSet
                    typedMod
                } else {
                    ModificationOnItem.Set(untypedRules.variable as KxVariable<T, Any?>, newSet)
                }
            } else typedMod
        }
        return underlying.modify(transaction, id, newModifications).also { readAllowed(transaction, it, false) }
    }

    override suspend fun query(
            transaction: Transaction,
            condition: ConditionOnItem<T>,
            sortedBy: List<SortOnItem<T, *>>,
            continuationToken: String?,
            count: Int
    ): QueryResult<T> {
        wholeQuery(transaction.untypedUser)
        return underlying.query(
                transaction = transaction,
                condition = condition.also {
                    it.recursing().forEach {
                        if (it is ConditionOnItem.OnField<*, *>) {
                            propertyRules[it.field]?.query(transaction.untypedUser)
                        }
                    }
                },
                sortedBy = sortedBy.also {
                    it.forEach {
                        propertyRules[it.field]?.query(transaction.untypedUser)
                    }
                },
                continuationToken = continuationToken,
                count = count
        ).also {
            it.results = it.results.filter {
                readAllowed(transaction, it, false)
            }
        }
    }

    override suspend fun delete(transaction: Transaction, id: ID) {
        val old = underlying.get(transaction, id)
        handleWholeItemUpdate(transaction, old, null)
        return underlying.delete(transaction, id)
    }
}