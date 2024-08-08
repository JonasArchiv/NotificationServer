package de.jonasheilig

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import de.jonasheilig.models.ApiKeyLink
import de.jonasheilig.models.Notification

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

                transaction {
                    val linkedKey = ApiKeyLink.find { ApiKeyLink.senderKey eq notification.apiKey }
                        .singleOrNull()

                    if (linkedKey != null) {
                        Notification.new {
                            apiKeyLink = linkedKey
                            message = notification.message
                        }
                        call.respondText("Notification stored", status = HttpStatusCode.OK)
                    } else {
                        call.respondText("API Key not found", status = HttpStatusCode.BadRequest)
                    }
                }
            }

            get("/notify/{apiKey}") {
                val apiKey = call.parameters["apiKey"]

                val notificationsList = transaction {
                    val linkedKey = ApiKeyLink.find { ApiKeyLink.receiverKey eq apiKey }
                        .singleOrNull()

                    linkedKey?.notifications?.map { it.message } ?: emptyList()
                }

                call.respond(HttpStatusCode.OK, notificationsList)
            }
        }
    }.start(wait = true)
}
