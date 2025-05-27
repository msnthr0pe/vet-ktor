package com.database.nurseries

import com.database.users.InfoDTO
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureGetNurseryByOwnerRouting() {
    routing {
        post("/getnurseryby") {
            val input = call.receive<InfoDTO>()
            val nurseries = transaction {
                NurseriesObject.select() { NurseriesObject.owner eq input.info }
                    .map {
                        NurseriesDTO(
                            address = it[NurseriesObject.address],
                            name = it[NurseriesObject.name],
                            phone = it[NurseriesObject.phone],
                            description = it[NurseriesObject.description],
                            owner = it[NurseriesObject.owner],
                        )
                    }
            }
            call.respond(nurseries)
        }
    }
}