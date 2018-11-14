package com.lightningkite.kotlinx.server.base

import com.lightningkite.kotlinx.persistence.Transaction
import com.lightningkite.kotlinx.reflection.kxReflect
import com.lightningkite.kotlinx.server.ServerFunction
import java.util.*
import kotlin.reflect.KClass

private val KClassServerFunction_RequiresWrite = HashMap<KClass<*>, Boolean>()
var KClass<out ServerFunction<*>>.requiresWrite: Boolean
    set(value) {
        KClassServerFunction_RequiresWrite[this] = value
    }
    get() {
        return KClassServerFunction_RequiresWrite.getOrPut(this){
            this.kxReflect.annotations.any { it.name.endsWith("Mutates") }
        }
    }

private val KClassServerFunction_RequiresAtomicTransaction = HashMap<KClass<*>, Boolean>()
var KClass<out ServerFunction<*>>.requiresAtomicTransaction: Boolean
    set(value) {
        KClassServerFunction_RequiresAtomicTransaction[this] = value
    }
    get() {
        return KClassServerFunction_RequiresAtomicTransaction.getOrPut(this){
            this.kxReflect.annotations.any { it.name.endsWith("RequiresAtomicTransaction") }
        }
    }

private val KClassServerFunction_Invocation = HashMap<KClass<*>, suspend (Any, Transaction) -> Any?>()
@Suppress("UNCHECKED_CAST")
var <SF : ServerFunction<R>, R> KClass<SF>.invocation: suspend SF.(Transaction) -> R
    set(value) {
        KClassServerFunction_Invocation[this] = value as suspend (Any, Transaction) -> Any?
    }
    get() {
        return KClassServerFunction_Invocation[this] as suspend SF.(Transaction) -> R
    }

@Suppress("UNCHECKED_CAST")
suspend operator fun <R> ServerFunction<R>.invoke(transaction: Transaction): R
    = KClassServerFunction_Invocation[this::class]!!.invoke(this, transaction) as R

fun List<ServerFunction<*>>.transaction(untypedUser:Any?): Transaction = Transaction(
        untypedUser = untypedUser,
        atomic = any { it::class.requiresAtomicTransaction },
        readOnly = none { it::class.requiresWrite }
)

@Suppress("UNCHECKED_CAST")
suspend fun <R> ServerFunction<R>.invokeWithUser(untypedUser:Any?): R = invoke(listOf(this).transaction(untypedUser))