package com.database.nurseries

import com.database.users.InfoDTO
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureGetNurseryByAddressRouting() {
    routing {
        post("/getnurserybyaddress") {
            val input = call.receive<InfoDTO>()
            val nursery = NurseriesObject.fetchNursery(input.info)
            if (nursery != null) {
                call.respond(nursery)
            } else {
                call.respond(HttpStatusCode.NotFound, InfoDTO("Питомник не найден"))
            }
        }
    }
}
