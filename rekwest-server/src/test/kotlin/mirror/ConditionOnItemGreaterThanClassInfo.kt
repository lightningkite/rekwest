//Generated by Lightning Kite's Mirror plugin
//AUTOMATICALLY GENERATED AND WILL BE OVERRIDDEN IF THIS MESSAGE IS PRESENT
package com.lightningkite.mirror.archive


import com.lightningkite.mirror.info.*
import kotlin.reflect.KClass

@Suppress("RemoveExplicitTypeArguments", "UNCHECKED_CAST", "USELESS_CAST")
object ConditionOnItemGreaterThanClassInfo: ClassInfo<ConditionOnItem.GreaterThan<*,*>> {

   override val kClass: KClass<ConditionOnItem.GreaterThan<*,*>> = ConditionOnItem.GreaterThan::class
   override val modifiers: List<ClassInfo.Modifier> = listOf(ClassInfo.Modifier.Data)

   override val implements: List<Type<*>> = listOf()

   override val packageName: String = "com.lightningkite.mirror.archive"
   override val owner: KClass<*>? = ConditionOnItem::class
   override val ownerName: String? = "ConditionOnItem"

   override val name: String = "GreaterThan"
   override val annotations: List<AnnotationInfo> = listOf()
   override val enumValues: List<ConditionOnItem.GreaterThan<*,*>>? = null

   object Fields {
       val field = FieldInfo<ConditionOnItem.GreaterThan<*,*>, com.lightningkite.mirror.info.FieldInfo<kotlin.Any, kotlin.Comparable<kotlin.Comparable<*>>>>(ConditionOnItemGreaterThanClassInfo, "field", Type<com.lightningkite.mirror.info.FieldInfo<kotlin.Any, kotlin.Comparable<kotlin.Comparable<*>>>>(com.lightningkite.mirror.info.FieldInfo::class, listOf(TypeProjection(Type<kotlin.Any>(kotlin.Any::class, listOf(), false), TypeProjection.Variance.INVARIANT), TypeProjection(Type<kotlin.Comparable<kotlin.Comparable<*>>>(kotlin.Comparable::class, listOf(), false), TypeProjection.Variance.INVARIANT)), false), false, { it.field as com.lightningkite.mirror.info.FieldInfo<kotlin.Any, kotlin.Comparable<kotlin.Comparable<*>>>}, listOf())
        val value = FieldInfo<ConditionOnItem.GreaterThan<*,*>, kotlin.Comparable<kotlin.Comparable<*>>>(ConditionOnItemGreaterThanClassInfo, "value", Type<kotlin.Comparable<kotlin.Comparable<*>>>(kotlin.Comparable::class, listOf(), false), false, { it.value as kotlin.Comparable<kotlin.Comparable<*>>}, listOf())
   }

   override val fields:List<FieldInfo<ConditionOnItem.GreaterThan<*,*>, *>> = listOf(Fields.field, Fields.value)

   override fun construct(map: Map<String, Any?>): ConditionOnItem.GreaterThan<kotlin.Any, kotlin.Comparable<kotlin.Comparable<*>>> {
       //Gather variables
       val field:com.lightningkite.mirror.info.FieldInfo<kotlin.Any, kotlin.Comparable<kotlin.Comparable<*>>> = map["field"] as com.lightningkite.mirror.info.FieldInfo<kotlin.Any, kotlin.Comparable<kotlin.Comparable<*>>>
        val value:kotlin.Comparable<kotlin.Comparable<*>> = map["value"] as kotlin.Comparable<kotlin.Comparable<*>>
           //Handle the optionals
       
       //Finally do the call
       return ConditionOnItem.GreaterThan<kotlin.Any, kotlin.Comparable<kotlin.Comparable<*>>>(
           field = field,
            value = value
       )
   }

}