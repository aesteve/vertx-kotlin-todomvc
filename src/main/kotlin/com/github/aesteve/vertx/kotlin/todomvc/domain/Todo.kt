package com.github.aesteve.vertx.kotlin.todomvc.domain

data class Todo(
    var id: String?,
    val title: String,
    var order: Int?,
    val complete: Boolean = false
)
