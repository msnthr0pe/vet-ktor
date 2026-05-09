package com.database.clubs

import com.database.users.InfoDTO
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureGetClubByOwnerRouting() {
    routing {
        post("/getclubby") {
            val input = call.receive<InfoDTO>()
            val clubs = transaction {
                ClubsObject.select { ClubsObject.owner eq input.info }
                    .map {
                        ClubsDTO(
                            address = it[ClubsObject.address],
                            name = it[ClubsObject.name],
                            phone = it[ClubsObject.phone],
                            description = it[ClubsObject.description],
                            owner = it[ClubsObject.owner],
                        )
                    }
            }
            call.respond(clubs)
        }
    }
}
