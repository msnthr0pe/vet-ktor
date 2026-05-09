package com

import com.database.animals.configureAddAnimalRouting
import com.database.animals.configureDeleteAnimalRouting
import com.database.animals.configureGetAnimalFullRouting
import com.database.animals.configureGetAnimalRouting
import com.database.animals.configureGetAnimalsByNurseryRouting
import com.database.animals.configureGetAnimalsByShelterRouting
import com.database.animals.configureGetAnimalsRouting
import com.database.animals.configureUpdateAnimalRouting
import com.database.clubs.configureAddClubRouting
import com.database.clubs.configureDeleteClubRouting
import com.database.clubs.configureGetClubByOwnerRouting
import com.database.clubs.configureGetClubsRouting
import com.database.clubs.configureUpdateClubRouting
import com.database.news.configureAddNewsRouting
import com.database.news.configureGetNewsRouting
import com.database.nurseries.configureAddNurseryRouting
import com.database.nurseries.configureGetNurseriesRouting
import com.database.nurseries.configureGetNurseryByOwnerRouting
import com.database.nurseries.configureUpdateNurseryRouting
import com.database.shelters.configureAddShelterRouting
import com.database.shelters.configureGetShelterByOwnerRouting
import com.database.shelters.configureGetSheltersRouting
import com.database.shelters.configureUpdateShelterRouting
import com.database.users.configureGetUserRouting
import com.database.users.configureUpdateUserRouting
import com.login.configureLoginRouting
import com.register.configureRegisterRouting
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.jetbrains.exposed.sql.Database

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)

}

fun Application.module() {
    Database.connect("jdbc:postgresql://localhost:5432/vetuslugi",
        "org.postgresql.Driver",
        "postgres",
        "root")

    configureAddNewsRouting()
    configureGetNewsRouting()

    configureAddShelterRouting()
    configureGetSheltersRouting()

    configureAddNurseryRouting()
    configureGetNurseriesRouting()

    configureUpdateShelterRouting()
    configureUpdateNurseryRouting()

    configureGetShelterByOwnerRouting()
    configureGetNurseryByOwnerRouting()

    configureAddClubRouting()
    configureGetClubsRouting()
    configureGetClubByOwnerRouting()
    configureUpdateClubRouting()
    configureDeleteClubRouting()

    configureAddAnimalRouting()
    configureGetAnimalsRouting()
    configureGetAnimalsByShelterRouting()
    configureGetAnimalsByNurseryRouting()
    configureGetAnimalRouting()
    configureGetAnimalFullRouting()
    configureUpdateAnimalRouting()
    configureDeleteAnimalRouting()

    configureRegisterRouting()
    configureLoginRouting()

    configureGetUserRouting()
    configureUpdateUserRouting()

    configureSerialization()
    configureRouting()

}
