plugins {
    `java-gradle-plugin`
    `maven-publish`
    id("ca.cutterslade.analyze") version "1.9.2"
    id("com.asarkar.gradle.build-time-tracker") version "4.3.0"
    id("com.autonomousapps.dependency-analysis") version "1.30.0"
    id("com.diffplug.spotless") version "6.25.0"
    id("com.github.ben-manes.versions") version "0.51.0"
    id("io.github.ngyewch.git-describe") version "0.2.0"
    id("com.gradle.plugin-publish") version "1.2.1"
    id("se.ascp.gradle.gradle-versions-filter") version "0.1.16"
}

group = "com.github.ngyewch.gradle"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(platform("com.fasterxml.jackson:jackson-bom:2.16.1"))
    testImplementation(platform("org.junit:junit-bom:5.10.2"))

    implementation(gradleApi())

    implementation("com.fasterxml.jackson.core:jackson-core")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-toml")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    implementation("commons-codec:commons-codec:1.16.1")
    implementation("commons-io:commons-io:2.15.1")
    implementation("de.rtner:PBKDF2:1.1.4")
    implementation("org.apache.commons:commons-lang3:3.14.0")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

repositories {
    mavenCentral()
}

tasks.named<Test>("test") {
    useJUnitPlatform()

    maxHeapSize = "1G"

    testLogging {
        events("passed", "failed", "skipped", "standard_out", "standard_error")
    }
}

gradlePlugin {
    website.set("https://github.com/ngyewch/gradle-ansible-vault-plugin")
    vcsUrl.set("https://github.com/ngyewch/gradle-ansible-vault-plugin.git")
    plugins {
        create("gradle-ansible-vault-plugin") {
            id = "com.github.ngyewch.ansible.vault"
            displayName = "Gradle Ansible Vault Plugin"
            description = "Gradle plugin for Ansible Vault."
            implementationClass = "com.github.ngyewch.gradle.ansible.vault.AnsibleVaultPlugin"
            tags.set(listOf("ansible", "ansible-vault"))
        }
    }
}

versionsFilter {
    gradleReleaseChannel.set("current")
    checkConstraints.set(true)
    outPutFormatter.set("json")
}

spotless {
    java {
        googleJavaFormat("1.19.2").reflowLongStrings().skipJavadocFormatting()
        formatAnnotations()
        targetExclude("build/**")
    }
}
