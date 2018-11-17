//Generated by Lightning Kite's Mirror plugin
package com.lightningkite.rekwest.server

import com.lightningkite.kommon.exception.ExceptionNames
import com.lightningkite.lokalize.TimeStamp
import com.lightningkite.mirror.archive.*
import com.lightningkite.mirror.info.Indexed
import com.lightningkite.mirror.info.Mutates
import com.lightningkite.mirror.info.ThrowsTypes
import com.lightningkite.rekwest.ServerFunction
import com.lightningkite.mirror.info.*
import kotlin.reflect.KClass

@Suppress("RemoveExplicitTypeArguments", "UNCHECKED_CAST", "USELESS_CAST")
object UserResetPasswordClassInfo: ClassInfo<User.ResetPassword> {

   override val kClass: KClass<User.ResetPassword> = User.ResetPassword::class
   override val modifiers: List<ClassInfo.Modifier> = listOf()

   override val implements: List<Type<*>> = listOf(Type<ServerFunction<Unit>>(ServerFunction::class, listOf(TypeProjection(Type<Unit>(Unit::class, listOf(), false), TypeProjection.Variance.INVARIANT)), false))

   override val packageName: String = "com.lightningkite.rekwest.server"
   override val owner: KClass<*>? = User::class
   override val ownerName: String? = "User"

   override val name: String = "ResetPassword"
   override val annotations: List<AnnotationInfo> = listOf(AnnotationInfo("ThrowsTypes", listOf(ExceptionNames.ForbiddenException, ExceptionNames.NoSuchElementException)))
   override val enumValues: List<User.ResetPassword>? = null

   object Fields {
       val email = SerializedFieldInfo<User.ResetPassword, String>(UserResetPasswordClassInfo, "email", Type<String>(String::class, listOf(), false), false, { it.email as String}, listOf())
   }

   override val fields:List<SerializedFieldInfo<User.ResetPassword, *>> = listOf(Fields.email)

   override fun construct(map: Map<String, Any?>): User.ResetPassword {
       //Gather variables
       val email:String = map["email"] as String
           //Handle the optionals
       
       //Finally do the call
       return User.ResetPassword(
           email = email
       )
   }

}