package com.database.clubs

import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object ClubsObject : Table("clubs") {
    val address = varchar("address", 45)
    val name = varchar("name", 45)
    val phone = varchar("phone", 45)
    val description = varchar("description", 500)
    val owner = varchar("owner", 45)
    val createdAt = datetime("created_at")

    fun insert(clubsDTO: ClubsDTO) {
        transaction {
            insert {
                it[address] = clubsDTO.address
                it[name] = clubsDTO.name
                it[phone] = clubsDTO.phone
                it[description] = clubsDTO.description
                it[owner] = clubsDTO.owner
            }
        }
    }

    fun fetchClub(address: String): ClubsDTO? {
        return try {
            transaction {
                ClubsObject
                    .select { ClubsObject.address eq address }
                    .singleOrNull()
                    ?.let { row ->
                        ClubsDTO(
                            address = row[ClubsObject.address],
                            name = row[name],
                            phone = row[phone],
                            description = row[description],
                            owner = row[owner],
                        )
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun deleteClub(address: String): Int {
        return transaction {
            ClubsObject.deleteWhere { ClubsObject.address eq address }
        }
    }
}
