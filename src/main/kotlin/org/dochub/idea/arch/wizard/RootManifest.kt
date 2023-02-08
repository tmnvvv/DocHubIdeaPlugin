package org.dochub.idea.arch.wizard

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import org.dochub.idea.arch.tools.Navigation
import java.io.IOException

class RootManifest {
    private fun executeAction(action: Runnable) {
        val app = ApplicationManager.getApplication()
        app.invokeLater { ApplicationManager.getApplication().runWriteAction(action) }
    }

    fun createExampleManifest(project: Project?): VirtualFile? {
        executeAction(object : Runnable {
            override fun run() {
                val vProject = ProjectRootManager.getInstance(project!!).contentRoots[0]
                try {
                    val input = javaClass.classLoader.getResourceAsStream("wizard/example.yaml")
                    val root = vProject.createChildData(this, "dochub.yaml")
                    root.setBinaryContent(input.readAllBytes())
                    Navigation(project).gotoPsiElement(PsiManager.getInstance(project).findFile(root))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        })
        return null
    }

    fun createRootManifest(project: Project?): VirtualFile? {
        executeAction(object : Runnable {
            override fun run() {
                val vProject = ProjectRootManager.getInstance(project!!).contentRoots[0]
                try {
                    val input = javaClass.classLoader.getResourceAsStream("wizard/dochub.yaml")
                    val root = vProject.createChildData(this, "dochub.yaml")
                    root.setBinaryContent(input.readAllBytes())
                    Navigation(project).gotoPsiElement(PsiManager.getInstance(project).findFile(root))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        })
        return null
    }
}
