package com.database.users

import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import javax.management.Query.and

fun Application.configureUpdateUserRouting() {
    routing {
        post("/updateuser") {
            val updateRequest = call.receive<UserDTO>()

            val rowsUpdated = transaction {
                Users.update({
                    (Users.login eq updateRequest.login) and
                        (Users.password eq updateRequest.password)
                }) {
                    it[name] = updateRequest.name
                    it[surname] = updateRequest.surname
                    it[phone] = updateRequest.phone
                }
            }

            if (rowsUpdated > 0) {
                call.respond(InfoDTO("Данные изменены"))
            } else {
                call.respond(InfoDTO("Ошибка изменения данных"))
            }
        }
    }
}
