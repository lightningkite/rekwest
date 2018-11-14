package com.lightningkite.kotlinx.server.base.security

import com.lightningkite.kotlinx.persistence.ModificationOnItem
import com.lightningkite.kotlinx.persistence.invoke
import com.lightningkite.kotlinx.reflection.KxClass
import com.lightningkite.kotlinx.reflection.KxVariable
import com.lightningkite.kotlinx.reflection.untyped
import com.lightningkite.kotlinx.server.invoke
import java.util.*

sealed class SecurityAction<T> {

    @Suppress("NOTHING_TO_INLINE")
    inline fun typeless() = this as SecurityAction<*>

    object Allow : SecurityAction<Any?>()
    object Deny : SecurityAction<Any?>()
    object Ignore : SecurityAction<Any?>()
    class Obscure<T>(val value:T) : SecurityAction<T>()
    class Tweak<T>(val value:T) : SecurityAction<T>()

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun <T> allow(): SecurityAction<T> = Allow as SecurityAction<T>
        fun <T> deny(): SecurityAction<T> = Deny as SecurityAction<T>
        fun <T> ignore(): SecurityAction<T> = Ignore as SecurityAction<T>
        fun <T> obscure(value:T): SecurityAction<T> = Obscure(value)
        fun <T> tweak(value:T): SecurityAction<T> = Tweak(value)
    }
}


