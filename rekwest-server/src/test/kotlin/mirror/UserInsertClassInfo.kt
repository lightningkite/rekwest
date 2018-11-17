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
object UserInsertClassInfo: ClassInfo<User.Insert> {

   override val kClass: KClass<User.Insert> = User.Insert::class
   override val modifiers: List<ClassInfo.Modifier> = listOf()

   override val implements: List<Type<*>> = listOf(Type<ServerFunction<Session>>(ServerFunction::class, listOf(TypeProjection(Type<Session>(Session::class, listOf(), false), TypeProjection.Variance.INVARIANT)), false))

   override val packageName: String = "com.lightningkite.rekwest.server"
   override val owner: KClass<*>? = User::class
   override val ownerName: String? = "User"

   override val name: String = "Insert"
   override val annotations: List<AnnotationInfo> = listOf(AnnotationInfo("Mutates", listOf()), AnnotationInfo("ThrowsTypes", listOf(ExceptionNames.ForbiddenException)))
   override val enumValues: List<User.Insert>? = null

   object Fields {
       val value = SerializedFieldInfo<User.Insert, User>(UserInsertClassInfo, "value", Type<User>(User::class, listOf(), false), false, { it.value as User}, listOf())
   }

   override val fields:List<SerializedFieldInfo<User.Insert, *>> = listOf(Fields.value)

   override fun construct(map: Map<String, Any?>): User.Insert {
       //Gather variables
       val value:User = map["value"] as User
           //Handle the optionals
       
       //Finally do the call
       return User.Insert(
           value = value
       )
   }

}