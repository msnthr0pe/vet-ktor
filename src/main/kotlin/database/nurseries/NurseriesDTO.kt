package com.database.nurseries

import kotlinx.serialization.Serializable

@Serializable
data class NurseriesDTO (
    val address: String,
    val name: String,
    val phone: String,
    val description: String,
    val owner: String,
    )