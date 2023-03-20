import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.grammarkit.tasks.GenerateParserTask
import org.jetbrains.intellij.mainSourceSet

plugins {
    id("java")
    id("idea")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.intellij")
    id("org.jetbrains.grammarkit")
}

val genPath: String by project
val junitVersion: String by project
val platformVersion: String by project
val platformType: String by project
val platformPlugins: String by project
val flexPath: String by project
val genLexerPath: String by project
val genLexerClassName: String by project
val genLexerPurgeOldFiles: String by project
val bnfPath: String by project
val genParserClassPath: String by project
val genPsiPath: String by project
val genParserPurgeOldFiles: String by project
val kotlinVersion: String by project
val JSONataVersion: String by project
val pluginSinceBuild: String by project
val pluginUntilBuild: String by project


repositories {
    mavenCentral()
}

dependencies {

    /**
     * Базовые зависимости
     */
    implementation ("com.ibm.jsonata4java:JSONata4Java:$JSONataVersion")
    implementation(files("jars/elk-full.jar", "jars/plantuml.jar"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    /**
     * Тестовые зависимости
     */
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

intellij {
    version.set(platformVersion)
    type.set(platformType)
    plugins.set(platformPlugins.split(',').map(String::trim).filter(String::isNotEmpty))
}


sourceSets {
    mainSourceSet(project).java.srcDir(genPath)
}

tasks.jar {
    manifest.attributes["PLANTUML_LIMIT_SIZE"] = "24384"
}

val generateJSONataLexer =  tasks.register<GenerateLexerTask>("genJSONataLexer") {
    description = "Generated Jsonata lexer"
    group = "build setup"
    source.set(flexPath)
    targetDir.set(genLexerPath)
    targetClass.set(genLexerClassName)
    purgeOldFiles.set(genLexerPurgeOldFiles.toBoolean())
}

val generateJSONataParser = tasks.register<GenerateParserTask>("genJSONataParser") {
    dependsOn(generateJSONataLexer)
    description = "Generated Jsonata parser"
    group = "build setup"
    source.set(bnfPath)
    targetRoot.set(genPath)
    pathToParser.set(genParserClassPath)
    pathToPsiRoot.set(genPsiPath)
    purgeOldFiles.set(genParserPurgeOldFiles.toBoolean())

    parserFile.set(project.file(genParserClassPath))
    psiDir.set(project.file(genPath))
    sourceFile.set(project.file(bnfPath))
    targetRootOutputDir.set(project.file(genPath))
}

tasks {

    buildSearchableOptions {
        enabled = false
    }

    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
        dependsOn(generateJSONataParser)
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
        dependsOn(generateJSONataParser)
    }

    patchPluginXml {
        sinceBuild.set(pluginSinceBuild)
        untilBuild.set(pluginUntilBuild)
        changeNotes.set(file("src/main/resources/html/change-notes.html").readText())
    }


    test {
        useJUnitPlatform()
    }
}


