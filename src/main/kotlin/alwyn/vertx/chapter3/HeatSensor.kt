package alwyn.vertx.chapter3

import io.vertx.core.AbstractVerticle
import io.vertx.core.json.JsonObject
import java.util.*

class HeatSensor: AbstractVerticle() {
    private val random = Random()
    private val id = UUID.randomUUID().toString()
    private var temp: Double = 21.0

    override fun start() {
        scheduleNextUpdate()
    }

    private fun scheduleNextUpdate() {
        vertx.setTimer((random.nextInt(5000) + 1000).toLong(), ::update)
    }

    private fun update(tid: Long) {
        temp += (delta() / 10)
        val payload = JsonObject()
            .put("id", id)
            .put("temp", temp)
        vertx.eventBus().publish("sensor.updates", payload)
        scheduleNextUpdate()
    }

    private fun delta(): Double {
        return if (random.nextInt() > 0) {
            random.nextGaussian()
        } else {
            - random.nextGaussian()
        }
    }
}