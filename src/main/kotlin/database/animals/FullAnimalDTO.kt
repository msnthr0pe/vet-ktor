package com.database.animals

import com.database.nurseries.NurseriesDTO
import com.database.shelters.SheltersDTO
import kotlinx.serialization.Serializable

@Serializable
data class FullAnimalDTO(
    val id: String,
    val nickname: String,
    val species: String,
    val breed: String,
    val age: Int,
    val diseases: String? = null,
    val imageUrl: String? = null,
    val shelter: SheltersDTO? = null,
    val nursery: NurseriesDTO? = null,
)
