import csstype.px
import csstype.rgb
import emotion.react.css
import kotlinx.browser.document
import org.w3c.dom.*
import react.create
import react.dom.client.createRoot

fun main() {
    //get an url of our route
    val path: List<String> = document.URL.split("/")

    if(path.size >= 2 && path[path.size - 2] == "chats"){
        (document.getElementById("clear") as HTMLButtonElement).apply {
            innerText = "CLEAR CHAT"
        }
        (document.getElementById("send") as HTMLInputElement).apply {
            value = "SEND MESSAGE"
        }
    }

    //get name of our user
    val name: String = document.getElementsByTagName("h1").get(0)!!.innerHTML
    //create a websocket request
    val ws = WebSocket("ws://localhost:8080/main/chats/${name}").apply {
        onmessage = {
            //if we get a message
            //we add it to our div element which id is "history"
            val div = document.getElementById("history") as HTMLDivElement

            div.innerHTML = "${div.innerHTML}<br>${it.data as String}"

            Unit
        }
    }

    //if we want to send message, we need to click to this button and handle this event(onclick)
    (document.getElementById("send") as HTMLInputElement).apply {
        onclick = {
            val message = (document.getElementById("message") as HTMLInputElement).value

            ws.send(message)
        }
    }
    //similar with |^| button
    (document.getElementById("clear") as HTMLButtonElement).apply {
       value = "Clear"
        onclick = {
            ws.send("cleaning")
        }
    }
}