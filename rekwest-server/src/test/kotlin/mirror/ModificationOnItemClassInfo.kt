//Generated by Lightning Kite's Mirror plugin
package com.lightningkite.mirror.archive


import com.lightningkite.mirror.info.*
import kotlin.reflect.KClass

@Suppress("RemoveExplicitTypeArguments", "UNCHECKED_CAST", "USELESS_CAST")
object ModificationOnItemClassInfo: ClassInfo<ModificationOnItem<*,*>> {

   override val kClass: KClass<ModificationOnItem<*,*>> = ModificationOnItem::class
   override val modifiers: List<ClassInfo.Modifier> = listOf(ClassInfo.Modifier.Sealed)

   override val implements: List<Type<*>> = listOf()

   override val packageName: String = "com.lightningkite.mirror.archive"
   override val owner: KClass<*>? = null
   override val ownerName: String? = null

   override val name: String = "ModificationOnItem"
   override val annotations: List<AnnotationInfo> = listOf()
   override val enumValues: List<ModificationOnItem<*,*>>? = null

   object Fields {
       
   }

   override val fields:List<SerializedFieldInfo<ModificationOnItem<*,*>, *>> = listOf()

   override fun construct(map: Map<String, Any?>): ModificationOnItem<kotlin.Any,Any?> = throw NotImplementedError()

}