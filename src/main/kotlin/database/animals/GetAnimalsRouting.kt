package com.database.animals

import com.database.animals.AnimalsObject.toDTO
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureGetAnimalsRouting() {
    routing {
        get("/getanimals") {
            val animals = transaction {
                AnimalsObject.selectAll().map { it.toDTO() }
            }
            call.respond(animals)
        }
    }
}
