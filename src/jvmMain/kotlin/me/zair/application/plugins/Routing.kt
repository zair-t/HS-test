package me.zair.application.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.html.*
import kotlinx.html.form
import me.zair.application.dao.dao
import me.zair.application.models.Message

//we will use it for displaying its in chat page
var userLogin: String? = null

fun Application.configureRouting() {

    //To install the form authentication provider
    install(Authentication) {
        form("auth-form") {
            userParamName = "username"
            passwordParamName = "password"
            //The validate function validates a username and password
            validate { credentials ->
                var flag = true
                for (user in dao.allUsers()) {
                    println(credentials.name + "\t" + credentials.password)
                    println("\t" + user.login + "\t" + user.password)
                    if (credentials.name == user.login && credentials.password == user.password) {
                        userLogin = user.login
                        flag = false
                    }
                }

                if (flag)
                    null
                else
                    UserIdPrincipal(credentials.name)
            }
        }
    }

    routing {
        //go here if authentication is successful
        authenticate("auth-form") {
            //The post function within the routing block receives POST requests made
            //to the /main path and responds with a html page
            post("/main") {
                call.respondHtml {
                    body {
                        form(action = "/main/chats/{name}", method = FormMethod.post) {
                            p {
                                +"Write name of chat to join: "
                                textInput(name = "name")
                            }
                            p {
                                submitInput() { value = "JOIN!" }
                            }
                        }
                        form(action = "/main/chats/creating", method = FormMethod.post) {
                            p {
                                +"Write name of chat to create: "
                                textInput(name = "name")
                            }
                            p {
                                submitInput() { value = "CREATE!" }
                            }
                        }
                    }
                }
            }
        }
        //The get function within the routing block receives GET requests made
        //to the / path and responds with a html page
        get("/") {
            call.respondHtml {
                head{
                    link(rel = "stylesheet", href = "/static/login.css", type = "text/css")
                }
                body {
                    form(
                        action = "/main",
                        encType = FormEncType.applicationXWwwFormUrlEncoded,
                        method = FormMethod.post
                    ) {
                        p {
                            +"Username:"
                            textInput(name = "username")
                        }
                        p {
                            +"Password:"
                            passwordInput(name = "password")
                        }
                        p {
                            a("/register") { +"Have not an account yet? Register" }
                        }
                        p {
                            a("/main") {
                                +"Do not want to register? Sign in anonymously"
                                userLogin = "anonymous"
                            }
                        }
                        p {
                            submitInput() { value = "Login" }
                        }
                    }
                }
            }
        }
        //The get function within the routing block receives GET requests made
        //to the /main path and responds with a html page
        get("/main"){
            call.respondHtml{
                body {
                    form(action = "/main/chats/{name}", method = FormMethod.post) {
                        p {
                            +"Write name of chat to join: "
                            textInput(name = "name")
                        }
                        p {
                            submitInput() { value = "JOIN!" }
                        }
                    }
                }
            }
        }
        //The post function within the routing block receives POST requests made
        //to the / path and responds with a html page
        //we use this to register our user
        post("/") {
            val formParameters = call.receiveParameters()
            val login: String = formParameters.getOrFail("login")
            val password: String = formParameters.getOrFail("password")
            val repeatedPassword: String = formParameters.getOrFail("repeatedPassword")
            var flag = true
            for (user in dao.allUsers()) {
                if (login == user.login && password == user.password)
                    flag = false
            }
            if (!flag)
                call.respondText { "Error, you have already had account! " }
            else if (password == repeatedPassword) {
                dao.addNewUser(login, password)
                call.respondRedirect("/") //-> call get("/")
            } else
                call.respondText { "Error, passwords don't match! " }
        }
        //The get function within the routing block receives GET requests made
        //to the /register path and responds with a html register page
        get("/register") {
            call.respondHtml {
                body {
                    form(
                        action = "/",
                        method = FormMethod.post
                    ) {
                        p {
                            +"Enter your login:"
                            textInput(name = "login")
                        }
                        p {
                            +"Create password:"
                            passwordInput(name = "password")
                        }
                        p {
                            +"Repeat your password:"
                            passwordInput(name = "repeatedPassword")
                        }
                        p {
                            submitInput() { value = "Register" }
                        }
                    }
                }
            }
        }
        //The get function within the routing block receives GET requests made
        //to the /main/chats/{name} (<- it's name of our chat) path and responds with a html page
        //first is a name of chat
        //second is a user name
        //third is old messages
        //last is new messages
        get("/main/chats/{name}") {
            //if user don't login he cannot get access to this page
            if(userLogin == null)
                call.respondHtml(HttpStatusCode.NotFound){}
            val name = call.parameters.getOrFail<String>("name").toString()
            val messages: List<Message> = dao.allMessages().filter { it.whatChat == name }
            for (i in messages)
                println(i.whoSent + "\t" + i.data)
            var isAdmin: Boolean = false
            for(i in dao.allChats())
                if(i.name == name && i.admin == userLogin)
                    isAdmin = true
            val n: Int = messages.size
            call.respondHtml {
                head {
                    title("Full stack chat")
                }
                body {
                    h1 {
                        +name
                    }
                    h3 {
                        +userLogin!!
                    }

                    input {
                        type = InputType.text
                        id = "message"
                    }

                    input {
                        type = InputType.button
                        id = "send"
                    }
                    if(n!=0) {
                        p {
                            h4 {
                                +"OLD: "
                            }
                            //for old messages
                            for (i in 0 until n) {
                                +"[${messages[i].whoSent}]: ${messages[i].data}"
                                if (i == n - 1)
                                    h4 {
                                        +"NEW: "
                                    }
                                else
                                    br { }
                            }
                        }
                    }

                    div {
                        id = "history"
                    }

                    if(isAdmin)
                        button {
                            id = "clear"
                        }
                    script(src = "/static/fullStackWithKtor.js") {}
                }
            }
        }
        //The post function within the routing block receives POST requests made
        //to the /main/chats/{name} path and responds with an error or redirect this rout
        //we use this for check if in bd chat that was been writen by our user
        post("/main/chats/{name}") {
            val name: String = call.receiveParameters().getOrFail("name")
            for (cht in dao.allChats()) {
                if (cht.name == name) {
                    call.respondRedirect("/main/chats/${name}")
                }
            }
            call.respond(HttpStatusCode.NotFound,"Error, there is no such chat!")
        }
        //The post function within the routing block receives POST requests made
        //to the /main/chats/creating path and responds with an error or redirect this rout
        //we use this for checking if there is(in bd) already exist chat that user want to create
        post("/main/chats/creating") {
            val name: String = call.receiveParameters().getOrFail("name")
            for(cht in dao.allChats())
                if(cht.name == name)
                    call.respond(HttpStatusCode.Conflict, "Error, chat with this name is already exist!")
            dao.addChat(name, userLogin!!)
            call.respondRedirect("/main/chats/${name}")
        }
        //we need this for use static resources without written all path to this resources.
        //we can just write /static/{resource_that_we_want_to_use}
        static("/static") {
            resources()
        }

    }
}
