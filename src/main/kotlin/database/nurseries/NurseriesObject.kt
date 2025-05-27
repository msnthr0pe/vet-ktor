package com.database.nurseries

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object NurseriesObject : Table("nurseries") {
    val address = varchar("address", 45)
    val name = varchar("name",45)
    val phone = varchar("phone", 45)
    val description = varchar("description",45)
    val owner = varchar("owner",45)

    fun insert(sheltersDTO: NurseriesDTO) {
        transaction {
            insert {
                it[address] = sheltersDTO.address
                it[name] = sheltersDTO.name
                it[phone] = sheltersDTO.phone
                it[description] = sheltersDTO.description
            }
        }
    }
    fun fetchNursery(address: String): NurseriesDTO? {
        return try {
            val result: NurseriesDTO? = transaction {
                NurseriesObject
                    .select { (NurseriesObject.address eq address) }
                    .singleOrNull()
                    ?.let { row ->
                        NurseriesDTO(
                            address = row[NurseriesObject.address],
                            name = row[name],
                            phone = row[phone],
                            description = row[description],
                            owner = row[owner],
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