package alwyn.vertx.chapter3

import io.vertx.core.AbstractVerticle
import io.vertx.core.json.JsonObject
import org.slf4j.LoggerFactory
import java.text.DecimalFormat

class Listener: AbstractVerticle() {
    private val logger = LoggerFactory.getLogger(Listener::class.java)
    private val df = DecimalFormat("#.##")

    override fun start() {
        val bus = vertx.eventBus()
        bus.consumer<JsonObject>("sensor.updates") { msg ->
            val jsonObject = msg.body()
            val id = jsonObject.getString("id")
            val temp = df.format(jsonObject.getDouble("temp"))
            logger.info("$id reports a temperature ~${temp}C ")
        }
    }
}