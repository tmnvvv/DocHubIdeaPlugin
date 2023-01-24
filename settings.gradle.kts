pluginManagement {

    repositories {
        gradlePluginPortal()
    }

    val kotlinVersion: String by settings
    val gradleIntellijPluginVersion: String by settings
    val changelogPluginVersion: String by settings

    plugins {
        id("org.jetbrains.kotlin.jvm") version kotlinVersion
        id("org.jetbrains.intellij") version gradleIntellijPluginVersion
        id("org.jetbrains.changelog") version changelogPluginVersion
    }
}

rootProject.name = "IDEAPlugin"