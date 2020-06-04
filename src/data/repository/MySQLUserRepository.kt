package com.alexzh.coffeedrinks.api.data.repository

import com.alexzh.coffeedrinks.api.data.model.User
import com.alexzh.coffeedrinks.api.data.model.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class MySQLUserRepository : UserRepository {

    // TODO: should return User
    override suspend fun createUser(user: User) {
        transaction {
            val statement = UserTable.insert {
                it[name] = user.name
                it[email] = user.email
                it[passwordHash] = user.passwordHash
            }
        }
    }

    override suspend fun deleteUser(id: Long): Boolean {
        return UserTable.deleteWhere { UserTable.id eq id } > 0
    }

    // TODO: should return User
    override suspend fun updateUser(user: User): Boolean {
        return UserTable.update({ UserTable.id eq user.id}) {
            it[name] = user.name
            it[email] = user.email
            it[passwordHash] = user.passwordHash
        } > 0
    }

    override suspend fun getUserById(id: Long): User? {
        return dbQuery {
            UserTable.select { UserTable.id eq id }
                    .mapNotNull { toUser(it) }
                    .singleOrNull()
        }
    }

    override suspend fun getUsers(): List<User> {
        return dbQuery {
            UserTable.selectAll()
                    .map { toUser(it) }
        }
    }

    private fun toUser(row: ResultRow): User {
        return User(
                id = row[UserTable.id],
                name = row[UserTable.name],
                email = row[UserTable.email],
                passwordHash = row[UserTable.passwordHash]
        )
    }
}