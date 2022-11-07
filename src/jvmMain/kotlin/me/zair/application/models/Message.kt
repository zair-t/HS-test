package me.zair.application.models

import org.jetbrains.exposed.sql.Table

data class Message(val id: Int, val whatChat: String, val whoSent: String, val data: String)

object Messages : Table() {
    val id = integer("id").autoIncrement()
    val whatChat = varchar("whatChat", 128)
    val whoSent = varchar("whoSent", 128)
    val data = varchar("data", 512)

    override val primaryKey = PrimaryKey(id)}
