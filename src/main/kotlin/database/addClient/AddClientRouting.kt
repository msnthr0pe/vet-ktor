package com.database.addClient

import io.ktor.server.application.Application
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureAddClientRouting() {
    routing {
        post("/addclient") {
            val addClientController = AddClientController(call)
            addClientController.registerNewClient()
        }
    }
}