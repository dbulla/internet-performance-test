import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/7.1.1/userguide/building_java_projects.html
 */

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    // id("org.jetbrains.kotlin.jvm") version "1.7.20"

    // Apply the application plugin to add support for building a CLI application in Java.
    application

    id("com.github.ben-manes.versions") version "0.44.0"
    id("com.dorongold.task-tree") version "2.1.0"
    kotlin("jvm") version "1.8.0"
    id("org.springframework.boot") version "3.0.1"
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

var KOTEST_VERSION = "5.5.4"
dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:31.1-jre")
    implementation("commons-io:commons-io:2.11.0")

    // Use the Kotlin test library.
//    testImplementation("org.jetbrains.kotlin:kotlin-test")
    // Use the Kotlin JUnit integration.
//    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    // newer stuff
    testImplementation ("io.kotest:kotest-runner-junit5:$KOTEST_VERSION")
    testImplementation ("io.kotest:kotest-assertions-core:$KOTEST_VERSION")
    testImplementation ("io.kotest:kotest-property:$KOTEST_VERSION")
}

application {
    // Define the main class for the application.
    mainClass.set("com.nurflugel.internetTest.Monitor")
    group = "com.nurflugel"
    version = "0.0.1-SNAPSHOT"
}

//tasks.withType<Test> {
//    useJUnitPlatform()
//}

kotlin {
    jvmToolchain(17) // "8"
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.withType<BootJar>{
    mainClass.set("com.nurflugel.internetTest.Monitor")
    launchScript()
}