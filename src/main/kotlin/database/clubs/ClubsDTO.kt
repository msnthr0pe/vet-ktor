package com.database.clubs

import kotlinx.serialization.Serializable

@Serializable
data class ClubsDTO(
    val address: String,
    val name: String,
    val phone: String,
    val description: String,
    val owner: String,
)
