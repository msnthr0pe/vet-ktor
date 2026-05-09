package com.database.animals

import com.database.users.InfoDTO
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureDeleteAnimalRouting() {
    routing {
        post("/deleteanimal") {
            val input = call.receive<InfoDTO>()
            val rowsDeleted = AnimalsObject.deleteAnimal(input.info)

            if (rowsDeleted > 0) {
                call.respond(InfoDTO("Животное удалено"))
            } else {
                call.respond(InfoDTO("Запись не найдена"))
            }
        }
    }
}
