package com.database.animals

import com.database.animals.AnimalsObject.toDTO
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class SurveyController(val call: ApplicationCall) {

    suspend fun survey() {
        val request = call.receive<SurveyRequestDTO>()

        val (speciesWeight, breedWeight, healthWeight) = when (request.mostImportant) {
            "species" -> Triple(3, 1, 1)
            "breed"   -> Triple(1, 3, 1)
            "health"  -> Triple(1, 1, 3)
            else -> {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "mostImportant должен быть одним из: species, breed, health"
                )
                return
            }
        }
        val maxScore = speciesWeight + breedWeight + healthWeight // всегда 5

        val animals = transaction {
            AnimalsObject.selectAll().map { it.toDTO() }
        }

        val result = animals
            .map { animal ->
                val speciesScore = if (animal.species.lowercase() == request.species.lowercase()) speciesWeight else 0
                val breedScore   = if (animal.breed.lowercase()   == request.breed.lowercase())   breedWeight   else 0
                val healthScore  = when {
                    request.willingToAdoptSick  -> healthWeight               // больное животное принимается — штрафа нет
                    animal.diseases == null     -> healthWeight               // здоровое, пользователь хочет здоровое
                    else                        -> 0                          // больное, пользователь не хочет больное
                }
                val coefficient = (speciesScore + breedScore + healthScore).toDouble() / maxScore
                AnimalMatchDTO(animal = animal, coefficient = coefficient)
            }
            .sortedByDescending { it.coefficient }

        call.respond(result)
    }
}
