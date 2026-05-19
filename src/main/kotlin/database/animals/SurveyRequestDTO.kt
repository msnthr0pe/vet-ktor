package com.database.animals

import kotlinx.serialization.Serializable

@Serializable
data class SurveyRequestDTO(
    val species: String,
    val breed: String,
    val age: Int,
    val willingToAdoptSick: Boolean,
    // допустимые значения: "breed", "health", "age"
    val mostImportant: String,
)
