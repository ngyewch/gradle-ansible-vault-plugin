import java.util.*

plugins {
    java
    id("io.github.ngyewch.ansible.vault")
}

repositories {
    mavenCentral()
}

ansibleVault {
    passwordFile = project.file("ansible-vault-password")

    val decryptedFile = decrypt(project.file("test.yml"))
    val x = decryptedFile.yaml().asMap()
    println(x)
    println(x["aString"])
    println(decryptedFile.asString())
    println(file(project.file("test.toml")).toml().asMap())

    if (!decrypt(project.file("test.yml")).asByteArray()
            .contentEquals(file(project.file("test-original.yml")).asByteArray())) {
        throw GradleException("content mismatch")
    }
}
