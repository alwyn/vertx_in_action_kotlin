package alwyn.vertxinaction.chapter3

import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject

class SensorData: AbstractVerticle() {
    private val lastValues = mutableMapOf<String, Double>()

    override fun start() {
        val bus = vertx.eventBus()
        bus.consumer<JsonObject>("sensor.updates", ::update)
        bus.consumer<JsonObject>("sensor.average", ::average)
    }

    private fun update(message: Message<JsonObject>) {
        val json = message.body()
        lastValues[json.getString("id")] = json.getDouble("temp")
    }

    private fun average(message: Message<JsonObject>) {
        val avg = lastValues.values.average()
        val json = JsonObject().put("average", avg)
        message.reply(json)
    }
}
