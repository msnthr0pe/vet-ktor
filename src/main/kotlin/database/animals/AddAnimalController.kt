package com.database.animals

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.util.UUID

class AddAnimalController(val call: ApplicationCall) {
    suspend fun addAnimal() {
        val request = call.receive<AnimalDTO>()

        val hasShelter = request.shelterAddress != null
        val hasNursery = request.nurseryAddress != null
        if (hasShelter == hasNursery) {
            call.respond(
                HttpStatusCode.BadRequest,
                "Укажите ровно одно из полей: shelterAddress или nurseryAddress"
            )
            return
        }

        val id = UUID.randomUUID().toString()
        try {
            AnimalsObject.insert(request.copy(id = id))
        } catch (e: ExposedSQLException) {
            e.printStackTrace()
            call.respond(HttpStatusCode.Conflict, "SQL error: ${e.localizedMessage}")
            return
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, "Unexpected error: ${e.localizedMessage}")
            return
        }

        call.respond(HttpStatusCode.Created, AnimalsObject.fetchAnimal(id)!!)
    }
}
