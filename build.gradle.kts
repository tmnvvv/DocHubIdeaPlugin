import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.changelog.markdownToHTML

val javaVersion = JavaVersion.VERSION_17

val pluginRepositoryUrl: String by project
val junitVersion: String by project
val JSONataVersion: String by project
val platformVersion: String by project
val platformType: String by project
val platformPlugins: String by project
val pluginSinceBuild: String by project
val pluginUntilBuild: String by project


plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.intellij")
    id("org.jetbrains.changelog")
}


dependencies {

    /**
     * Внешние зависимости
     */
    implementation("com.ibm.jsonata4java:JSONata4Java:$JSONataVersion")
    implementation(files("libs/elk-full.jar", "libs/plantuml.jar"))

    /**
     * Тестовые зависимости
     */
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

repositories {
    mavenCentral()
}


intellij {
    version.set(platformVersion)
    type.set(platformType)
    plugins.set(platformPlugins.split(',').map(String::trim).filter(String::isNotEmpty))
}

tasks {

    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    buildSearchableOptions {
        enabled = false
    }

    patchPluginXml {

        sinceBuild.set(pluginSinceBuild)
        untilBuild.set(pluginUntilBuild)

        pluginDescription.set(
            file("description.md").readText().lines().run {
                val start = "<!-- Plugin description -->"
                val end = "<!-- Plugin description end -->"

                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in description.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end))
            }.joinToString("\n").let { markdownToHTML(it) }
        )
    }
}
