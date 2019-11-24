package alwyn.vertxinaction.chapter2

import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import org.slf4j.LoggerFactory

class EmptyVerticle : AbstractVerticle() {
    private val logger = LoggerFactory.getLogger(EmptyVerticle::class.java)
    override fun start() {
        logger.info("start")
    }
    override fun stop() {
        logger.info("stop")
    }
}

class Deployer : AbstractVerticle() {
    private val logger = LoggerFactory.getLogger(Deployer::class.java)

    override fun start() {
        var delay = 1000L
        for (i in 1..50) {
            vertx.setTimer(delay) {
                id -> deploy()
            }
            delay += 1000
        }
    }

    private fun deploy() {
        vertx.deployVerticle(EmptyVerticle()) { ar ->
            if (ar.succeeded()) {
                val id = ar.result()
                logger.info("Successfully deployed $id")
                vertx.setTimer(5000) { tid ->
                    undeployLater(id)
                }
            } else {
                logger.error("Error while deploying: ${ar.cause()}")
            }
        }
    }

    private fun undeployLater(id: String) {
        vertx.undeploy(id) { ar ->
            if (ar.succeeded()) {
                logger.info("$id was undeployed")
            } else {
                logger.error("$id could not be undeployed")
            }
        }
    }
}

fun main() {
    val vertx = Vertx.vertx()
    vertx.deployVerticle(Deployer())
}
