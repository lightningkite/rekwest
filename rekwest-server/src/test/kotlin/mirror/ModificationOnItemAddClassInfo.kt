//Generated by Lightning Kite's Mirror plugin
//AUTOMATICALLY GENERATED AND WILL BE OVERRIDDEN IF THIS MESSAGE IS PRESENT
package com.lightningkite.mirror.archive


import com.lightningkite.mirror.info.*
import kotlin.reflect.KClass

@Suppress("RemoveExplicitTypeArguments", "UNCHECKED_CAST", "USELESS_CAST")
object ModificationOnItemAddClassInfo: ClassInfo<ModificationOnItem.Add<*,*>> {

   override val kClass: KClass<ModificationOnItem.Add<*,*>> = ModificationOnItem.Add::class
   override val modifiers: List<ClassInfo.Modifier> = listOf(ClassInfo.Modifier.Data)

   override val implements: List<Type<*>> = listOf()

   override val packageName: String = "com.lightningkite.mirror.archive"
   override val owner: KClass<*>? = ModificationOnItem::class
   override val ownerName: String? = "ModificationOnItem"

   override val name: String = "Add"
   override val annotations: List<AnnotationInfo> = listOf()
   override val enumValues: List<ModificationOnItem.Add<*,*>>? = null

   object Fields {
       val field = FieldInfo<ModificationOnItem.Add<*,*>, com.lightningkite.mirror.info.FieldInfo<kotlin.Any, kotlin.Number>>(ModificationOnItemAddClassInfo, "field", Type<com.lightningkite.mirror.info.FieldInfo<kotlin.Any, kotlin.Number>>(com.lightningkite.mirror.info.FieldInfo::class, listOf(TypeProjection(Type<kotlin.Any>(kotlin.Any::class, listOf(), false), TypeProjection.Variance.INVARIANT), TypeProjection(Type<kotlin.Number>(kotlin.Number::class, listOf(), false), TypeProjection.Variance.INVARIANT)), false), false, { it.field as com.lightningkite.mirror.info.FieldInfo<kotlin.Any, kotlin.Number>}, listOf())
        val amount = FieldInfo<ModificationOnItem.Add<*,*>, kotlin.Number>(ModificationOnItemAddClassInfo, "amount", Type<kotlin.Number>(kotlin.Number::class, listOf(), false), false, { it.amount as kotlin.Number}, listOf())
   }

   override val fields:List<FieldInfo<ModificationOnItem.Add<*,*>, *>> = listOf(Fields.field, Fields.amount)

   override fun construct(map: Map<String, Any?>): ModificationOnItem.Add<kotlin.Any, kotlin.Number> {
       //Gather variables
       val field:com.lightningkite.mirror.info.FieldInfo<kotlin.Any, kotlin.Number> = map["field"] as com.lightningkite.mirror.info.FieldInfo<kotlin.Any, kotlin.Number>
        val amount:kotlin.Number = map["amount"] as kotlin.Number
           //Handle the optionals
       
       //Finally do the call
       return ModificationOnItem.Add<kotlin.Any, kotlin.Number>(
           field = field,
            amount = amount
       )
   }

}