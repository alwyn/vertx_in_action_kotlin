package alwyn.vertxinaction.chapter3

import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("FirstInstance")
    Vertx.clusteredVertx(VertxOptions()) { ar ->
        if (ar.succeeded()) {
            logger.info("First instance has been started")
            val vertx = ar.result()
            vertx.deployVerticle("alwyn.vertxinaction.chapter3.HeatSensor", DeploymentOptions().setInstances(4))
            vertx.deployVerticle(HttpServer())
        } else {
            logger.error("Could not start", ar.cause())
        }
    }
}
