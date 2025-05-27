package com.database.nurseries

import io.ktor.server.application.Application
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureAddNurseryRouting() {
    routing {
        post("/addnursery") {
            val addNurseriesController = AddNurseryController(call)
            addNurseriesController.addNursery()
        }
    }
}