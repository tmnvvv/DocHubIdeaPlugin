pluginManagement {

    repositories {
        gradlePluginPortal()
    }

    val kotlinVersion: String by settings
    val gradleIntellijPluginVersion: String by settings
    val grammarKitVersion: String by settings

    plugins {
        id("org.jetbrains.kotlin.jvm") version kotlinVersion
        id("org.jetbrains.intellij.platform") version gradleIntellijPluginVersion
        id("org.jetbrains.grammarkit") version grammarKitVersion
    }
}

rootProject.name = "IDEAPlugin"