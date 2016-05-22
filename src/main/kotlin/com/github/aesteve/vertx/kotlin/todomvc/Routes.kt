package com.github.aesteve.vertx.kotlin.todomvc

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.aesteve.vertx.kotlin.todomvc.domain.Todo
import com.github.aesteve.vertx.kotlin.todomvc.service.*
import io.vertx.core.Vertx
import io.vertx.core.http.HttpHeaders.CONTENT_TYPE
import io.vertx.core.http.HttpMethod.*
import io.vertx.core.json.Json
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.CorsHandler
import java.util.UUID.randomUUID

val mapper = jacksonObjectMapper()

fun RoutingContext.toJson(obj: Any) {
    response()
            .putHeader(CONTENT_TYPE, "application/json; charset=utf-8")
            .end(Json.encodePrettily(obj))
}

val sendPayload = { ctx: RoutingContext ->
    ctx.toJson(ctx["payload"])
}

fun readPayload(ctx: RoutingContext): Todo {
    return mapper.readValue(ctx.bodyAsString)
}

val list = { ctx: RoutingContext ->
    ctx.toJson(allTodos)
}

val create = { ctx: RoutingContext ->
    val todo: Todo = mapper.readValue(ctx.bodyAsString)
    createTodo(todo)
    todo.id = randomUUID().toString()
    ctx["payload"] = todo
    ctx
}

val getFromUrl = { ctx: RoutingContext ->
    val id = ctx.request().getParam("id")
    val todo = getTodoById(id)
    if (todo == null) {
        ctx.fail(404)
    } else {
        ctx["payload"] = todo
        ctx.next()
    }
}

val updateFromPayload = { ctx: RoutingContext ->
    val todo: Todo = ctx["payload"]
    val newTodo = readPayload(ctx)
    ctx["payload"] = updateTodo(todo.id!!, newTodo)
    ctx
}

val removeById = {ctx: RoutingContext ->
    val todo: Todo = ctx["payload"]
    ctx["payload"] = delete(todo.id!!)
    ctx
}

val clear = { ctx: RoutingContext ->
    ctx["payload"] = removeAll()
    ctx
}

val cors = CorsHandler.create("*")
        .allowedHeader(CONTENT_TYPE)
        .allowedMethods(setOf(GET, POST, PUT, PATCH, DELETE, OPTIONS))


fun createRouter(vertx: Vertx): Router = Router.router(vertx).apply {
    route().handler(cors)
    route().handler(BodyHandler.create())

    get("/").handler(list)
    post("/").handler(create then sendPayload)
    delete("/").handler(clear then sendPayload)

    get("/todos/:id").handler(getFromUrl)
    get("/todos/:id").handler(sendPayload)
    patch("/todos/:id").handler(getFromUrl)
    patch("/todos/:id").handler(updateFromPayload then sendPayload)
    delete("/todos/:id").handler(getFromUrl)
    delete("/todos/:id").handler(removeById then sendPayload)
}
