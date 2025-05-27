package com.database.news

import kotlinx.serialization.Serializable

@Serializable
data class NewsDTO (
    val title: String,
    val description: String
)