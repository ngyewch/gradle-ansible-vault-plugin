plugins {
    java
    id("com.github.ngyewch.ansible.vault")
}

repositories {
    mavenCentral()
}

ansibleVault {
    passwordFile = project.file("ansible-vault-password")

    val x = unmarshalYAML(project.file("test.yml"))
    println(x)
    println(x["aString"])
}
