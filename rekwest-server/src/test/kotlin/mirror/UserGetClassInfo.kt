//Generated by Lightning Kite's Mirror plugin
//AUTOMATICALLY GENERATED AND WILL BE OVERRIDDEN IF THIS MESSAGE IS PRESENT
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
object UserGetClassInfo: ClassInfo<User.Get> {

   override val kClass: KClass<User.Get> = User.Get::class
   override val modifiers: List<ClassInfo.Modifier> = listOf()

   override val implements: List<Type<*>> = listOf(Type<ServerFunction<User>>(ServerFunction::class, listOf(TypeProjection(Type<User>(User::class, listOf(), false), TypeProjection.Variance.INVARIANT)), false))

   override val packageName: String = "com.lightningkite.rekwest.server"
   override val owner: KClass<*>? = User::class
   override val ownerName: String? = "User"

   override val name: String = "Get"
   override val annotations: List<AnnotationInfo> = listOf(AnnotationInfo("ThrowsTypes", listOf(ExceptionNames.NoSuchElementException)))
   override val enumValues: List<User.Get>? = null

   object Fields {
       val id = FieldInfo<User.Get, Reference<User, Long>>(UserGetClassInfo, "id", Type<Reference<User, Long>>(Reference::class, listOf(TypeProjection(Type<User>(User::class, listOf(), false), TypeProjection.Variance.INVARIANT), TypeProjection(Type<Long>(Long::class, listOf(), false), TypeProjection.Variance.INVARIANT)), false), false, { it.id as Reference<User, Long>}, listOf())
   }

   override val fields:List<FieldInfo<User.Get, *>> = listOf(Fields.id)

   override fun construct(map: Map<String, Any?>): User.Get {
       //Gather variables
       val id:Reference<User, Long> = map["id"] as Reference<User, Long>
           //Handle the optionals
       
       //Finally do the call
       return User.Get(
           id = id
       )
   }

}