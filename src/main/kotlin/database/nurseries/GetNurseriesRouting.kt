package com.database.nurseries

import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureGetNurseriesRouting() {
    routing {
        get("/getnurseries") {
            val news = transaction {
                NurseriesObject.selectAll().map { row ->
                    NurseriesDTO(
                        address = row[NurseriesObject.address],
                        name = row[NurseriesObject.name],
                        phone = row[NurseriesObject.phone],
                        description = row[NurseriesObject.description],
                        owner = row[NurseriesObject.owner]
                    )
                }
            }
            call.respond(news)
        }
    }
}