//Generated by Lightning Kite's Mirror plugin
//AUTOMATICALLY GENERATED AND WILL BE OVERRIDDEN IF THIS MESSAGE IS PRESENT
package com.lightningkite.mirror.info

import com.lightningkite.mirror.info.*
import kotlin.reflect.KClass

@Suppress("RemoveExplicitTypeArguments", "UNCHECKED_CAST", "USELESS_CAST")
object TypeProjectionVarianceClassInfo: ClassInfo<TypeProjection.Variance> {

   override val kClass: KClass<TypeProjection.Variance> = TypeProjection.Variance::class
   override val modifiers: List<ClassInfo.Modifier> = listOf()

   override val implements: List<Type<*>> = listOf()

   override val packageName: String = "com.lightningkite.mirror.info"
   override val owner: KClass<*>? = TypeProjection::class
   override val ownerName: String? = "TypeProjection"

   override val name: String = "Variance"
   override val annotations: List<AnnotationInfo> = listOf()
   override val enumValues: List<TypeProjection.Variance>? = listOf(TypeProjection.Variance.INVARIANT, TypeProjection.Variance.IN, TypeProjection.Variance.OUT, TypeProjection.Variance.STAR)

   

   override val fields:List<FieldInfo<TypeProjection.Variance, *>> = listOf()

   override fun construct(map: Map<String, Any?>): TypeProjection.Variance = throw NotImplementedError()

}