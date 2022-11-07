package me.zair.application.models

import io.ktor.websocket.*

class Connection(val session: DefaultWebSocketSession, val login: String) {
    val name = login
}