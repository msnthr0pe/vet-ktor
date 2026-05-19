package com.database.animals

import com.database.animals.AnimalsObject.toDTO
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

private val speciesSynonyms = listOf(
    setOf("кошка", "кот"),
    setOf("пёс", "собака", "пес"),
)

private fun normalizeSpecies(species: String): String {
    val lower = species.lowercase()
    return speciesSynonyms.firstOrNull { lower in it }?.first() ?: lower
}

class SurveyController(val call: ApplicationCall) {

    suspend fun survey() {
        val request = call.receive<SurveyRequestDTO>()

        val (breedWeight, healthWeight, ageWeight) = when (request.mostImportant) {
            "breed"  -> Triple(0.6, 0.2, 0.2)
            "health" -> Triple(0.2, 0.6, 0.2)
            "age"    -> Triple(0.2, 0.2, 0.6)
            else -> {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "mostImportant должен быть одним из: breed, health, age"
                )
                return
            }
        }

        // Жёсткий фильтр по виду
        val normalizedRequestSpecies = normalizeSpecies(request.species)
        val animals = transaction {
            AnimalsObject.selectAll()
                .orderBy(AnimalsObject.createdAt, SortOrder.DESC)
                .map { it.toDTO() }
                .filter { normalizeSpecies(it.species) == normalizedRequestSpecies }
        }

        // Группа запрошенной породы — вычисляем один раз
        val requestedGroupId = BreedGroupsObject.findGroupId(request.breed, request.species)

        val result = animals
            .map { animal ->
                val breedScore  = calcBreedScore(animal, request.breed, requestedGroupId, breedWeight)
                val healthScore = calcHealthScore(animal.diseaseSeverity, request.willingToAdoptSick, healthWeight)
                val ageScore    = calcAgeScore(animal.age, request.age, ageWeight)
                AnimalMatchDTO(
                    animal      = animal,
                    coefficient = breedScore + healthScore + ageScore
                )
            }
            .sortedByDescending { it.coefficient }

        call.respond(result)
    }

    // ── Порода ──────────────────────────────────────────────────────────────
    // Точное совпадение            → breedWeight       (1.0)
    // Та же группа                 → breedWeight * 0.8 (похожая порода)
    // Другая группа / нет в таблице → breedWeight * 0.1
    private fun calcBreedScore(
        animal: AnimalDTO,
        requestedBreed: String,
        requestedGroupId: Int?,
        breedWeight: Double
    ): Double {
        if (animal.breed.lowercase() == requestedBreed.lowercase()) {
            return breedWeight
        }
        val animalGroupId = BreedGroupsObject.findGroupId(animal.breed, animal.species)
        return if (requestedGroupId != null && animalGroupId != null && requestedGroupId == animalGroupId)
            breedWeight * 0.8
        else
            breedWeight * 0.1
    }

    // ── Возраст ──────────────────────────────────────────────────────────────
    // A = max(0, 1 - 0.2 × |age_a - age_r|)
    // Каждый год разницы снижает score на 0.2; при разнице ≥ 5 лет → 0
    private fun calcAgeScore(animalAge: Int, requestedAge: Int, ageWeight: Double): Double {
        val diff = Math.abs(animalAge - requestedAge)
        return ageWeight * maxOf(0.0, 1.0 - 0.2 * diff)
    }

    // ── Здоровье ─────────────────────────────────────────────────────────────
    // willingToAdoptSick = false:
    //   severity 0         → healthWeight      (только здоровые)
    //   severity 1/2/3     → 0                 (больные исключены)
    // willingToAdoptSick = true:
    //   severity 0         → healthWeight * 1.0
    //   severity 1 (лёгкое) → healthWeight * 0.8
    //   severity 2 (среднее) → healthWeight * 0.5
    //   severity 3 (тяжёлое) → healthWeight * 0.2
    private fun calcHealthScore(
        severity: Int,
        willingToAdoptSick: Boolean,
        healthWeight: Double
    ): Double {
        if (!willingToAdoptSick) {
            return if (severity == 0) healthWeight else 0.0
        }
        return when (severity) {
            0    -> healthWeight * 1.0
            1    -> healthWeight * 0.8
            2    -> healthWeight * 0.5
            3    -> healthWeight * 0.2
            else -> 0.0
        }
    }
}
