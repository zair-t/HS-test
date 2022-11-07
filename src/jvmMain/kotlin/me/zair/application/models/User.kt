package me.zair.application.models

import org.jetbrains.exposed.sql.*

data class User(val id: Int, val login: String, val password: String)

object Users : Table() {
    val id = integer("id").autoIncrement()
    val login = varchar("login", 128)
    val password = varchar("password", 256)

    override val primaryKey = PrimaryKey(id)
}