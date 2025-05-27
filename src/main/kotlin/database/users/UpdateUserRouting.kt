package com.database.users

import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun Application.configureUpdateUserRouting() {
    routing {
        post("/updateuser") {
            val updateRequest = call.receive<PasswordDTO>()

            val rowsUpdated = transaction {
                Users.update({ Users.login eq updateRequest.email }) {
                    it[password] = updateRequest.password
                }
            }

            if (rowsUpdated > 0) {
                call.respond(EmailDTO("Пароль изменён"))
            } else {
                call.respond(EmailDTO("Пользователь не найден"))
            }
        }
    }
}
