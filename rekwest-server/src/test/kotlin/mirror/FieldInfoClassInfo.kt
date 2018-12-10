//Generated by Lightning Kite's Mirror plugin
//AUTOMATICALLY GENERATED AND WILL BE OVERRIDDEN IF THIS MESSAGE IS PRESENT
package com.lightningkite.mirror.info


import com.lightningkite.mirror.info.*
import kotlin.reflect.KClass

@Suppress("RemoveExplicitTypeArguments", "UNCHECKED_CAST", "USELESS_CAST")
object FieldInfoClassInfo: ClassInfo<FieldInfo<*,*>> {

   override val kClass: KClass<FieldInfo<*,*>> = FieldInfo::class
   override val modifiers: List<ClassInfo.Modifier> = listOf(ClassInfo.Modifier.Data)

   override val implements: List<Type<*>> = listOf()

   override val packageName: String = "com.lightningkite.mirror.info"
   override val owner: KClass<*>? = null
   override val ownerName: String? = null

   override val name: String = "FieldInfo"
   override val annotations: List<AnnotationInfo> = listOf()
   override val enumValues: List<FieldInfo<*,*>>? = null

   object Fields {
       val owner = FieldInfo<FieldInfo<*,*>, com.lightningkite.mirror.info.ClassInfo<kotlin.Any>>(FieldInfoClassInfo, "owner", Type<com.lightningkite.mirror.info.ClassInfo<kotlin.Any>>(com.lightningkite.mirror.info.ClassInfo::class, listOf(TypeProjection(Type<kotlin.Any>(kotlin.Any::class, listOf(), false), TypeProjection.Variance.INVARIANT)), false), false, { it.owner as com.lightningkite.mirror.info.ClassInfo<kotlin.Any>}, listOf())
        val name = FieldInfo<FieldInfo<*,*>, kotlin.String>(FieldInfoClassInfo, "name", Type<kotlin.String>(kotlin.String::class, listOf(), false), false, { it.name as kotlin.String}, listOf())
        val type = FieldInfo<FieldInfo<*,*>, com.lightningkite.mirror.info.Type<Any?>>(FieldInfoClassInfo, "type", Type<com.lightningkite.mirror.info.Type<Any?>>(com.lightningkite.mirror.info.Type::class, listOf(TypeProjection(Type<Any?>(Any::class, listOf(), false), TypeProjection.Variance.INVARIANT)), false), false, { it.type as com.lightningkite.mirror.info.Type<Any?>}, listOf())
        val isOptional = FieldInfo<FieldInfo<*,*>, kotlin.Boolean>(FieldInfoClassInfo, "isOptional", Type<kotlin.Boolean>(kotlin.Boolean::class, listOf(), false), false, { it.isOptional as kotlin.Boolean}, listOf())
        val get = FieldInfo<FieldInfo<*,*>, kotlin.Function1<kotlin.Any, Any?>>(FieldInfoClassInfo, "get", Type<kotlin.Function1<kotlin.Any, Any?>>(kotlin.Function1::class, listOf(TypeProjection(Type<kotlin.Any>(kotlin.Any::class, listOf(), false), TypeProjection.Variance.INVARIANT), TypeProjection(Type<Any?>(Any::class, listOf(), false), TypeProjection.Variance.INVARIANT)), false), false, { it.get as kotlin.Function1<kotlin.Any, Any?>}, listOf())
        val annotations = FieldInfo<FieldInfo<*,*>, kotlin.collections.List<com.lightningkite.mirror.info.AnnotationInfo>>(FieldInfoClassInfo, "annotations", Type<kotlin.collections.List<com.lightningkite.mirror.info.AnnotationInfo>>(kotlin.collections.List::class, listOf(TypeProjection(Type<com.lightningkite.mirror.info.AnnotationInfo>(com.lightningkite.mirror.info.AnnotationInfo::class, listOf(), false), TypeProjection.Variance.INVARIANT)), false), true, { it.annotations as kotlin.collections.List<com.lightningkite.mirror.info.AnnotationInfo>}, listOf())
   }

   override val fields:List<FieldInfo<FieldInfo<*,*>, *>> = listOf(Fields.owner, Fields.name, Fields.type, Fields.isOptional, Fields.get, Fields.annotations)

   override fun construct(map: Map<String, Any?>): FieldInfo<kotlin.Any, Any?> {
       //Gather variables
       val owner:com.lightningkite.mirror.info.ClassInfo<kotlin.Any> = map["owner"] as com.lightningkite.mirror.info.ClassInfo<kotlin.Any>
        val name:kotlin.String = map["name"] as kotlin.String
        val type:com.lightningkite.mirror.info.Type<Any?> = map["type"] as com.lightningkite.mirror.info.Type<Any?>
        val isOptional:kotlin.Boolean = map["isOptional"] as kotlin.Boolean
        val get:kotlin.Function1<kotlin.Any, Any?> = map["get"] as kotlin.Function1<kotlin.Any, Any?>
           //Handle the optionals
       val annotations:kotlin.collections.List<com.lightningkite.mirror.info.AnnotationInfo> = map["annotations"] as? kotlin.collections.List<com.lightningkite.mirror.info.AnnotationInfo> ?: (Fields.annotations.get(FieldInfo<kotlin.Any, Any?>(owner = owner, name = name, type = type, isOptional = isOptional, get = get)) as kotlin.collections.List<com.lightningkite.mirror.info.AnnotationInfo>)
       //Finally do the call
       return FieldInfo<kotlin.Any, Any?>(
           owner = owner,
            name = name,
            type = type,
            isOptional = isOptional,
            get = get,
            annotations = annotations
       )
   }

}