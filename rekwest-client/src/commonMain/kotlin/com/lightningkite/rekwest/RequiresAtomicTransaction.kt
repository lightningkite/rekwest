package com.lightningkite.rekwest

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequiresAtomicTransaction