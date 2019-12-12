plugins {
    id("org.jetbrains.intellij") version "0.4.15"
    kotlin("jvm") version "1.3.61"
}

group = "com.test4x"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = "2019.3"
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
    changeNotes(
        """
      Just Work.<br>
      <em>shift + control + c 👉 add note</em>
      <em>shift + control + x 👉 clear note</em>"""
    )
}