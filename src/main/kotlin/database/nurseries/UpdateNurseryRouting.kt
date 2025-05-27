package com.database.nurseries

import com.database.users.EmailDTO
import com.database.users.InfoDTO
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun Application.configureUpdateNurseryRouting() {
    routing {
        post("/updatenursery") {
            val updateRequest = call.receive<NurseriesDTO>()

            val rowsUpdated = transaction {
                NurseriesObject.update({ NurseriesObject.address eq
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
