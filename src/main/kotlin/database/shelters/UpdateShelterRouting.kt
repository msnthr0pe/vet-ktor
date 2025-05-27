package com.database.shelters

import com.database.users.EmailDTO
import com.database.users.InfoDTO
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun Application.configureUpdateShelterRouting() {
    routing {
        post("/updateshelter") {
            val updateRequest = call.receive<SheltersDTO>()

            val rowsUpdated = transaction {
                SheltersObject.update({ SheltersObject.address eq
                        updateRequest.address }) {
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
