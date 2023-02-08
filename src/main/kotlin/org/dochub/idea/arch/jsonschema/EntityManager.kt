package org.dochub.idea.arch.jsonschema

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import java.io.File
import java.io.IOException

private val schemas = mutableMapOf<String, VirtualFile?>()

// Применяет JSONSchema для проекта
fun applySchema(project: Project, schema: String): VirtualFile? {
    val projectHash = project.locationHash
    var currentSchema = schemas[projectHash]
    if (currentSchema != null) {
        File(currentSchema.path).delete()
    }
    try {
        val file = File.createTempFile("EntityDocHubJSONSchema", ".json")
        FileUtil.writeToFile(file, schema)
        currentSchema = VfsUtil.findFileByIoFile(file, true)
        schemas[projectHash] = currentSchema
        ApplicationManager.getApplication().invokeLater({
            PsiManager.getInstance(project).dropResolveCaches()
            PsiManager.getInstance(project).dropPsiCaches()
        }, ModalityState.defaultModalityState())
    } catch (e: IOException) {
        throw RuntimeException(e)
    }
    return currentSchema
}

// Возвращает JSONSchema для проекта
fun getSchema(project: Project): VirtualFile? {
    return schemas[project.locationHash]
}

