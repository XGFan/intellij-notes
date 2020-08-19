import org.jetbrains.kotlin.backend.common.onlyIf

plugins {
    id("org.jetbrains.intellij") version "0.4.18"
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
    version = "LATEST-EAP-SNAPSHOT"
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
      shift + control + c 👉 Set bookmark with Descritpion <br>
      show bookmark description in the editor<br>"""
    )
    setSinceBuild("183")
}

tasks.register("buildForGithub") {
    group = "build"
    description = "build zip for github"
    dependsOn("buildPlugin")

    doLast {
        val distributions = this.project.buildDir.resolve("distributions")
        distributions.resolve("bookmarkplus.zip")
            .onlyIf({ exists() }, File::delete)
        distributions.listFiles { _, name ->
            name.contains(".zip")
        }?.first()?.copyTo(distributions.resolve("bookmarkplus.zip"))
    }
}
