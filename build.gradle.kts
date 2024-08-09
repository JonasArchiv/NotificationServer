plugins {
    kotlin("jvm") version "2.0.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"

}

group = "de.jonasheilig"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:2.3.2")
    implementation("io.ktor:ktor-server-netty:2.3.2")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.2")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.2")
    implementation("org.jetbrains.exposed:exposed-core:0.43.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.43.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.43.0")
    implementation("com.h2database:h2:2.1.214")
    implementation("ch.qos.logback:logback-classic:1.4.5")
    testImplementation("io.ktor:ktor-server-tests:2.3.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.0")
}

tasks.test {
    useJUnitPlatform()
}