package alwyn.vertx.chapter2

import io.vertx.core.AbstractVerticle
import io.vertx.core.Context
import io.vertx.core.Vertx
import org.slf4j.LoggerFactory
import java.util.concurrent.CountDownLatch

class MixedThreading : AbstractVerticle() {
    private val logger = LoggerFactory.getLogger(MixedThreading::class.java)

    override fun start() {
        context = vertx.orCreateContext
        Thread {
            try {
                run(context)
            } catch (e: InterruptedException) {
                logger.error("Whoops", e)
            }
        }.start()
    }

    private fun run(context: Context) {
        val latch = CountDownLatch(1)
        logger.info("I am a non-Vertx thread")
        context.runOnContext {
            logger.info("I am on the event loop")
            vertx.setTimer(1000) {
                logger.info("This is the final countdown")
                latch.countDown()
            }
        }
        logger.info("Waiting on the countdown latch...")
        latch.await()
        logger.info("Bye!")
    }
}

fun main() {
    val vertx = Vertx.vertx()
    vertx.deployVerticle(MixedThreading())
}

