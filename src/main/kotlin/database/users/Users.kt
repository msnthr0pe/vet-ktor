package com.database.users

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table("Users") {
    val phone = varchar("phone", 45)
    val name = varchar("name",45)
    val surname = varchar("surname",45)
    val login = varchar("login", 45)
    val password = varchar("password", 45)
    val role = varchar("role", 45)

    fun insert(userDTO: UserDTO) {
        transaction {
            insert {
                it[phone] = userDTO.phone
                it[name] = userDTO.name
                it[surname] = userDTO.surname
                it[login] = userDTO.login
                it[password] = userDTO.password
                it[role] = userDTO.role
            }
        }
    }
    fun fetchUser(login: String): UserDTO? {
        return try {
            val result: UserDTO? = transaction {
                Users
                    .select { Users.login eq login }
                    .singleOrNull()
                    ?.let { row ->
                        UserDTO(
                            phone = row[phone],
                            name = row[name],
                            surname = row[surname],
                            login = row[Users.login],
                            password = row[password],
                            role = row[role]
                        )
                    }
            }
            result
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}