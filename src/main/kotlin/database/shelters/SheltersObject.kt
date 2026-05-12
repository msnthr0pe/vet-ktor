package com.database.shelters

import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction

object SheltersObject : Table("shelters") {
    val address = varchar("address", 45)
    val name = varchar("name", 45)
    val phone = varchar("phone", 45)
    val description = varchar("description", 45)
    val owner = varchar("owner", 45)
    val clubAddress = varchar("club_address", 45).nullable()
    val createdAt = datetime("created_at")

    fun insert(sheltersDTO: SheltersDTO) {
        transaction {
            insert {
                it[address] = sheltersDTO.address
                it[name] = sheltersDTO.name
                it[phone] = sheltersDTO.phone
                it[description] = sheltersDTO.description
                it[owner] = sheltersDTO.owner
                it[clubAddress] = sheltersDTO.clubAddress
            }
        }
    }

    fun fetchShelter(address: String): SheltersDTO? {
        return try {
            transaction {
                SheltersObject
                    .select { SheltersObject.address eq address }
                    .singleOrNull()
                    ?.let { row ->
                        SheltersDTO(
                            address = row[SheltersObject.address],
                            name = row[name],
                            phone = row[phone],
                            description = row[description],
                            owner = row[owner],
                            clubAddress = row[clubAddress],
                        )
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
