package com.database.clients

import kotlinx.serialization.Serializable

@Serializable
data class ClientDTO (
    val phone: String,
    val date: String,
    val name: String,
    val time: String,
    val email: String,
    val description: String
)

@Serializable
data class ClientByDTO (
    val name: String,
    val date: String,
    val mode: String
)