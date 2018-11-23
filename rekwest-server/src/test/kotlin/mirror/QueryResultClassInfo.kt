//Generated by Lightning Kite's Mirror plugin
//AUTOMATICALLY GENERATED AND WILL BE OVERRIDDEN IF THIS MESSAGE IS PRESENT
package com.lightningkite.mirror.archive


import com.lightningkite.mirror.info.*
import kotlin.reflect.KClass

@Suppress("RemoveExplicitTypeArguments", "UNCHECKED_CAST", "USELESS_CAST")
object QueryResultClassInfo: ClassInfo<QueryResult<*>> {

   override val kClass: KClass<QueryResult<*>> = QueryResult::class
   override val modifiers: List<ClassInfo.Modifier> = listOf(ClassInfo.Modifier.Data)

   override val implements: List<Type<*>> = listOf()

   override val packageName: String = "com.lightningkite.mirror.archive"
   override val owner: KClass<*>? = null
   override val ownerName: String? = null

   override val name: String = "QueryResult"
   override val annotations: List<AnnotationInfo> = listOf()
   override val enumValues: List<QueryResult<*>>? = null

   object Fields {
       val results = FieldInfo<QueryResult<*>, kotlin.collections.List<Any?>>(QueryResultClassInfo, "results", Type<kotlin.collections.List<Any?>>(kotlin.collections.List::class, listOf(TypeProjection(Type<Any?>(Any::class, listOf(), false), TypeProjection.Variance.INVARIANT)), false), false, { it.results as kotlin.collections.List<Any?>}, listOf())
        val continuationToken = FieldInfo<QueryResult<*>, kotlin.String?>(QueryResultClassInfo, "continuationToken", Type<kotlin.String?>(kotlin.String::class, listOf(), true), true, { it.continuationToken as kotlin.String?}, listOf())
   }

   override val fields:List<FieldInfo<QueryResult<*>, *>> = listOf(Fields.results, Fields.continuationToken)

   override fun construct(map: Map<String, Any?>): QueryResult<Any?> {
       //Gather variables
       val results:kotlin.collections.List<Any?> = map["results"] as kotlin.collections.List<Any?>
           //Handle the optionals
       val continuationToken:kotlin.String? = map["continuationToken"] as kotlin.String?
       //Finally do the call
       return QueryResult<Any?>(
           results = results,
            continuationToken = continuationToken
       )
   }

}