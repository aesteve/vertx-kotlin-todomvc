package com.github.aesteve.vertx.kotlin.todomvc.service

import com.github.aesteve.vertx.kotlin.todomvc.domain.Todo

private var todos: MutableList<Todo> = arrayListOf()

val allTodos: List<Todo> = todos

fun createTodo(todo: Todo): List<Todo> {
    todos.add(todo)
    return todos
}

fun removeAll(): List<Todo> {
    todos.clear()
    return todos
}