plugins {
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
	kotlin("jvm") version "2.0.20"
	kotlin("plugin.spring") version "2.0.20"
	kotlin("plugin.jpa") version "2.0.20"

	kotlin("plugin.serialization") version "2.0.20"

}

val ktorVersion = "2.3.10"

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")

	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")


	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("mysql:mysql-connector-java:8.0.33")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

	implementation("io.ktor:ktor-client-core:${ktorVersion}")
	implementation("io.ktor:ktor-client-cio:${ktorVersion}")
// or ktor-client-okhttp for Android
	implementation("io.ktor:ktor-client-content-negotiation:${ktorVersion}")
	implementation("io.ktor:ktor-serialization-kotlinx-json:${ktorVersion}")
	implementation("io.ktor:ktor-client-logging:${ktorVersion}")

	//flightService
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.1")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.1")

	implementation("org.springframework.boot:spring-boot-starter")

	implementation("org.seleniumhq.selenium:selenium-java:4.19.1")
	implementation("org.seleniumhq.selenium:selenium-chrome-driver:4.19.1")

	implementation("io.github.bonigarcia:webdrivermanager:5.8.0")

	implementation("com.github.haifengl:smile-core:2.6.0")
// Alternative ML library for Java

	implementation("org.jsoup:jsoup:1.17.2")

	//web scraping
	

}



tasks.withType<Test> {
	useJUnitPlatform()
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

