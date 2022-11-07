package me.zair.application.dao

import me.zair.application.models.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*

object DatabaseFactory {
    fun init() {
        //for connection to our db
        //what driver we use
        val driverClassName = "org.h2.Driver"
        //where is it
        val jdbcURL = "jdbc:h2:file:./build/db"
        val database = Database.connect(jdbcURL, driverClassName)
        //creating our tables
        transaction(database) {
            SchemaUtils.create(Users)
            SchemaUtils.create(Messages)
            SchemaUtils.create(Chats)
        }
    }
    //For our convenience,
    // let's create a utility function dbQuery inside the DatabaseFactory object,
    // which we'll be using for all future requests to the database
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}