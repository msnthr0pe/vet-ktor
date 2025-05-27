package com.database.users

import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureGetUserRouting() {
    routing {
        post("/getuser") {
            val request = call.receive<TextDTO>()
            val email = request.email
            try {
                val result: UserDTO? = transaction {
                    Users
                        .select { Users.login eq email }
                        .singleOrNull()
                        ?.let { row ->
                            UserDTO(
                                phone = row[Users.phone],
                                surname = row[Users.surname],
                                name = row[Users.name],
                                login = row[Users.login],
                                password = row[Users.password],
                                role = row[Users.role]
                            )
                        }
                }
                call.respond(result ?: "User Not found")
            } catch (e: Exception) {
                e.printStackTrace()
                call.respondText("Server error", status = io.ktor.http.HttpStatusCode.InternalServerError)
            }
        }
    }
}
