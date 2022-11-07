package me.zair.application.dao

import me.zair.application.models.*

interface DAOFacade {
    //dealing with users
    suspend fun allUsers(): List<User>
    suspend fun user(id: Int): User?
    suspend fun addNewUser(login: String, password: String): User?
    suspend fun editUser(id: Int, newLogin: String, newPassword: String): Boolean
    suspend fun deleteUser(id: Int): Boolean

    //dealing with messages
    suspend fun allMessages(): List<Message>
    suspend fun message(id: Int): Message?
    suspend fun addMessage(whatChat: String, whoSent: String, data: String): Message?
    suspend fun editMessage(id: Int, newData: String): Boolean
    suspend fun deleteMessage(id: Int): Boolean

    //dealing with chats
    suspend fun allChats(): List<Chat>
    suspend fun chat(id: Int): Chat?
    suspend fun addChat(whatChat: String, admin: String): Chat?
    suspend fun editChat(id: Int, newName: String): Boolean
    suspend fun deleteChat(id: Int): Boolean
}

