plugins {
    kotlin("jvm") version "2.1.0"
    application
}

group = "se.brainleech"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("stdlib"))
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("MainKt")
}

kotlin {
    testing {
        compilerOptions {
            jvmToolchain(21)
        }
    }
    compilerOptions {
        jvmToolchain(21)
    }
}