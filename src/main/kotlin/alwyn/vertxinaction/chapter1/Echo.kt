package alwyn.vertxinaction.chapter1

import io.vertx.core.Vertx
import io.vertx.core.net.NetSocket

class Echo {
    companion object Handler {
        private var numberOfConnections = 0
        fun handleNewClient(socket: NetSocket) {
            numberOfConnections++
            socket.handler { buffer ->
                socket.write(buffer)
                if (buffer.toString().endsWith("/quit\r\n")) {
                    socket.close()
                }
            }
            socket.closeHandler { numberOfConnections-- }
        }

        fun howMany() = "We now have $numberOfConnections connections"
    }
}

fun main() {
    val vertx = Vertx.vertx()

    vertx.createNetServer()
        .connectHandler(Echo.Handler::handleNewClient)
        .listen(3000)

    vertx.setPeriodic(5000) { println(Echo.howMany())

    vertx.createHttpServer()
        .requestHandler { request -> request.response().end(Echo.howMany())}
        .listen(8080)
    }
}
