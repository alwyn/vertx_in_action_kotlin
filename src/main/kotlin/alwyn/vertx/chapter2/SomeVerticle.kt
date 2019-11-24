package alwyn.vertx.chapter2

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future

class SomeVerticle : AbstractVerticle() {

    override fun start(startFuture: Future<Void>) {
        vertx.createHttpServer()
            .requestHandler { req ->
                req.response().end("Ok")
            }
            .listen(8080) { ar ->
                if (ar.succeeded()) {
                    startFuture.complete()
                } else {
                    startFuture.fail(ar.cause())
                }
            }
    }
}