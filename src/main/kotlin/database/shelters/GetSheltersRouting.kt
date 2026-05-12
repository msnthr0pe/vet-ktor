package com.database.shelters

import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureGetSheltersRouting() {
    routing {
        get("/getshelters") {
            val shelters = transaction {
                SheltersObject.selectAll().orderBy(SheltersObject.createdAt, SortOrder.DESC).map { row ->
                    SheltersDTO(
                        address = row[SheltersObject.address],
                        name = row[SheltersObject.name],
                        phone = row[SheltersObject.phone],
                        description = row[SheltersObject.description],
                        owner = row[SheltersObject.owner],
                        clubAddress = row[SheltersObject.clubAddress],
                    )
                }
            }
            call.respond(shelters)
        }
    }
}
