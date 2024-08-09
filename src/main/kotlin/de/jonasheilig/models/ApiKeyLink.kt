package de.jonasheilig.models

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column

object ApiKeyLinks : IntIdTable() {
    val senderKey: Column<String> = varchar("sender_key", 255)
    val receiverKey: Column<String> = varchar("receiver_key", 255)
}

class ApiKeyLink(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ApiKeyLink>(ApiKeyLinks)

    var senderKey by ApiKeyLinks.senderKey
    var receiverKey by ApiKeyLinks.receiverKey
    val notifications by Notification referrersOn Notifications.apiKeyLink
}
