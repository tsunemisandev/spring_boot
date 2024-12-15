plugins {
    kotlin("jvm") version "1.9.23"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val ktor_version: String by project
dependencies {
    testImplementation(kotlin("test"))
    // https://mvnrepository.com/artifact/io.ktor/ktor-client-core
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}