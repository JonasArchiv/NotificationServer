package de.jonasheilig.models

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object Notification : IntIdTable() {
    val apiKeyLink = reference("api_key_link_id", ApiKeyLink)
    val message = text("message")
}

class Notification(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Notification>(Notification)

    var apiKeyLink by ApiKeyLink referencedOn Notification.apiKeyLink
    var message by Notification.message
}
