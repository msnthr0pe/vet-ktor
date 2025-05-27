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
        val addNurseriesObjectReceiveRemote = call.receive<NurseriesDTO>()
        val nurseriesDTO = NurseriesObject.fetchNursery(addNurseriesObjectReceiveRemote.address)
        if (nurseriesDTO != null) {
            call.respond(HttpStatusCode.Conflict, "Nursery already exists")
        } else {
            val token = UUID.randomUUID().toString()
            try {
                NurseriesObject.insert(
                    NurseriesDTO(
                        address = addNurseriesObjectReceiveRemote.address,
                        name = addNurseriesObjectReceiveRemote.name,
                        phone = addNurseriesObjectReceiveRemote.phone,
                        description = addNurseriesObjectReceiveRemote.description,
                        owner = addNurseriesObjectReceiveRemote.owner,
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
                    login = addNurseriesObjectReceiveRemote.address,
                    token = token
                )
            )
            call.respond(RegisterResponseRemote(token = token))
        }
    }
}