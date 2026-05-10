package com.database.shelters

import com.database.users.InfoDTO
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureGetShelterByAddressRouting() {
    routing {
        post("/getshelterbyaddress") {
            val input = call.receive<InfoDTO>()
            val shelter = SheltersObject.fetchShelter(input.info)
            if (shelter != null) {
                call.respond(shelter)
            } else {
                call.respond(HttpStatusCode.NotFound, InfoDTO("Приют не найден"))
            }
        }
    }
}
