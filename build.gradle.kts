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
    maven {
        url = uri("https://hub.jeff-media.com/nexus/repository/jeff-media-public")
    }
    maven {
        url = uri("https://nexus.bencodez.com/repository/maven-public/") // some guy's repo that has NuVotifier
        // Because the GitHub one doesnt work
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")
    implementation("com.h2database:h2:2.0.202")
    implementation("net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT")
    implementation("de.jeff_media:CustomBlockData:1.0.3")

    compileOnly("com.vexsoftware:nuvotifier-universal:2.7.2")
    compileOnly("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    destinationDirectory.set(file("/home/cookie/TestServers/sudoyou/plugins/"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}


java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}