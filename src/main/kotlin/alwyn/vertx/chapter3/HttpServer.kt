package alwyn.vertx.chapter3

import io.vertx.core.AbstractVerticle
import io.vertx.core.http.HttpServerRequest

class HttpServer: AbstractVerticle() {

    override fun start() {
        vertx.createHttpServer()
            .requestHandler(::handler)
            .listen(config().getInteger("port", 8080))
    }

    private fun handler(request: HttpServerRequest) {
        when (request.path()) {
            "/" -> request.response().sendFile("index.html")
            "/sse" -> sse.request()
            else -> request.response().statusCode = 404
        }
    }
}