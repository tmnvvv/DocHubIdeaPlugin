import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.grammarkit.tasks.GenerateParserTask

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
val plantumlVersion: String by project
val elkVersion: String by project

repositories {
    mavenCentral()
}

dependencies {

    /**
     * Базовые зависимости
     */
    implementation ("com.ibm.jsonata4java:JSONata4Java:$JSONataVersion")
    implementation("net.sourceforge.plantuml:plantuml:$plantumlVersion")
    implementation("org.eclipse.elk:org.eclipse.elk.alg.layered:$elkVersion")
    implementation("org.eclipse.elk:org.eclipse.elk.core:$elkVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.javassist:javassist:3.29.2-GA")

    /**
     * Тестовые зависимости
     */
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

intellij {
    version.set(platformVersion)
    type.set(platformType)
    plugins.set(platformPlugins.split(',').map(String::trim).filter(String::isNotEmpty))
}


sourceSets {
    main {
        java {
            srcDir(genPath)
        }
    }
}

tasks.jar {
    manifest.attributes["PLANTUML_LIMIT_SIZE"] = "24384"
}

val generateJSONataLexer =  tasks.register<GenerateLexerTask>("genJSONataLexer") {

    description = "Generated Jsonata lexer"
    group = "build setup"
    sourceFile.set(File(flexPath))
    targetOutputDir.set(File(genLexerPath))
    purgeOldFiles.set(genLexerPurgeOldFiles.toBoolean())
}

val generateJSONataParser = tasks.register<GenerateParserTask>("genJSONataParser") {
    dependsOn(generateJSONataLexer)
    description = "Generated Jsonata parser"
    group = "build setup"

    pathToParser.set(genParserClassPath)
    pathToPsiRoot.set(genPsiPath)
    purgeOldFiles.set(genParserPurgeOldFiles.toBoolean())

    sourceFile.set(File(bnfPath))
    targetRootOutputDir.set(File(genPath))
    pathToParser.set(genParserClassPath)
    pathToPsiRoot.set(genPsiPath)
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
        options.encoding = "UTF-8"
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


