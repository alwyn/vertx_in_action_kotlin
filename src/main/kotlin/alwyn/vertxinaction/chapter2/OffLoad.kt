package alwyn.vertxinaction.chapter2

import io.vertx.core.*
import org.slf4j.LoggerFactory

class OffLoad : AbstractVerticle() {
    private val logger = LoggerFactory.getLogger(OffLoad::class.java)

    override fun start() {
        vertx.setPeriodic(5000) {
            logger.info("Tick")
            vertx.executeBlocking(::blockingCode, ::resultHandler)
        }
    }

    private fun blockingCode(promise: Promise<String>) {
        logger.info("Blocking code running")
        try {
            Thread.sleep(4000)
            logger.info("Done!")
            promise.complete("Ok!")
        } catch (e: InterruptedException) {
            promise.fail(e)
        }
    }

    private fun resultHandler(ar: AsyncResult<String>) {
        if (ar.succeeded()) {
            logger.info("Blocking code result: ${ar.result()}")
        }
        logger.error("Woops ${ar.cause()}")
    }
}

fun main() {
    val vertx = Vertx.vertx()
    vertx.deployVerticle(OffLoad())
}
