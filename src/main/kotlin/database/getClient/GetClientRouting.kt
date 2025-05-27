package com.database.getClient

import com.database.clients.ClientDTO
import com.database.clients.Clients
import com.login.LoginController
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureGetClientRouting() {
    routing {
        get("/getclients") {
            val clients = transaction {
                Clients.selectAll().map { row ->
                    ClientDTO(
                        phone = row[Clients.phone],
                        date = row[Clients.date],
                        name = row[Clients.name],
                        email = row[Clients.email],
                        time = row[Clients.time],
                        description = row[Clients.description],
                    )
                }
            }
            call.respond(clients)
        }
    }
}