//Generated by Lightning Kite's Mirror plugin
//AUTOMATICALLY GENERATED AND WILL BE OVERRIDDEN IF THIS MESSAGE IS PRESENT
package com.lightningkite.mirror.archive

import com.lightningkite.mirror.info.*
import kotlin.reflect.KClass

@Suppress("RemoveExplicitTypeArguments", "UNCHECKED_CAST", "USELESS_CAST")
object ModificationOnItemSetClassInfo: ClassInfo<ModificationOnItem.Set<*,*>> {

   override val kClass: KClass<ModificationOnItem.Set<*,*>> = ModificationOnItem.Set::class
   override val modifiers: List<ClassInfo.Modifier> = listOf(ClassInfo.Modifier.Data)

   override val implements: List<Type<*>> = listOf()

   override val packageName: String = "com.lightningkite.mirror.archive"
   override val owner: KClass<*>? = ModificationOnItem::class
   override val ownerName: String? = "ModificationOnItem"

   override val name: String = "Set"
   override val annotations: List<AnnotationInfo> = listOf()
   override val enumValues: List<ModificationOnItem.Set<*,*>>? = null

   val fieldField = FieldInfo<ModificationOnItem.Set<*,*>, com.lightningkite.mirror.info.FieldInfo<kotlin.Any, Any?>>(this, "field", Type<com.lightningkite.mirror.info.FieldInfo<kotlin.Any, Any?>>(com.lightningkite.mirror.info.FieldInfo::class, listOf(TypeProjection(Type<kotlin.Any>(kotlin.Any::class, listOf(), false), TypeProjection.Variance.INVARIANT), TypeProjection(Type<Any?>(Any::class, listOf(), false), TypeProjection.Variance.INVARIANT)), false), false, { it.field as com.lightningkite.mirror.info.FieldInfo<kotlin.Any, Any?>}, listOf())
    val fieldValue = FieldInfo<ModificationOnItem.Set<*,*>, Any?>(this, "value", Type<Any?>(Any::class, listOf(), false), false, { it.value as Any?}, listOf())

   override val fields:List<FieldInfo<ModificationOnItem.Set<*,*>, *>> = listOf(fieldField, fieldValue)

   override fun construct(map: Map<String, Any?>): ModificationOnItem.Set<kotlin.Any, Any?> {
       //Gather variables
       val field:com.lightningkite.mirror.info.FieldInfo<kotlin.Any, Any?> = map["field"] as com.lightningkite.mirror.info.FieldInfo<kotlin.Any, Any?>
        val value:Any? = map["value"] as Any?
           //Handle the optionals
       
       //Finally do the call
       return ModificationOnItem.Set<kotlin.Any, Any?>(
           field = field,
            value = value
       )
   }

}