package com.alexzh.coffeedrinks.api.data.repository

import com.alexzh.coffeedrinks.api.data.db.table.UsersTable
import com.alexzh.coffeedrinks.api.data.exception.UserAlreadyExistException
import com.alexzh.coffeedrinks.api.data.model.User
import com.alexzh.coffeedrinks.api.data.utils.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement

class UserRepositoryImpl : UserRepository {

    override suspend fun createUser(user: User): User? {
        if (getUserByEmail(user.email) != null) {
            throw UserAlreadyExistException()
        }

        var statement: InsertStatement<Number>? = null
        dbQuery {
            statement = UsersTable.insert {
                it[username] = user.name
                it[email] = user.email
                it[password] = user.passwordHash
            }
        }
        return statement?.resultedValues?.first()?.let { toUser(it) }
    }

    override suspend fun getUserById(userId: Long): User? {
        return dbQuery {
            UsersTable.select { UsersTable.id eq userId }
                .mapNotNull { toUser(it) }
                .singleOrNull()
        }
    }

    override suspend fun getUserByEmail(email: String): User? {
        return dbQuery {
            UsersTable.select { UsersTable.email eq email }
                .mapNotNull { toUser(it) }
                .singleOrNull()
        }
    }

    override suspend fun getUserByEmailAndPassword(email: String, password: String): User? {
        return dbQuery {
            UsersTable.select { (UsersTable.email eq email) and (UsersTable.password eq password) }
                .mapNotNull { toUser(it) }
                .singleOrNull()
        }
    }

    private fun toUser(row: ResultRow): User {
        return User(
            id = row[UsersTable.id],
            name = row[UsersTable.username],
            email = row[UsersTable.email],
            passwordHash = row[UsersTable.password]
        )
    }
}