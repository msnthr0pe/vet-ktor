package com.database.animals

import com.database.animals.AnimalsObject.toDTO
import com.database.users.InfoDTO
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureGetAnimalsByNurseryRouting() {
    routing {
        post("/getanimalsbynursery") {
            val input = call.receive<InfoDTO>()
            val animals = transaction {
                AnimalsObject.select { AnimalsObject.nurseryAddress eq input.info }
                    .map { it.toDTO() }
            }
            call.respond(animals)
        }
    }
}
