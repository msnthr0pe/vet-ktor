package com.database.animals

import com.database.animals.AnimalsObject.toDTO
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureSurveyRouting() {
    routing {
        // Основной эндпоинт опроса
        post("/survey") {
            SurveyController(call).survey()
        }

        // Вспомогательные: получить доступные виды и породы для формы опроса
        get("/surveyspecies") {
            val species = transaction {
                AnimalsObject
                    .slice(AnimalsObject.species)
                    .selectAll()
                    .withDistinct()
                    .map { it[AnimalsObject.species] }
            }
            call.respond(species)
        }

        get("/surveybreeds") {
            val breeds = transaction {
                AnimalsObject
                    .slice(AnimalsObject.breed)
                    .selectAll()
                    .withDistinct()
                    .map { it[AnimalsObject.breed] }
            }
            call.respond(breeds)
        }
    }
}
