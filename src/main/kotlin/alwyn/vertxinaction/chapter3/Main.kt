package alwyn.vertxinaction.chapter3

import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx

fun main(args: Array<String>) {
    val vertx = Vertx.vertx()
    vertx.deployVerticle("alwyn.vertxinaction.chapter3.HeatSensor", DeploymentOptions().setInstances(4))
    vertx.deployVerticle(Listener())
    vertx.deployVerticle(SensorData())
    vertx.deployVerticle(HttpServer())
}
