package com.database.animals

import kotlinx.serialization.Serializable

@Serializable
data class AnimalDTO(
    val id: String,
    val nickname: String,
    val species: String,
    val breed: String,
    val age: Int,
    val diseases: String? = null,
    val imageUrl: String? = null,
    val shelterAddress: String? = null,
    val nurseryAddress: String? = null,
)
