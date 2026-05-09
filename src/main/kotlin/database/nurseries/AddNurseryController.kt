package com.database.nurseries

import com.database.tokens.TokenDTO
import com.database.tokens.Tokens
import com.register.RegisterResponseRemote
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.util.UUID

class AddNurseryController(val call: ApplicationCall) {
    suspend fun addNursery() {
        val request = call.receive<NurseriesDTO>()
        val existing = NurseriesObject.fetchNursery(request.address)
        if (existing != null) {
            call.respond(HttpStatusCode.Conflict, "Nursery already exists")
            return
        }
        val token = UUID.randomUUID().toString()
        try {
            NurseriesObject.insert(
                NurseriesDTO(
                    address = request.address,
                    name = request.name,
                    phone = request.phone,
                    description = request.description,
                    owner = request.owner,
                    clubAddress = request.clubAddress,
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
