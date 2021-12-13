plugins {
    kotlin("jvm") version "1.5.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    java
}

group = "me.cookie"
version = "1.0"

repositories {
    mavenCentral()
    maven {
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
}

dependencies {
    compileOnly(kotlin("stdlib"))
    implementation("com.h2database:h2:2.0.202")
    implementation("net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT")

    compileOnly("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    destinationDirectory.set(file("D:\\coding\\Test Servers\\TimeRewards\\plugins"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}