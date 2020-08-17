import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.4.0"
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "h.code.country.AppKt"
    }
}

group = "h.code.country"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.sparkjava:spark-core:2.9.2")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }
}
tasks.withType<ShadowJar> {
    manifest {
        attributes["Main-Class"] = "h.code.country.AppKt"
    }
}
