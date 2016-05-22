package com.github.aesteve.vertx.kotlin.todomvc

import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.CorsHandler

infix fun<T, V, R> Function1<T, V>.then(after: (V) -> R): (T) -> R {
    return { t: T -> after(this(t)) }
}

fun CorsHandler.allowedHeader(header: CharSequence) = allowedHeader(header.toString())

operator fun RoutingContext.set(key: String, value: Any) = this.put(key, value)

fun<T> MutableList<T>.updateWhere(finder: (T) -> Boolean, newValue: T): Int {
    val idx = indexOf(find(finder))
    if (idx > -1) set(idx, newValue)
    return idx
}