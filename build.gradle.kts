plugins {
    kotlin("jvm") version "1.5.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    java
}

group = "me.cookie"
version = "1.0"

repositories {
    mavenCentral()
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
    maven { url = uri("https://hub.jeff-media.com/nexus/repository/jeff-media-public/") }
    maven { url = uri("https://jitpack.io/")}
}

dependencies {
    compileOnly(kotlin("stdlib"))

    implementation("com.h2database:h2:2.0.204")
    implementation("net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT")
    implementation("com.github.retrooper:packetevents:2.0-SNAPSHOT")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.0")

    compileOnly("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    destinationDirectory.set(file("D:\\coding\\Test Servers\\TimeRewards\\plugins"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}


