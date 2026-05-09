package com.database.clubs

import com.database.tokens.TokenDTO
import com.database.tokens.Tokens
import com.register.RegisterResponseRemote
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.util.UUID

class AddClubController(val call: ApplicationCall) {
    suspend fun addClub() {
        val request = call.receive<ClubsDTO>()
        val existing = ClubsObject.fetchClub(request.address)
        if (existing != null) {
            call.respond(HttpStatusCode.Conflict, "Club already exists")
            return
        }
        val token = UUID.randomUUID().toString()
        try {
            ClubsObject.insert(
                ClubsDTO(
                    address = request.address,
                    name = request.name,
                    phone = request.phone,
                    description = request.description,
                    owner = request.owner,
                )
            )
        } catch (e: ExposedSQLException) {
            e.printStackTrace()
            call.respond(HttpStatusCode.Conflict, "SQL error: ${e.localizedMessage}")
            return
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, "Unexpected error: ${e.localizedMessage}")
            return
        }
        Tokens.insert(
            TokenDTO(
                rowId = UUID.randomUUID().toString(),
                login = request.address,
                token = token
            )
        )
        call.respond(RegisterResponseRemote(token = token))
    }
}
