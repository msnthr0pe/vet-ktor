package com.database.animals

import com.database.users.InfoDTO
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun Application.configureUpdateAnimalRouting() {
    routing {
        post("/updateanimal") {
            val request = call.receive<AnimalDTO>()

            val hasShelter = request.shelterAddress != null
            val hasNursery = request.nurseryAddress != null
            if (hasShelter == hasNursery) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "Укажите ровно одно из полей: shelterAddress или nurseryAddress"
                )
                return@post
            }

            val rowsUpdated = transaction {
                AnimalsObject.update({ AnimalsObject.id eq request.id }) {
                    it[species] = request.species
                    it[breed] = request.breed
                    it[age] = request.age
                    it[diseases] = request.diseases
                    it[shelterAddress] = request.shelterAddress
                    it[nurseryAddress] = request.nurseryAddress
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
