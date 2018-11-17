//Generated by Lightning Kite's Mirror plugin
package com.lightningkite.mirror.archive


import com.lightningkite.mirror.info.*
import kotlin.reflect.KClass

@Suppress("RemoveExplicitTypeArguments", "UNCHECKED_CAST", "USELESS_CAST")
object ConditionOnItemAndClassInfo: ClassInfo<ConditionOnItem.And<*>> {

   override val kClass: KClass<ConditionOnItem.And<*>> = ConditionOnItem.And::class
   override val modifiers: List<ClassInfo.Modifier> = listOf(ClassInfo.Modifier.Data)

   override val implements: List<Type<*>> = listOf()

   override val packageName: String = "com.lightningkite.mirror.archive"
   override val owner: KClass<*>? = ConditionOnItem::class
   override val ownerName: String? = "ConditionOnItem"

   override val name: String = "And"
   override val annotations: List<AnnotationInfo> = listOf()
   override val enumValues: List<ConditionOnItem.And<*>>? = null

   object Fields {
       val conditions = SerializedFieldInfo<ConditionOnItem.And<*>, kotlin.collections.List<com.lightningkite.mirror.archive.ConditionOnItem<kotlin.Any>>>(ConditionOnItemAndClassInfo, "conditions", Type<kotlin.collections.List<com.lightningkite.mirror.archive.ConditionOnItem<kotlin.Any>>>(kotlin.collections.List::class, listOf(TypeProjection(Type<com.lightningkite.mirror.archive.ConditionOnItem<kotlin.Any>>(com.lightningkite.mirror.archive.ConditionOnItem::class, listOf(TypeProjection(Type<kotlin.Any>(kotlin.Any::class, listOf(), false), TypeProjection.Variance.INVARIANT)), false), TypeProjection.Variance.INVARIANT)), false), false, { it.conditions as kotlin.collections.List<com.lightningkite.mirror.archive.ConditionOnItem<kotlin.Any>>}, listOf())
   }

   override val fields:List<SerializedFieldInfo<ConditionOnItem.And<*>, *>> = listOf(Fields.conditions)

   override fun construct(map: Map<String, Any?>): ConditionOnItem.And<kotlin.Any> {
       //Gather variables
       val conditions:kotlin.collections.List<com.lightningkite.mirror.archive.ConditionOnItem<kotlin.Any>> = map["conditions"] as kotlin.collections.List<com.lightningkite.mirror.archive.ConditionOnItem<kotlin.Any>>
           //Handle the optionals
       
       //Finally do the call
       return ConditionOnItem.And<kotlin.Any>(
           conditions = conditions
       )
   }

}