package com.database.users

import kotlinx.serialization.Serializable

@Serializable
class UserDTO (
    val phone: String,
    val name: String,
    val surname: String,
    val login: String,
    val password: String,
    val role: String
)

@Serializable
data class EmailDTO(val email: String)

@Serializable
data class InfoDTO(val info: String)

@Serializable
data class PasswordDTO(
    val email: String,
    val password: String
)