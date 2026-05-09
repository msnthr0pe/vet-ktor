package com.database.clubs

import com.database.users.InfoDTO
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureDeleteClubRouting() {
    routing {
        post("/deleteclub") {
            val input = call.receive<InfoDTO>()
            val rowsDeleted = ClubsObject.deleteClub(input.info)

            if (rowsDeleted > 0) {
                call.respond(InfoDTO("Клуб удалён"))
            } else {
                call.respond(InfoDTO("Запись не найдена"))
            }
        }
    }
}
