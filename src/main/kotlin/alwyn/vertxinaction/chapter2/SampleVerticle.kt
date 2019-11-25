package alwyn.vertxinaction.chapter2

import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import org.slf4j.LoggerFactory

class SampleVerticle : AbstractVerticle() {
    private val logger = LoggerFactory.getLogger(SampleVerticle::class.java)

    override fun start() {
        logger.info("n = ${config().getInteger("n", -1)}")
    }
}

fun main() {
    val vertx = Vertx.vertx()
    for (i in 1..4) {
        val conf = JsonObject().put("n", i)
        val opts = DeploymentOptions()
            .setConfig(conf)
            .setInstances(i)
        vertx.deployVerticle("alwyn.vertxinaction.chapter2.SampleVerticle", opts)
    }
}
