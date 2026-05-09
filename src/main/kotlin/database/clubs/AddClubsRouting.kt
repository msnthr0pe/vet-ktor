package com.database.clubs

import io.ktor.server.application.Application
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureAddClubRouting() {
    routing {
        post("/addclub") {
            val controller = AddClubController(call)
            controller.addClub()
        }
    }
}
