package org.dochub.idea.arch.jsonschema

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.jsonSchema.extension.JsonSchemaFileProvider
import com.jetbrains.jsonSchema.extension.JsonSchemaProviderFactory
import com.jetbrains.jsonSchema.extension.SchemaType
import org.dochub.idea.arch.jsonschema.model.DataDescription
import org.dochub.idea.arch.utils.JsonUtils
import java.net.URL
import java.nio.file.*
import java.util.*



class EntityJSONSchemaProviderFactory : JsonSchemaProviderFactory {

    private val descriptions: MutableMap<String?, DataDescription?> = mutableMapOf()

    override fun getProviders(project: Project): List<JsonSchemaFileProvider> {

        return listOf<JsonSchemaFileProvider>(object : JsonSchemaFileProvider {
            override fun isAvailable(file: VirtualFile): Boolean {
                return true
            }

            override fun getName(): String {
                return "DocHub.Entity.JSONSchema"
            }

            override fun getSchemaFile(): VirtualFile? {
                findJsonConfigurationInJar()
                var result: VirtualFile? = getSchema(project)
                if (result == null) {
                    result = JsonSchemaProviderFactory.getResourceFile(javaClass, "/schemas/empty.json")
                }
                return result
            }

            override fun getSchemaType(): SchemaType {
                return SchemaType.embeddedSchema
            }
        })
    }

    private fun findJsonConfigurationInJar() {
        val uri: URL = getUri(defaultSchemasPath)
        val jsonFindPredicate: (path: Path) -> Boolean = { path: Path -> path.toString().endsWith(".json") }
        val jsonConsumer: (stringPath: String) -> Unit = {
            val (id, title) = JsonUtils.fromJson(it, DataDescription::class.java)
            descriptions[id] = DataDescription(id, title)
        }
        loadConfiguration(jsonFindPredicate, jsonConsumer, uri, defaultSchemasPath)
    }

    private fun getUri(path: String): URL = EntityJSONSchemaProviderFactory::class.java.getResource(path)


    private fun  loadConfiguration(predicate: (path: Path) -> Boolean, consumer: (path: String) -> Unit, uriSchema: URL, fsPath: String ) {
        val myPath: Path? = getPath(uriSchema, fsPath)
        val walk = Files.walk(myPath, 1)
        try {
            val it = walk.iterator()
            while (it.hasNext()) {
                val path = it.next()
                val stringPath =  if (isJar(uriSchema)) path.toString().substring(1) else fsPath + "/" + path.toFile().name
                log.info("loading: Path to json schema files - ${path}")
                if (predicate(path)) {
                    consumer(stringPath)
                }
            }
        } finally {
            walk.close()
        }
    }

    private fun getPath(uri: URL, fsPath: String): Path? {
        var myPath: Path? = null
        if (isJar(uri)) {
            log.info("loading: Scheme JAR discovered")
            val fileSystem: FileSystem? = JsonUtils.getFileSystem(uri.toString())
            if (fileSystem != null) {
                myPath = fileSystem.getPath(fsPath)
            }
            log.info("loading: Path to resource - ${myPath}")
        } else {
            log.info("loading: Scheme discovered: ${ uri.toURI().scheme}")
            myPath = Paths.get(uri.toURI())
            log.info("loading: Path to resource - ${myPath.fileName}")
        }
        return myPath
    }




    private fun isJar(uri: URL): Boolean {
        return uri.toURI().scheme.lowercase(Locale.getDefault()) == "jar"
    }



    companion object {
        private const val defaultSchemasPath = "/schemas"

        var log = Logger.getInstance(EntityJSONSchemaProviderFactory::class.java)
    }
}