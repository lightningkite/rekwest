//Generated by Lightning Kite's Mirror plugin
//AUTOMATICALLY GENERATED AND WILL BE OVERRIDDEN IF THIS MESSAGE IS PRESENT
package com.lightningkite.rekwest.server

import com.lightningkite.kommon.exception.ExceptionNames
import com.lightningkite.lokalize.TimeStamp
import com.lightningkite.mirror.info.Indexed
import com.lightningkite.mirror.info.Mutates
import com.lightningkite.mirror.info.ThrowsTypes
import com.lightningkite.rekwest.ServerFunction
import com.lightningkite.mirror.archive.*
import com.lightningkite.mirror.info.*
import kotlin.reflect.KClass

@Suppress("RemoveExplicitTypeArguments", "UNCHECKED_CAST", "USELESS_CAST")
object UserLoginClassInfo: ClassInfo<User.Login> {

   override val kClass: KClass<User.Login> = User.Login::class
   override val modifiers: List<ClassInfo.Modifier> = listOf()

   override val implements: List<Type<*>> = listOf(Type<ServerFunction<User.Session>>(ServerFunction::class, listOf(TypeProjection(Type<User.Session>(User.Session::class, listOf(), false), TypeProjection.Variance.INVARIANT)), false))

   override val packageName: String = "com.lightningkite.rekwest.server"
   override val owner: KClass<*>? = User::class
   override val ownerName: String? = "User"

   override val name: String = "Login"
   override val annotations: List<AnnotationInfo> = listOf(AnnotationInfo("@ThrowsTypes", listOf(ExceptionNames.ForbiddenException, ExceptionNames.NoSuchElementException)))
   override val enumValues: List<User.Login>? = null

   val fieldEmail = FieldInfo<User.Login, String>(this, "email", Type<String>(String::class, listOf(), false), false, { it.email as String}, listOf())
    val fieldPassword = FieldInfo<User.Login, String>(this, "password", Type<String>(String::class, listOf(), false), false, { it.password as String}, listOf())

   override val fields:List<FieldInfo<User.Login, *>> = listOf(fieldEmail, fieldPassword)

   override fun construct(map: Map<String, Any?>): User.Login {
       //Gather variables
       val email:String = map["email"] as String
        val password:String = map["password"] as String
           //Handle the optionals
       
       //Finally do the call
       return User.Login(
           email = email,
            password = password
       )
   }

}