package com.database.shelters

import com.database.users.InfoDTO
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureGetShelterByOwnerRouting() {
    routing {
        post("/getshelterby") {
            val input = call.receive<InfoDTO>()
            val shelters = transaction {
                SheltersObject.select { SheltersObject.owner eq input.info }.orderBy(SheltersObject.createdAt, SortOrder.DESC)
                    .map {
                        SheltersDTO(
                            address = it[SheltersObject.address],
                            name = it[SheltersObject.name],
                            phone = it[SheltersObject.phone],
                            description = it[SheltersObject.description],
                            owner = it[SheltersObject.owner],
                            clubAddress = it[SheltersObject.clubAddress],
                        )
                    }
            }
            call.respond(shelters)
        }
    }
}
