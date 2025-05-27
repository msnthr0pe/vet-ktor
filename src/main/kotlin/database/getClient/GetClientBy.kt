package com.database.getClient

import com.database.clients.ClientDTO
import com.database.clients.ClientByDTO
import com.database.clients.Clients
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureGetClientByNameRouting() {
    routing {
        post("/getclientby") {
            val input = call.receive<ClientByDTO>()
            if (input.mode == "name") {
                val clients = transaction {
                    Clients.select() { Clients.name eq input.name }
                        .map {
                            ClientDTO(
                                phone = it[Clients.phone],
                                date = it[Clients.date],
                                name = it[Clients.name],
                                email = it[Clients.email],
                                time = it[Clients.time],
                                description = it[Clients.description],
                            )
                        }
                }
                call.respond(clients)
            } else {
                val clients = transaction {
                    Clients.select() { Clients.date eq input.date }
                        .map {
                            ClientDTO(
                                phone = it[Clients.phone],
                                date = it[Clients.date],
                                name = it[Clients.name],
                                email = it[Clients.email],
                                time = it[Clients.time],
                                description = it[Clients.description],
                            )
                        }
                }
                call.respond(clients)
            }
        }
    }
}