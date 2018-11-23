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
object UserRoleClassInfo: ClassInfo<User.Role> {

   override val kClass: KClass<User.Role> = User.Role::class
   override val modifiers: List<ClassInfo.Modifier> = listOf()

   override val implements: List<Type<*>> = listOf()

   override val packageName: String = "com.lightningkite.rekwest.server"
   override val owner: KClass<*>? = User::class
   override val ownerName: String? = "User"

   override val name: String = "Role"
   override val annotations: List<AnnotationInfo> = listOf()
   override val enumValues: List<User.Role>? = listOf(User.Role.Admin, User.Role.Citizen)

   object Fields {
       
   }

   override val fields:List<FieldInfo<User.Role, *>> = listOf()

   override fun construct(map: Map<String, Any?>): User.Role = throw NotImplementedError()

}