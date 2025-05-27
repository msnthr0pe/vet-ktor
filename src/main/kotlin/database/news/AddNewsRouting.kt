package com.database.news

import com.database.shelters.AddShelterController
import io.ktor.server.application.Application
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureAddNewsRouting() {
    routing {
        post("/addnews") {
            val addNewsController = AddNewsController(call)
            addNewsController.registerNews()
        }
    }
}