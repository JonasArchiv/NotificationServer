package de.jonasheilig

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import de.jonasheilig.models.ApiKeyLinks
import de.jonasheilig.models.Notifications
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Serializable
data class NotificationRequest(val apiKey: String, val message: String)

fun main() {
    initDatabase()

    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json()
        }
        routing {
            post("/sendnotification") {
                val notification = call.receive<NotificationRequest>()

                withContext(Dispatchers.IO) {
                    transaction {
                        val linkedKey = ApiKeyLinks.find { ApiKeyLinks.senderKey eq notification.apiKey }
                            .singleOrNull()

                        if (linkedKey != null) {
                            Notifications.new {
                                apiKeyLink = linkedKey
                                message = notification.message
                            }
                            call.respondText("Notification stored", status = HttpStatusCode.OK)
                        } else {
                            call.respondText("API Key not found", status = HttpStatusCode.BadRequest)
                        }
                    }
                }
            }

            get("/notify/{apiKey}") {
                val apiKey = call.parameters["apiKey"]

                val notificationsList = withContext(Dispatchers.IO) {
                    transaction {
                        val linkedKey = ApiKeyLinks.find { ApiKeyLinks.receiverKey eq apiKey }
                            .singleOrNull()

                        linkedKey?.notifications?.map { it.message } ?: emptyList()
                    }
                }

                call.respond(HttpStatusCode.OK, notificationsList)
            }
        }
    }.start(wait = true)
}
