package com.github.aesteve.vertx.kotlin.todomvc

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router

class Server : AbstractVerticle() {

    var server: HttpServer? = null
    val router: Router by lazy {
        createRouter(vertx)
    }

    override fun start(future: Future<Void>) {
        server = vertx.createHttpServer()
        server?.requestHandler { router.accept(it) } // no method reference :( https://github.com/Kotlin/KEEP/issues/5
        server?.listen(8080) { result ->
            if (result.succeeded()) {
                future.complete()
            } else {
                future.fail(result.cause())
            }
        }
    }

    override fun stop(future: Future<Void>) = when(server) {
        null -> future.complete()
        else -> server?.close(future.completer())
    }!!

}