package com.database.shelters

import com.database.tokens.TokenDTO
import com.database.tokens.Tokens
import com.register.RegisterResponseRemote
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.util.UUID

class AddShelterController(val call: ApplicationCall) {
    suspend fun addShelter() {
        val addSheltersObjectReceiveRemote = call.receive<SheltersDTO>()
        val sheltersDTO = SheltersObject.fetchShelter(addSheltersObjectReceiveRemote.address)
        if (sheltersDTO != null) {
            call.respond(HttpStatusCode.Conflict, "News already exist")
        } else {
            val token = UUID.randomUUID().toString()
            try {
                SheltersObject.insert(
                    SheltersDTO(
                        address = addSheltersObjectReceiveRemote.address,
                        name = addSheltersObjectReceiveRemote.name,
                        phone = addSheltersObjectReceiveRemote.phone,
                        description = addSheltersObjectReceiveRemote.description,
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
                    login = addSheltersObjectReceiveRemote.address,
                    token = token
                )
            )
            call.respond(RegisterResponseRemote(token = token))
        }
    }
}