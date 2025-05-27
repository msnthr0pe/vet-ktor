package com.database.shelters

import io.ktor.server.application.Application
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureAddShelterRouting() {
    routing {
        post("/addshelter") {
            val addNewsController = AddShelterController(call)
            addNewsController.addShelter()
        }
    }
}