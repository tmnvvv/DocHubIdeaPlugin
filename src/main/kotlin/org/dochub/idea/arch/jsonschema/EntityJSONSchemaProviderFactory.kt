package org.dochub.idea.arch.jsonschema

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.jsonSchema.extension.JsonSchemaFileProvider
import com.jetbrains.jsonSchema.extension.JsonSchemaProviderFactory
import com.jetbrains.jsonSchema.extension.SchemaType

class EntityJSONSchemaProviderFactory : JsonSchemaProviderFactory {
    override fun getProviders(project: Project): List<JsonSchemaFileProvider> {
        return listOf<JsonSchemaFileProvider>(object : JsonSchemaFileProvider {
            override fun isAvailable(file: VirtualFile): Boolean {
                return true
            }

            override fun getName(): String {
                return "DocHub.Entity.JSONSchema"
            }

            override fun getSchemaFile(): VirtualFile? {
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
}