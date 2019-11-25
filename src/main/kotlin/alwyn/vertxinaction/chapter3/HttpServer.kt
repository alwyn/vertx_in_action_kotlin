package alwyn.vertxinaction.chapter3

import io.vertx.core.AbstractVerticle
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject

class HttpServer : AbstractVerticle() {

    override fun start() {
        vertx.createHttpServer()
            .requestHandler(::handler)
            .listen(config().getInteger("port", 8080))
    }

    private fun handler(request: HttpServerRequest) {
        when (request.path()) {
            "/" -> request.response().sendFile("index.html")
            "/sse" -> sse(request)
            else -> request.response().statusCode = 404
        }
    }

    private fun sse(request: HttpServerRequest) {
        val response = request.response()
        response
            .putHeader("Content-Type", "text/event-stream")
            .putHeader("Cache-Control", "no-cache")
            .isChunked = true

        val updateConsumer = vertx.eventBus().consumer<JsonObject>("sensor.updates")
        updateConsumer.handler { msg ->
            response.write("event: update\n")
            response.write("data: ${msg.body().encode()}\n\n")
        }

        val ticks = vertx.periodicStream(1000)
        ticks.handler {
            vertx.eventBus().request<JsonObject>("sensor.average", "") { reply ->
                if (reply.succeeded()) {
                    response.write("event: average\n")
                    response.write("data:${reply.result().body().encode()}\n\n")
                }
            }
        }

        response.endHandler {
            updateConsumer.unregister()
            ticks.cancel()
        }
    }
}
