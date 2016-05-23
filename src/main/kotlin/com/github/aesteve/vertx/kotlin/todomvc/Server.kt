package com.github.aesteve.vertx.kotlin.todomvc

import com.github.aesteve.vertx.kotlin.todomvc.env.HOST
import com.github.aesteve.vertx.kotlin.todomvc.env.PORT
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerOptions
import io.vertx.ext.web.Router


class Server : AbstractVerticle() {

    var server: HttpServer? = null
    val router: Router by lazy {
        createRouter(vertx)
    }

    override fun start(future: Future<Void>) {
        server = vertx.createHttpServer(HttpServerOptions().setPort(PORT).setHost(HOST))
        server?.requestHandler { router.accept(it) } // no method reference :( https://github.com/Kotlin/KEEP/issues/5
        server?.listen() { result ->
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