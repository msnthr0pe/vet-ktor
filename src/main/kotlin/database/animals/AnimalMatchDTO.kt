package com.database.animals

import kotlinx.serialization.Serializable

@Serializable
data class AnimalMatchDTO(
    val animal: AnimalDTO,
    val coefficient: Double,
)
