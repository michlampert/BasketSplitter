buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("gradle.plugin.com.github.johnrengelman:shadow:7.1.2")
    }
}

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("java")
}

java {
    withSourcesJar()
}

group = "com.ocado.basket"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.google.guava:guava:33.1.0-jre")
    implementation("org.javatuples:javatuples:1.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.2")
    implementation("org.apache.logging.log4j:log4j-core:2.23.1")
}

tasks.test {
    useJUnitPlatform()
}