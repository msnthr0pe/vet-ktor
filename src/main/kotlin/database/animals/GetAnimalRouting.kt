package com.database.animals

import com.database.users.InfoDTO
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureGetAnimalRouting() {
    routing {
        post("/getanimal") {
            val input = call.receive<InfoDTO>()
            val animal = AnimalsObject.fetchAnimal(input.info)
            if (animal != null) {
                call.respond(animal)
            } else {
                call.respond(HttpStatusCode.NotFound, InfoDTO("Животное не найдено"))
            }
        }
    }
}
