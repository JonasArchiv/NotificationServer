package de.jonasheilig

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import de.jonasheilig.models.ApiKeyLinks
import de.jonasheilig.models.Notifications

fun initDatabase() {
    Database.connect("jdbc:h2:./notification_db", driver = "org.h2.Driver")
    transaction {
        SchemaUtils.create(ApiKeyLinks, Notifications)
    }
}
