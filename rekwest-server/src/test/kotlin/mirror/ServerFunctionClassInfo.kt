//Generated by Lightning Kite's Mirror plugin
//AUTOMATICALLY GENERATED AND WILL BE OVERRIDDEN IF THIS MESSAGE IS PRESENT
package com.lightningkite.rekwest


import com.lightningkite.mirror.info.*
import kotlin.reflect.KClass

@Suppress("RemoveExplicitTypeArguments", "UNCHECKED_CAST", "USELESS_CAST")
object ServerFunctionClassInfo: ClassInfo<ServerFunction<*>> {

   override val kClass: KClass<ServerFunction<*>> = ServerFunction::class
   override val modifiers: List<ClassInfo.Modifier> = listOf(ClassInfo.Modifier.Interface, ClassInfo.Modifier.Abstract)

   override val implements: List<Type<*>> = listOf()

   override val packageName: String = "com.lightningkite.rekwest"
   override val owner: KClass<*>? = null
   override val ownerName: String? = null

   override val name: String = "ServerFunction"
   override val annotations: List<AnnotationInfo> = listOf()
   override val enumValues: List<ServerFunction<*>>? = null

   object Fields {
       
   }

   override val fields:List<FieldInfo<ServerFunction<*>, *>> = listOf()

   override fun construct(map: Map<String, Any?>): ServerFunction<Any?> = throw NotImplementedError()

}