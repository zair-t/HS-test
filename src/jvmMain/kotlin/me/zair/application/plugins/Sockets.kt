package me.zair.application.plugins

import me.zair.application.models.Connection
import io.ktor.network.sockets.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import me.zair.application.dao.dao
import me.zair.application.models.Message
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

//atomic because two guests can simultaneously get access to chat, it can be a cause of identically id
val lastIdForGuests =  AtomicInteger(0)

fun Application.configureSockets() {
    //We first enable WebSocket-related functionality provided by the Ktor framework
    // by installing the WebSockets Ktor plugin
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15) //Duration between pings or 0 to disable pings
        timeout = Duration.ofSeconds(15) //write/ping timeout after that a connection will be closed
        maxFrameSize = Long.MAX_VALUE //max message length
        masking = false
    }
    routing {
        //synchronized because connections can can simultaneously get access to chat,
        // it can be a cause of identically name
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
        webSocket("/main/chats/{name}") {
            if(userLogin == "anonymous")
                userLogin += lastIdForGuests.getAndIncrement()
            //create connection
            val thisConnection = Connection(this, userLogin!!)
            connections += thisConnection
            try {
                //if messages come
                for (frame in incoming) {
                    //check if frame contains text otherwise -> skip
                    frame as? Frame.Text ?: continue
                    //get message from frame
                    val receivedText = frame.readText()
                    //we check if it "cleaning"
                    if(receivedText == "cleaning"){
                        //delete all messages from db for this chat
                        val msgs: List<Message> = dao.allMessages()
                        for(i in msgs.indices){
                            if(msgs[i].whatChat == call.parameters.getOrFail("name")){
                                dao.deleteMessage(msgs[i].id)
                            }
                        }
                    }
                    else {
                        //add new message to bd
                        dao.addMessage(call.parameters.getOrFail("name"), thisConnection.name, receivedText)
                        val textWithUsername = "[${thisConnection.name}]: $receivedText"
                        //send message for each connection
                        connections.forEach {
                            it.session.send(textWithUsername)
                        }
                    }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                //remove our connection if he out from socket or lose the connection with server
                connections -= thisConnection
            }
        }
    }
}
