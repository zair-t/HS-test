package me.zair.application.models

import org.jetbrains.exposed.sql.*

data class Chat(val id: Int, val name: String, val admin: String)

object Chats : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 128)
    val admin = varchar("admin", 128)

    override val primaryKey = PrimaryKey(id)
}