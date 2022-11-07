package me.zair.application

import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import me.zair.application.dao.DatabaseFactory
import me.zair.application.plugins.configureRouting
import me.zair.application.plugins.configureSockets

fun main() {
    //The embeddedServer function is used to configure server parameters in code and run an application.
    embeddedServer(Netty, port = 8080, host = "127.0.0.1", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init() //initialization our database
    configureSockets() //configureRouting is an extension function that defines how will work webSockets
    configureRouting() //configureRouting is an extension function that defines routing
}