package de.jonasheilig.models

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object ApiKeyLink : IntIdTable() {
    val senderKey = varchar("sender_key", 255)
    val receiverKey = varchar("receiver_key", 255)
}

class ApiKeyLink(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ApiKeyLink>(ApiKeyLink)

    var senderKey by ApiKeyLink.senderKey
    var receiverKey by ApiKeyLink.receiverKey
    val notifications by Notification referrersOn Notification.apiKeyLink
}
