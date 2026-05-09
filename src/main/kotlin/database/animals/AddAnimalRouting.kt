package com.database.animals

import io.ktor.server.application.Application
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureAddAnimalRouting() {
    routing {
        post("/addanimal") {
            AddAnimalController(call).addAnimal()
        }
    }
}
