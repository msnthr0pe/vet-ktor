package com.database.animals

import com.database.nurseries.NurseriesObject
import com.database.shelters.SheltersObject
import com.database.users.InfoDTO
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureGetAnimalFullRouting() {
    routing {
        post("/getanimalfull") {
            val input = call.receive<InfoDTO>()
            val animal = AnimalsObject.fetchAnimal(input.info)
            if (animal == null) {
                call.respond(HttpStatusCode.NotFound, InfoDTO("Животное не найдено"))
                return@post
            }

            val full = transaction {
                FullAnimalDTO(
                    id = animal.id,
                    nickname = animal.nickname,
                    species = animal.species,
                    breed = animal.breed,
                    age = animal.age,
                    diseases = animal.diseases,
                    shelter = animal.shelterAddress?.let { addr ->
                        SheltersObject.fetchShelter(addr)
                    },
                    nursery = animal.nurseryAddress?.let { addr ->
                        NurseriesObject.fetchNursery(addr)
                    },
                )
            }
            call.respond(full)
        }
    }
}
