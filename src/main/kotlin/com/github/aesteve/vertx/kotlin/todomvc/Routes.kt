package com.github.aesteve.vertx.kotlin.todomvc

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.aesteve.vertx.kotlin.todomvc.domain.Todo
import com.github.aesteve.vertx.kotlin.todomvc.service.allTodos
import com.github.aesteve.vertx.kotlin.todomvc.service.createTodo
import com.github.aesteve.vertx.kotlin.todomvc.service.removeAll
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

val clear = { ctx: RoutingContext ->
    ctx["payload"] = removeAll()
    ctx
}

val cors = CorsHandler.create("*")
        .allowedHeader(CONTENT_TYPE)
        .allowedMethods(setOf(GET, POST, PUT, DELETE, OPTIONS))


fun createRouter(vertx: Vertx): Router = Router.router(vertx).apply {
    route().handler(cors)
    route().handler(BodyHandler.create())
    get("/").handler(list)
    post("/").handler(create then sendPayload)
    delete("/").handler(clear then sendPayload)
}
