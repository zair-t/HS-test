package me.zair.application.dao

import me.zair.application.dao.DatabaseFactory.dbQuery
import kotlinx.coroutines.runBlocking
import me.zair.application.models.*
import org.jetbrains.exposed.sql.*

class DAOFacadeImpl : DAOFacade {
    private fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id],
        login = row[Users.login],
        password = row[Users.password]
    )

    override suspend fun allUsers(): List<User> = dbQuery {
        Users.selectAll().map(::resultRowToUser)
    }

    override suspend fun user(id: Int): User? = dbQuery {
        Users
            .select { Users.id eq id }
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun addNewUser(login: String, password: String): User? = dbQuery {
        val insertStatement = Users.insert {
            it[Users.login] = login
            it[Users.password] = password
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)
    }

    override suspend fun editUser(id: Int, login: String, password: String): Boolean = dbQuery {
        Users.update({ Users.id eq id }) {
            it[Users.login] = login
            it[Users.password] = password
        } > 0
    }

    override suspend fun deleteUser(id: Int): Boolean = dbQuery {
        Users.deleteWhere { Users.id eq id } > 0
    }

    private fun resultRowToMessage(row: ResultRow) = Message(
        id = row[Messages.id],
        whatChat = row[Messages.whatChat],
        whoSent = row[Messages.whoSent],
        data = row[Messages.data]
    )

    override suspend fun allMessages(): List<Message> = dbQuery {
        Messages.selectAll().map(::resultRowToMessage)
    }

    override suspend fun message(id: Int): Message? = dbQuery {
        Messages
            .select { Messages.id eq id }
            .map(::resultRowToMessage)
            .singleOrNull()
    }

    override suspend fun addMessage(whatChat: String, whoSent: String, data: String): Message? = dbQuery {
        val insertStatement = Messages.insert {
            it[Messages.whatChat] = whatChat
            it[Messages.whoSent] = whoSent
            it[Messages.data] = data
        }

        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToMessage)    }

    override suspend fun editMessage(id: Int, newData: String): Boolean = dbQuery {
        Messages.update({ Messages.id eq id }) {
            it[Messages.data] = newData
        } > 0
    }

    override suspend fun deleteMessage(id: Int): Boolean = dbQuery {
        Messages.deleteWhere { Messages.id eq id } > 0
    }

    private fun resultRowToChat(row: ResultRow) = Chat(
        id = row[Chats.id],
        name = row[Chats.name],
        admin = row[Chats.admin]
    )

    override suspend fun allChats(): List<Chat> = dbQuery {
        Chats.selectAll().map(::resultRowToChat)
    }

    override suspend fun chat(id: Int): Chat? = dbQuery {
        Chats
            .select { Chats.id eq id }
            .map(::resultRowToChat)
            .singleOrNull()
    }

    override suspend fun addChat(whatChat: String, admin: String): Chat? = dbQuery {
        val insertStatement = Chats.insert {
            it[Chats.name] = whatChat
            it[Chats.admin] = admin
        }

        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToChat)
    }

    override suspend fun editChat(id: Int, newName: String): Boolean = dbQuery {
        Chats.update({ Chats.id eq id }) {
            it[Chats.name] = newName
        } > 0
    }

    override suspend fun deleteChat(id: Int): Boolean = dbQuery {
        Chats.deleteWhere { Chats.id eq id } > 0
    }

}

val dao: DAOFacade = DAOFacadeImpl().apply {
    runBlocking {
        if(allUsers().isEmpty()) {
            addNewUser("zair", "1234qwerAdmin")
        }
        if(allMessages().isEmpty()){
            addMessage("admin", "zair", "Hi!")
        }
        if(allChats().isEmpty()){
            addChat("admin", "zair")
        }
    }
}