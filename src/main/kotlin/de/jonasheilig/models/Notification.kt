package de.jonasheilig.models

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column

object Notifications : IntIdTable() {
    val apiKeyLink = reference("api_key_link_id", ApiKeyLinks)
    val message: Column<String> = text("message")
}

class Notification(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Notification>(Notifications)

    var apiKeyLink by ApiKeyLink referencedOn Notifications.apiKeyLink
    var message by Notifications.message
}
