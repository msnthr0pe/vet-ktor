package com.database.shelters

import kotlinx.serialization.Serializable

@Serializable
data class SheltersDTO (
    val address: String,
    val name: String,
    val phone: String,
    val description: String,
    val owner: String,
    )