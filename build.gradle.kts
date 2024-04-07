plugins {
    id("java")
}

group = "com.ocado.basket"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.google.guava:guava:33.1.0-jre")
    implementation("org.javatuples:javatuples:1.2")
}

tasks.test {
    useJUnitPlatform()
}