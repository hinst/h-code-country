plugins {
    kotlin("jvm") version "1.4.0"
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
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }
}