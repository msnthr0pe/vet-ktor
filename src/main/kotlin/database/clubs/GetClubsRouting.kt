package com.database.clubs

import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureGetClubsRouting() {
    routing {
        get("/getclubs") {
            val clubs = transaction {
                ClubsObject.selectAll().orderBy(ClubsObject.createdAt, SortOrder.DESC).map { row ->
                    ClubsDTO(
                        address = row[ClubsObject.address],
                        name = row[ClubsObject.name],
                        phone = row[ClubsObject.phone],
                        description = row[ClubsObject.description],
                        owner = row[ClubsObject.owner],
                    )
                }
            }
            call.respond(clubs)
        }
    }
}
