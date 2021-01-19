plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.allopen") version "1.4.10"
    kotlin("kapt") version "1.4.10"
    id("io.quarkus")
}

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://dl.bintray.com/oberstrike/maven")
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation("org.mapstruct:mapstruct:1.4.1.Final")
    kapt("org.mapstruct:mapstruct-processor:1.4.1.Final")
    implementation("com.maju.openapi:oas-generator:1.0.0")
    kapt("com.maju.openapi:oas-generator:1.0.0")
    implementation("com.maju.proxy:proxy-generator:1.0.5")
    kapt("com.maju.proxy:proxy-generator:1.0.5")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.0")

    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-keycloak-authorization")
    implementation("io.quarkus:quarkus-hibernate-orm-panache-kotlin")
    implementation("io.quarkus:quarkus-hibernate-validator")
    implementation("io.quarkus:quarkus-smallrye-openapi")
    implementation("io.quarkus:quarkus-resteasy-multipart")
    implementation("io.quarkus:quarkus-oidc")
    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkus:quarkus-jackson")
    implementation("io.quarkus:quarkus-scheduler")
    implementation("io.quarkus:quarkus-resteasy-jackson")
    implementation("io.quarkus:quarkus-jdbc-postgresql")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.10")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.10")
    implementation("io.quarkus:quarkus-flyway")

    implementation("io.quarkus:quarkus-undertow")
    implementation("org.jadira.usertype:usertype.core:7.0.0.CR1")

    implementation("io.quarkus:quarkus-resteasy")
    testImplementation("io.quarkus:quarkus-test-security")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation("io.quarkus:quarkus-panache-mock")
    testImplementation("io.rest-assured:kotlin-extensions")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("io.quarkus:quarkus-junit5-mockito")
    testImplementation("org.testcontainers:testcontainers:1.15.1")
    testImplementation("org.testcontainers:junit-jupiter:1.15.1")
    testImplementation("com.github.dasniko:testcontainers-keycloak:1.3.3")
    testImplementation("org.testcontainers:postgresql:1.15.1")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("io.quarkus:quarkus-keycloak-admin-client")

    val junitJupiterVersion = "5.4.2"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitJupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")


}

group = "de.maju"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

allOpen {
    annotation("javax.ws.rs.Path")
    annotation("javax.enterprise.context.ApplicationScoped")
    annotation("io.quarkus.test.junit.QuarkusTest")
    annotation("javax.persistence.Entity")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
    kotlinOptions.javaParameters = true
}
