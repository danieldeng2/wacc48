import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.serialization") version "1.4.21"
    id("com.adarshr.test-logger") version "2.1.1"
    application
    antlr
}

group = "wacc48"
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
}

dependencies {
    testImplementation(kotlin("test-junit"))
    antlr("org.antlr:antlr4:4.9.1")

    implementation("com.github.ajalt.clikt:clikt:3.1.0")
    implementation("org.apache.commons:commons-text:1.9")
    testImplementation("com.squareup.retrofit2:retrofit:2.9.0")
    testImplementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    testImplementation("com.squareup.moshi:moshi-kotlin:1.11.0")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.generateGrammarSource {
    maxHeapSize = "64m"
    arguments = arguments + listOf("-visitor", "-no-listener")
}

tasks.compileKotlin {
    dependsOn(tasks.generateGrammarSource)
}

tasks.compileTestKotlin {
    dependsOn(tasks.generateTestGrammarSource)
}