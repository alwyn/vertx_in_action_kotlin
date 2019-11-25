package alwyn.vertxinaction.chapter3

import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import io.vertx.core.json.JsonObject
import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("SecondInstance")
    Vertx.clusteredVertx(VertxOptions()) { ar ->
        if (ar.succeeded()) {
            logger.info("Second instance has been started")
            val vertx = ar.result()
            vertx.deployVerticle("alwyn.vertxinaction.chapter3.HeatSensor", DeploymentOptions().setInstances(4))
            vertx.deployVerticle(Listener())
            vertx.deployVerticle(SensorData())
            val conf: JsonObject = JsonObject().put("port", 8081)
            vertx.deployVerticle("alwyn.vertxinaction.chapter3.HttpServer", DeploymentOptions().setConfig(conf))
        } else {
            logger.error("Could not start", ar.cause())
        }
    }
}
