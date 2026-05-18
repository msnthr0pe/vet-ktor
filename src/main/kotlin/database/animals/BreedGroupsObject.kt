package com.database.animals

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object BreedGroupsObject : Table("breed_groups") {
    val breed   = varchar("breed",   100)
    val species = varchar("species",  45)
    val groupId = integer("group_id")

    fun findGroupId(breed: String, species: String): Int? {
        return try {
            transaction {
                BreedGroupsObject
                    .select {
                        (BreedGroupsObject.breed   eq breed.lowercase()) and
                        (BreedGroupsObject.species eq species.lowercase())
                    }
                    .singleOrNull()
                    ?.get(BreedGroupsObject.groupId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
