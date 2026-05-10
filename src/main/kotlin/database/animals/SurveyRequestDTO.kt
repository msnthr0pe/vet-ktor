package com.database.animals

import kotlinx.serialization.Serializable

@Serializable
data class SurveyRequestDTO(
    val species: String,
    val breed: String,
    val willingToAdoptSick: Boolean,
    // допустимые значения: "species", "breed", "health"
    val mostImportant: String,
)
