package com.database.shelters

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object SheltersObject : Table("shelters") {
    val address = varchar("address", 45)
    val name = varchar("name",45)
    val phone = varchar("phone", 45)
    val description = varchar("description",45)

    fun insert(sheltersDTO: SheltersDTO) {
        transaction {
            insert {
                it[address] = sheltersDTO.address
                it[name] = sheltersDTO.name
                it[phone] = sheltersDTO.phone
                it[description] = sheltersDTO.description
            }
        }
    }
    fun fetchShelter(address: String): SheltersDTO? {
        return try {
            val result: SheltersDTO? = transaction {
                SheltersObject
                    .select { (SheltersObject.address eq address) }
                    .singleOrNull()
                    ?.let { row ->
                        SheltersDTO(
                            address = row[SheltersObject.address],
                            name = row[name],
                            phone = row[phone],
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