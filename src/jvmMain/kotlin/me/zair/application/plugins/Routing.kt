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
                    head{
                        link(rel = "stylesheet", href = "/static/main.css", type = "text/css")
                    }
                    body {
                        div{
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
        }
        //The get function within the routing block receives GET requests made
        //to the / path and responds with a html page
        get("/") {
            call.respondHtml {
                head{
                    link(rel = "stylesheet", href = "/static/login.css", type = "text/css")
                }
                body {
                    div{
                        h2{
                            +"Login"
                        }
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
                                submitInput() { value = "Submit" }
                            }
                        }
                    }


                }
            }
        }
        //The get function within the routing block receives GET requests made
        //to the /main path and responds with a html page
        get("/main"){
            if(userLogin == null)
                call.respond(HttpStatusCode.NotFound)
            call.respondHtml{
                head{
                    link(rel = "stylesheet", href = "/static/main.css", type = "text/css")
                }
                body {
                    div{
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
                head{
                    link(rel = "stylesheet", href = "/static/register.css", type = "text/css")
                }
                body {
                    div{
                        h2{
                            + "Registration"
                        }
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
                call.respond(HttpStatusCode.NotFound)
            val name = call.parameters.getOrFail<String>("name").toString()
            val messages: List<Message> = dao.allMessages().filter { it.whatChat == name }
            var isAdmin: Boolean = false
            for(i in dao.allChats())
                if(i.name == name && i.admin == userLogin)
                    isAdmin = true
            val n: Int = messages.size
            call.respondHtml {
                head {
                    link(rel = "stylesheet", href = "/static/chat.css", type = "text/css")
                    title("Full stack chat")
                }
                body {
                    div{
                        h1 {
                            + "Chat name: $name"
                        }
                        h2 {
                            +"You signed in as $userLogin"
                        }

                        input {
                            type = InputType.text
                            id = "message"
                        }

                        input {
                            type = InputType.button
                            id = "send"
                        }
                        div{
                            div{
                                if (n != 0) {
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

                                table {
                                    id = "history"
                                }
                            }
                        }


                        if (isAdmin)
                            input {
                                type = InputType.button
                                id = "clear"
                            }
                        script(src = "/static/fullStackWithKtor.js") {}
                    }
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
