package com.lightningkite.rekwest

import com.lightningkite.kommon.collection.WeakHashMap
import com.lightningkite.mirror.info.ClassInfo
import com.lightningkite.mirror.info.Type
import com.lightningkite.mirror.info.allImplements
import kotlin.reflect.KClass

private val ServerFunctionReturnType = WeakHashMap<KClass<*>, Type<*>>()
@Suppress("UNCHECKED_CAST")
val <T> ServerFunction<T>.returnType: Type<T> get() = ServerFunctionReturnType.getOrPut(this::class){
    ClassInfo[this::class].allImplements.find { it.kClass == ServerFunction::class }!!.typeParameters.first().type
} as Type<T>

private val ServerFunctionThrows = WeakHashMap<KClass<*>, List<String>?>()
@Suppress("UNCHECKED_CAST")
val ServerFunction<*>.throwsTypes: List<String>? get() = ServerFunctionThrows.getOrPut(this::class){
    ClassInfo[this::class].annotations.find { it.name.endsWith("ThrowsTypes") }?.arguments as? List<String>
}