//private val KxVariableSecurityRuleRead = WeakHashMap<KxVariable<*, *>, SecurityRuleRead<*>>()
//@Suppress("UNCHECKED_CAST")
//var <T> KxVariable<*, T>.securityRuleRead: SecurityRuleRead<T>
//    get() = KxVariableSecurityRuleRead[this] as? SecurityRuleRead<T> ?: { _, _ -> SecurityAction.deny() }
//    set(value) {
//        KxVariableSecurityRuleRead[this] = value
//    }
//@Suppress("UNCHECKED_CAST")
//val KxVariable<*, *>.securityRuleReadTypeless : SecurityRuleRead<Any?>
//    get() = KxVariableSecurityRuleRead[this] as? SecurityRuleRead<Any?> ?: { _, _ -> SecurityAction.deny() }
//
//private val KxVariableSecurityRuleWrite = WeakHashMap<KxVariable<*, *>, SecurityRuleWrite<*>>()
//@Suppress("UNCHECKED_CAST")
//var <T> KxVariable<*, T>.securityRuleWrite: SecurityRuleWrite<T>
//    get() = KxVariableSecurityRuleWrite[this] as? SecurityRuleWrite<T> ?: { _, _, _ -> SecurityAction.deny() }
//    set(value){
//        KxVariableSecurityRuleWrite[this] = value
//    }
//@Suppress("UNCHECKED_CAST")
//val KxVariable<*, *>.securityRuleWriteTypeless : SecurityRuleWrite<Any?>
//    get() = KxVariableSecurityRuleWrite[this] as? SecurityRuleWrite<Any?> ?: { _, _, _ -> SecurityAction.deny() }
//
//
//
//private val KxClassSecurityRuleWrite = WeakHashMap<KxClass<*>, SecurityRuleWrite<*>>()
//@Suppress("UNCHECKED_CAST")
//var <T: Any> KxClass<T>.securityRuleWrite: SecurityRuleWrite<T>
//    get() = KxClassSecurityRuleWrite[this] as? SecurityRuleWrite<T> ?: { _, _, _ -> SecurityAction.deny() }
//    set(value){
//        KxClassSecurityRuleWrite[this] = value
//    }
//
//private val KxClassSecurityRuleRead = WeakHashMap<KxClass<*>, SecurityRuleRead<*>>()
//@Suppress("UNCHECKED_CAST")
//var <T: Any> KxClass<T>.securityRuleRead: SecurityRuleRead<T>
//    get() = KxClassSecurityRuleRead[this] as? SecurityRuleRead<T> ?: { _, _ -> SecurityAction.deny() }
//    set(value){
//        KxClassSecurityRuleRead[this] = value
//    }
//
//
//
//fun <T: Any> KxClass<T>.readFilter(untypedUser: Any?, currentState: T): T {
//    //check general
//    val classSecurityAction = securityRuleRead.invoke(untypedUser, currentState)
//    when(classSecurityAction.typeless()){
//        SecurityAction.Allow -> {}
//        SecurityAction.Ignore -> throw IllegalAccessException()
//        SecurityAction.Deny -> throw IllegalAccessException()
//        is SecurityAction.Obscure -> return (classSecurityAction as SecurityAction.Obscure<T>).value
//    }
//
//    //Check fields
//    for(field in this.variables.values){
//        val fieldSecurityAction = field.securityRuleReadTypeless.invoke(untypedUser, field.get(currentState))
//        @Suppress("UNCHECKED_CAST")
//        when(fieldSecurityAction.typeless()){
//            SecurityAction.Allow -> {}
//            SecurityAction.Ignore -> {}
//            SecurityAction.Deny -> throw IllegalAccessException()
//            is SecurityAction.Obscure -> field.set.untyped(currentState, (classSecurityAction as SecurityAction.Obscure<Any?>).value)
//        }
//    }
//
//    return currentState
//}
//
//fun <T: Any> KxClass<T>.writeFilter(untypedUser: Any?, currentState: T, nextState: T): T {
//    //check general
//    val classSecurityAction = securityRuleWrite.invoke(untypedUser, currentState, nextState)
//    when(classSecurityAction.typeless()){
//        SecurityAction.Allow -> {}
//        SecurityAction.Ignore -> { return currentState }
//        SecurityAction.Deny -> throw IllegalAccessException()
//        is SecurityAction.Obscure -> return (classSecurityAction as SecurityAction.Obscure<T>).value
//    }
//
//    //Check fields
//    for(field in this.variables.values){
//        val fieldSecurityAction = field.securityRuleWriteTypeless.invoke(untypedUser, field.get(currentState), field.get(nextState))
//        @Suppress("UNCHECKED_CAST")
//        when(fieldSecurityAction.typeless()){
//            SecurityAction.Allow -> {  }
//            SecurityAction.Ignore -> { field.set.untyped(nextState, field.get.untyped(currentState)) }
//            SecurityAction.Deny -> throw IllegalAccessException()
//            is SecurityAction.Obscure -> { field.set.untyped(nextState, field.get.untyped(currentState)) }
//        }
//    }
//
//    return currentState
//}
//
//fun <T: Any> KxClass<T>.modifyFilter(untypedUser: Any?, currentState: T, copy: T, modifications: List<ModificationOnItem<T, *>>):List<ModificationOnItem<T, *>>{
//    //check general
//    modifications.invoke(copy)
//    val classSecurityAction = securityRuleWrite.invoke(untypedUser, currentState, copy)
//    when(classSecurityAction.typeless()){
//        SecurityAction.Allow -> {}
//        SecurityAction.Ignore -> { return listOf() }
//        SecurityAction.Deny -> throw IllegalAccessException()
//        is SecurityAction.Obscure -> throw IllegalAccessException()
//    }
//
//    //Check fields
//    val outModifications = modifications.toMutableList()
//    for(field in modifications.asSequence().mapNotNull { it.field as? KxVariable<Any, Any?> }){
//        val fieldSecurityAction = field.securityRuleWriteTypeless.invoke(untypedUser, field.get(currentState), field.get(copy))
//        @Suppress("UNCHECKED_CAST")
//        when(fieldSecurityAction.typeless()){
//            SecurityAction.Allow -> {  }
//            SecurityAction.Ignore -> { outModifications.removeAll { it.field == field } }
//            SecurityAction.Deny -> throw IllegalAccessException()
//            is SecurityAction.Obscure -> { outModifications.removeAll { it.field == field } }
//        }
//    }
//
//    return outModifications
//}