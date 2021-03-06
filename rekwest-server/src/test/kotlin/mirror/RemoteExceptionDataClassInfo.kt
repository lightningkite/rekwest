//Generated by Lightning Kite's Mirror plugin
//AUTOMATICALLY GENERATED AND WILL BE OVERRIDDEN IF THIS MESSAGE IS PRESENT
package com.lightningkite.rekwest

import com.lightningkite.mirror.info.*
import kotlin.reflect.KClass

@Suppress("RemoveExplicitTypeArguments", "UNCHECKED_CAST", "USELESS_CAST")
object RemoteExceptionDataClassInfo: ClassInfo<RemoteExceptionData> {

   override val kClass: KClass<RemoteExceptionData> = RemoteExceptionData::class
   override val modifiers: List<ClassInfo.Modifier> = listOf()

   override val implements: List<Type<*>> = listOf()

   override val packageName: String = "com.lightningkite.rekwest"
   override val owner: KClass<*>? = null
   override val ownerName: String? = null

   override val name: String = "RemoteExceptionData"
   override val annotations: List<AnnotationInfo> = listOf()
   override val enumValues: List<RemoteExceptionData>? = null

   val fieldType = FieldInfo<RemoteExceptionData, kotlin.String>(this, "type", Type<kotlin.String>(kotlin.String::class, listOf(), false), true, { it.type as kotlin.String}, listOf())
    val fieldMessage = FieldInfo<RemoteExceptionData, kotlin.String>(this, "message", Type<kotlin.String>(kotlin.String::class, listOf(), false), true, { it.message as kotlin.String}, listOf())
    val fieldTrace = FieldInfo<RemoteExceptionData, kotlin.String>(this, "trace", Type<kotlin.String>(kotlin.String::class, listOf(), false), true, { it.trace as kotlin.String}, listOf())
    val fieldData = FieldInfo<RemoteExceptionData, kotlin.Any?>(this, "data", Type<kotlin.Any?>(kotlin.Any::class, listOf(), true), true, { it.data as kotlin.Any?}, listOf())

   override val fields:List<FieldInfo<RemoteExceptionData, *>> = listOf(fieldType, fieldMessage, fieldTrace, fieldData)

   override fun construct(map: Map<String, Any?>): RemoteExceptionData {
       //Gather variables
       
           //Handle the optionals
       val type:kotlin.String = map["type"] as? kotlin.String ?: (fieldType.get(RemoteExceptionData()) as kotlin.String)
        val message:kotlin.String = map["message"] as? kotlin.String ?: (fieldMessage.get(RemoteExceptionData(type = type)) as kotlin.String)
        val trace:kotlin.String = map["trace"] as? kotlin.String ?: (fieldTrace.get(RemoteExceptionData(type = type, message = message)) as kotlin.String)
        val data:kotlin.Any? = map["data"] as kotlin.Any?
       //Finally do the call
       return RemoteExceptionData(
           type = type,
            message = message,
            trace = trace,
            data = data
       )
   }

}