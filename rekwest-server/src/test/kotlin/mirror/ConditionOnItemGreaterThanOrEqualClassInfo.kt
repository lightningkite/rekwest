//Generated by Lightning Kite's Mirror plugin
//AUTOMATICALLY GENERATED AND WILL BE OVERRIDDEN IF THIS MESSAGE IS PRESENT
package com.lightningkite.mirror.archive


import com.lightningkite.mirror.info.*
import kotlin.reflect.KClass

@Suppress("RemoveExplicitTypeArguments", "UNCHECKED_CAST", "USELESS_CAST")
object ConditionOnItemGreaterThanOrEqualClassInfo: ClassInfo<ConditionOnItem.GreaterThanOrEqual<*,*>> {

   override val kClass: KClass<ConditionOnItem.GreaterThanOrEqual<*,*>> = ConditionOnItem.GreaterThanOrEqual::class
   override val modifiers: List<ClassInfo.Modifier> = listOf(ClassInfo.Modifier.Data)

   override val implements: List<Type<*>> = listOf()

   override val packageName: String = "com.lightningkite.mirror.archive"
   override val owner: KClass<*>? = ConditionOnItem::class
   override val ownerName: String? = "ConditionOnItem"

   override val name: String = "GreaterThanOrEqual"
   override val annotations: List<AnnotationInfo> = listOf()
   override val enumValues: List<ConditionOnItem.GreaterThanOrEqual<*,*>>? = null

   object Fields {
       val field = FieldInfo<ConditionOnItem.GreaterThanOrEqual<*,*>, com.lightningkite.mirror.info.FieldInfo<kotlin.Any, kotlin.Comparable<kotlin.Comparable<*>>>>(ConditionOnItemGreaterThanOrEqualClassInfo, "field", Type<com.lightningkite.mirror.info.FieldInfo<kotlin.Any, kotlin.Comparable<kotlin.Comparable<*>>>>(com.lightningkite.mirror.info.FieldInfo::class, listOf(TypeProjection(Type<kotlin.Any>(kotlin.Any::class, listOf(), false), TypeProjection.Variance.INVARIANT), TypeProjection(Type<kotlin.Comparable<kotlin.Comparable<*>>>(kotlin.Comparable::class, listOf(), false), TypeProjection.Variance.INVARIANT)), false), false, { it.field as com.lightningkite.mirror.info.FieldInfo<kotlin.Any, kotlin.Comparable<kotlin.Comparable<*>>>}, listOf())
        val value = FieldInfo<ConditionOnItem.GreaterThanOrEqual<*,*>, kotlin.Comparable<kotlin.Comparable<*>>>(ConditionOnItemGreaterThanOrEqualClassInfo, "value", Type<kotlin.Comparable<kotlin.Comparable<*>>>(kotlin.Comparable::class, listOf(), false), false, { it.value as kotlin.Comparable<kotlin.Comparable<*>>}, listOf())
   }

   override val fields:List<FieldInfo<ConditionOnItem.GreaterThanOrEqual<*,*>, *>> = listOf(Fields.field, Fields.value)

   override fun construct(map: Map<String, Any?>): ConditionOnItem.GreaterThanOrEqual<kotlin.Any, kotlin.Comparable<kotlin.Comparable<*>>> {
       //Gather variables
       val field:com.lightningkite.mirror.info.FieldInfo<kotlin.Any, kotlin.Comparable<kotlin.Comparable<*>>> = map["field"] as com.lightningkite.mirror.info.FieldInfo<kotlin.Any, kotlin.Comparable<kotlin.Comparable<*>>>
        val value:kotlin.Comparable<kotlin.Comparable<*>> = map["value"] as kotlin.Comparable<kotlin.Comparable<*>>
           //Handle the optionals
       
       //Finally do the call
       return ConditionOnItem.GreaterThanOrEqual<kotlin.Any, kotlin.Comparable<kotlin.Comparable<*>>>(
           field = field,
            value = value
       )
   }

}