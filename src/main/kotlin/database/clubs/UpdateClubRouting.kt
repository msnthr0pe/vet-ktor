package com.database.clubs

import com.database.users.InfoDTO
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun Application.configureUpdateClubRouting() {
    routing {
        post("/updateclub") {
            val updateRequest = call.receive<ClubsDTO>()

            val rowsUpdated = transaction {
                ClubsObject.update({ ClubsObject.address eq updateRequest.address }) {
                    it[name] = updateRequest.name
                    it[phone] = updateRequest.phone
                    it[description] = updateRequest.description
                }
            }

            if (rowsUpdated > 0) {
                call.respond(InfoDTO("Информация изменена"))
            } else {
                call.respond(InfoDTO("Запись не найдена"))
            }
        }
    }
}
