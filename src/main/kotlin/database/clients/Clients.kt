package com.database.clients

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object Clients : Table("clients") {
    val phone = varchar("phone", 45)
    val date = varchar("date",45)
    val name = varchar("name", 45)
    val email = varchar("email",45)
    val time = varchar("time", 45)
    val description = varchar("description",45)

    fun insert(clientDTO: ClientDTO) {
        transaction {
            insert {
                it[phone] = clientDTO.phone
                it[date] = clientDTO.date
                it[name] = clientDTO.name
                it[email] = clientDTO.email
                it[time] = clientDTO.time
                it[description] = clientDTO.description
            }
        }
    }
    fun fetchClient(date: String, phone: String): ClientDTO? {
        return try {
            val result: ClientDTO? = transaction {
                Clients
                    .select { (Clients.date eq date) and  (Clients.phone eq phone)}
                    .singleOrNull()
                    ?.let { row ->
                        ClientDTO(
                            phone = row[Clients.phone],
                            date = row[Clients.date],
                            name = row[name],
                            email = row[email],
                            time = row[time],
                            description = row[description],
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