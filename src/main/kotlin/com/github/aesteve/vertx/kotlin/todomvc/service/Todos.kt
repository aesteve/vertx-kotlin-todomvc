package com.github.aesteve.vertx.kotlin.todomvc.service

import com.github.aesteve.vertx.kotlin.todomvc.domain.Todo
import com.github.aesteve.vertx.kotlin.todomvc.updateWhere

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

fun getTodoById(id: String): Todo? {
    return todos.find { it.id == id }
}

fun updateTodo(id: String, update: Todo): Todo {
    val todo = Todo(id = id, completed = update.completed, order = update.order, title = update.title)
    todos.updateWhere({it.id == id}, todo)
    return todo
}

fun delete(id: String): List<Todo> {
    todos.retainAll { it.id != id }
    return todos
}