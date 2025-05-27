package com.database.news

import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureGetNewsRouting() {
    routing {
        get("/getnews") {
            val news = transaction {
                NewsObject.selectAll().map { row ->
                    NewsDTO(
                        title = row[NewsObject.title],
                        description = row[NewsObject.description]
                    )
                }
            }
            call.respond(news)
        }
    }
}