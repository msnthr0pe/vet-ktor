package com.database.news

import com.database.tokens.TokenDTO
import com.database.tokens.Tokens
import com.register.RegisterResponseRemote
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.util.UUID

class AddNewsController(val call: ApplicationCall) {
    suspend fun registerNews() {
        val addNewsObjectReceiveRemote = call.receive<NewsDTO>()
        val newsDTO = NewsObject.fetchNews(addNewsObjectReceiveRemote.title,
            addNewsObjectReceiveRemote.description)
        if (newsDTO != null) {
            call.respond(HttpStatusCode.Conflict, "News already exist")
        } else {
            val token = UUID.randomUUID().toString()
            try {
                NewsObject.insert(
                    NewsDTO(
                        title = addNewsObjectReceiveRemote.title,
                        description = addNewsObjectReceiveRemote.description
                    )
                )
            } catch (e: ExposedSQLException) {
                e.printStackTrace()
                call.respond(HttpStatusCode.Conflict, "SQL error: ${e.localizedMessage}")
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Unexpected error: ${e.localizedMessage}")
            }
            Tokens.insert(
                TokenDTO(
                    rowId = UUID.randomUUID().toString(),
                    login = addNewsObjectReceiveRemote.title,
                    token = token
                )
            )
            call.respond(RegisterResponseRemote(token = token))
        }
    }
}