package com.github.aesteve.vertx.kotlin.todomvc.domain

import com.github.aesteve.vertx.kotlin.todomvc.HOST
import com.github.aesteve.vertx.kotlin.todomvc.PORT

data class Todo(
    var id: String?,
    val title: String?,
    var order: Int?,
    val completed: Boolean = false
) {
    var url : String = ""
    get() { return "http://$HOST:$PORT/todos/${id}" }
}
