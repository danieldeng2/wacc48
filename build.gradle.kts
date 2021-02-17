import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
    id("com.adarshr.test-logger") version "2.1.1"
    application
}

group = "me.daniel"
version = "1.0-SNAPSHOT"

tasks.jar {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
}

repositories {
    mavenCentral()
    flatDir {
        dirs = setOf(file("lib"))
    }
}

dependencies {
    testImplementation(kotlin("test-junit"))
    implementation(fileTree(mapOf("dir" to "lib", "include" to listOf("*.jar"))))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

