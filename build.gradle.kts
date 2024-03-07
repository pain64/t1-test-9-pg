import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.22"
	kotlin("plugin.spring") version "1.9.22"
	kotlin("plugin.jpa") version "1.9.22"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
	implementation("org.springdoc:springdoc-openapi-starter-common:2.2.0")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.3")
	runtimeOnly("org.hsqldb:hsqldb:2.7.1")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	implementation("org.flywaydb:flyway-core:10.9.0")
	runtimeOnly("org.flywaydb:flyway-database-postgresql:10.9.0")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.3")
	testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
	testImplementation("io.kotest:kotest-assertions-core:5.8.0")

	testImplementation("io.zonky.test:embedded-postgres:2.0.6")
	testImplementation("io.zonky.test.postgres:embedded-postgres-binaries-bom:16.1.0")
	testImplementation("io.zonky.test.postgres:embedded-postgres-binaries-linux-amd64:16.1.0")
	testImplementation("io.zonky.test.postgres:embedded-postgres-binaries-windows-amd64:16.1.0")
	testImplementation("io.zonky.test.postgres:embedded-postgres-binaries-darwin-amd64:16.1.0")
	testImplementation("io.zonky.test.postgres:embedded-postgres-binaries-darwin-arm64v8:16.1.0")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
