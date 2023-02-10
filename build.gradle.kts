import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.changelog.Changelog

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
    implementation(files("libs/elk-full.jar"))
    implementation(files("libs/plantuml.jar"))
    implementation("com.google.code.gson:gson:2.10.1")


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

java {
    val javaSrcDir = "src/main/gen"
    val mainJavaSourceSet: SourceDirectorySet = sourceSets.getByName("main").java
    mainJavaSourceSet.srcDir(javaSrcDir)
}

tasks.jar {
    manifest.attributes["PLANTUML_LIMIT_SIZE"] = "24384"
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

        changeNotes.set(provider {
            with(changelog) {
                renderItem(
                    getOrNull(project.version as String)
                        ?: runCatching { getLatest() }.getOrElse { getUnreleased() },
                    Changelog.OutputType.HTML,
                )
            }
        })
    }
}
