package com.alexzh.coffeedrinks.api.data.repository

import com.alexzh.coffeedrinks.api.data.exception.UserAlreadyExistException
import com.alexzh.coffeedrinks.api.data.model.User
import com.alexzh.coffeedrinks.api.data.model.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement

class MySQLUserRepository : UserRepository {

    override suspend fun createUser(user: User): User? {
        if (getUserByEmail(user.email) != null) {
            throw UserAlreadyExistException()
        }

        var statement: InsertStatement<Number>? = null
        dbQuery {
            statement = UserTable.insert {
                it[name] = user.name
                it[email] = user.email
                it[passwordHash] = user.passwordHash
            }
        }
        return statement?.resultedValues?.first()?.let { toUser(it) }
    }

    override suspend fun getUserById(id: Long): User? {
        return dbQuery {
            UserTable.select { UserTable.id eq id }
                    .mapNotNull { toUser(it) }
                    .singleOrNull()
        }
    }

    override suspend fun getUserByEmail(email: String): User? {
        return dbQuery {
            UserTable.select { UserTable.email eq email }
                    .mapNotNull { toUser(it) }
                    .singleOrNull()
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