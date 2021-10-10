import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.21"
    kotlin("plugin.spring") version "1.5.21"
    kotlin("plugin.jpa") version "1.5.21"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    id("org.jlleitschuh.gradle.ktlint-idea") version "10.2.0"
}

group = "io.mvlchain"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.web3j:core:4.8.4")
    implementation("org.apache.commons:commons-lang3:3.11")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("commons-codec:commons-codec:1.15")
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.3.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.3.1")
    // implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.7.2")
    // https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-scalars
    implementation("com.squareup.retrofit2:converter-scalars:2.7.2")
    implementation("com.fasterxml.jackson.core:jackson-core:2.12.5")
    // https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-jackson
    implementation("com.squareup.retrofit2:converter-jackson:2.7.2")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.5")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.datatype/jackson-datatype-joda
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-joda:2.12.5")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-annotations
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.12.5")
    // https://mvnrepository.com/artifact/com.squareup.retrofit2/retrofit
    // https://mvnrepository.com/artifact/com.google.guava/guava
    implementation("com.google.guava:guava:30.1.1-jre")
    // https://mvnrepository.com/artifact/com.google.protobuf/protobuf-java
    implementation("com.google.protobuf:protobuf-java:4.0.0-rc-2")
    implementation("org.bitcoinj:bitcoinj-core:0.15.10")
}

tasks {
    compileKotlin {
        dependsOn(addKtlintFormatGitPreCommitHook)
    }
}
tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

ktlint {
    version.set("0.40.0")
}
