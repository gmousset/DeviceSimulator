plugins {
    kotlin("jvm") version "1.9.22"
}

group = "com.github.gmousset"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.eclipse.leshan:leshan-client-cf:2.0.0-M14")
    implementation("org.eclipse.leshan:leshan-tl-javacoap-client:2.0.0-M14")
    implementation("org.apache.commons:commons-lang3:3.14.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(20)
}