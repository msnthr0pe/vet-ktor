package com.database.animals

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object AnimalsObject : Table("animals") {
    val id = varchar("id", 45)
    val nickname = varchar("nickname", 45)
    val species = varchar("species", 45)
    val breed = varchar("breed", 45)
    val age = integer("age")
    val diseases = varchar("diseases", 255).nullable()
    val imageUrl = varchar("image_url", 500).nullable()
    val shelterAddress = varchar("shelter_address", 45).nullable()
    val nurseryAddress = varchar("nursery_address", 45).nullable()

    fun insert(animalDTO: AnimalDTO) {
        transaction {
            insert {
                it[id] = animalDTO.id
                it[nickname] = animalDTO.nickname
                it[species] = animalDTO.species
                it[breed] = animalDTO.breed
                it[age] = animalDTO.age
                it[diseases] = animalDTO.diseases
                it[imageUrl] = animalDTO.imageUrl
                it[shelterAddress] = animalDTO.shelterAddress
                it[nurseryAddress] = animalDTO.nurseryAddress
            }
        }
    }

    fun fetchAnimal(id: String): AnimalDTO? {
        return try {
            transaction {
                AnimalsObject
                    .select { AnimalsObject.id eq id }
                    .singleOrNull()
                    ?.toDTO()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun deleteAnimal(id: String): Int {
        return transaction {
            AnimalsObject.deleteWhere { AnimalsObject.id eq id }
        }
    }

    fun org.jetbrains.exposed.sql.ResultRow.toDTO() = AnimalDTO(
        id = this[id],
        nickname = this[nickname],
        species = this[species],
        breed = this[breed],
        age = this[age],
        diseases = this[diseases],
        imageUrl = this[imageUrl],
        shelterAddress = this[shelterAddress],
        nurseryAddress = this[nurseryAddress],
    )
}
