plugins {
    kotlin("jvm") version "1.6.0"
    id("com.github.johnrengelman.shadow") version "7.1.0"
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
    compileOnly(kotlin("stdlib", "1.6.0"))
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("com.h2database:h2:2.0.204")
    implementation("net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT")
    implementation("com.github.retrooper:packetevents:0056ada")

    compileOnly("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")
}

java {
    withSourcesJar()
    withJavadocJar()

    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks{
    shadowJar{
        archiveClassifier.set("")
        project.configurations.implementation.get().isCanBeResolved = true
        configurations = listOf(project.configurations.implementation.get())
        destinationDirectory.set(file("D:\\coding\\Test Servers\\TimeRewards\\plugins"))
    }
}