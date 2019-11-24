package alwyn.vertx.chapter2

import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import org.slf4j.LoggerFactory

class WorkerVerticle : AbstractVerticle() {
    private val logger = LoggerFactory.getLogger(WorkerVerticle::class.java)

    override fun start() {
        vertx.setPeriodic(10000) { id ->
            try {
                logger.info(("Zzzz....."))
                Thread.sleep(8000)
                logger.info("Up!")
            } catch (e: InterruptedException) {
                logger.error("Whoops", e)
            }
        }
    }
}

fun main() {
    val vertx = Vertx.vertx()
    val opts = DeploymentOptions()
        .setInstances(2)
        .setWorker(true)
    vertx.deployVerticle("alwyn.vertx.chapter2.WorkerVerticle", opts)
}