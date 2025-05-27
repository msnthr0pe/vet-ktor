package com.database.addClient

import com.database.clients.ClientDTO
import com.database.clients.Clients
import com.database.tokens.TokenDTO
import com.database.tokens.Tokens
import com.database.users.UserDTO
import com.database.users.Users
import com.register.RegisterResponseRemote
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.util.UUID

class AddClientController(val call: ApplicationCall) {
    suspend fun registerNewClient() {
        val addClientReceiveRemote = call.receive<ClientDTO>()
        val clientDTO = Clients.fetchClient(addClientReceiveRemote.date,
            addClientReceiveRemote.phone)
        if (clientDTO != null) {
            call.respond(HttpStatusCode.Conflict, "Request already exists")
        } else {
            val token = UUID.randomUUID().toString()
            try {
                Clients.insert(
                    ClientDTO(
                        date = addClientReceiveRemote.date,
                        phone = addClientReceiveRemote.phone,
                        name = addClientReceiveRemote.name,
                        email = addClientReceiveRemote.email,
                        time = addClientReceiveRemote.time,
                        description = addClientReceiveRemote.description
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
                    login = addClientReceiveRemote.phone,
                    token = token
                )
            )
            call.respond(RegisterResponseRemote(token = token))
        }
    }
}