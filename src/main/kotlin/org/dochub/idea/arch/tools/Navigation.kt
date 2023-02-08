package org.dochub.idea.arch.tools

import com.fasterxml.jackson.databind.JsonNode
import com.intellij.codeInsight.TargetElementUtil
import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.pom.Navigatable
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiUtilCore
import com.intellij.util.messages.MessageBusConnection
import org.dochub.idea.arch.indexing.SectionData
import org.dochub.idea.arch.indexing.getProjectCache
import org.dochub.idea.arch.utils.findFile
import org.dochub.idea.arch.indexing.getRootManifestName
import org.dochub.idea.arch.references.providers.RefBaseID.Companion.makeSourcePattern


class Navigation(private val project: Project) {
    private val connBus: MessageBusConnection? = null
    fun getVFile(uri: String): VirtualFile? {
        val source: String
        if (uri == ROOT_SOURCE_URI) {
            source = getRootManifestName(project)
        } else if (uri.startsWith(ROOT_SOURCE_PATH)) {
            source = uri.substring(ROOT_SOURCE_PATH.length)
        } else if (uri.startsWith(project.basePath!!)) {
            source = uri.substring(project.basePath!!.length)
        } else source = uri
        return findFile(source, project)
    }

    fun gotoPsiElement(element: PsiElement?) {
        val action = ActionManager.getInstance().getAction(IdeActions.ACTION_COPY_REFERENCE)
        val event = AnActionEvent(
            null, DataManager.getInstance().dataContext,
            ActionPlaces.UNKNOWN, Presentation(),
            ActionManager.getInstance(), 0
        )
        action.actionPerformed(event)
        var navElement = element!!.navigationElement
        navElement = TargetElementUtil.getInstance().getGotoDeclarationTarget(element, navElement)
        if (navElement is Navigatable) {
            if ((navElement as Navigatable).canNavigate()) {
                (navElement as Navigatable).navigate(true)
            }
        } else if (navElement != null) {
            val navOffset = navElement.textOffset
            val virtualFile = PsiUtilCore.getVirtualFile(navElement)
            if (virtualFile != null) {
                OpenFileDescriptor(project, virtualFile, navOffset).navigate(true)
            }
        }
    }

    private fun gotoByID(uri: String, entity: String, id: String) {
        val vFile = getVFile(uri)
        if (vFile != null) {
            val section = entityToSection(entity)
            val targetFile = PsiManager.getInstance(project).findFile(vFile)
            PsiTreeUtil.processElements(
                targetFile
            ) { element: PsiElement? ->
                if (makeSourcePattern(section, id).accepts(element)) {
                    gotoPsiElement(element)
                    return@processElements false
                }
                true
            }
        }
    }

    private fun gotoBySource(uri: String) {
        val vFile = getVFile(uri)
        if (vFile != null) gotoPsiElement(PsiManager.getInstance(project).findFile(vFile))
    }

    private fun gotoByPosition(uri: String, start: Int) {
        val vFile = getVFile(uri)
        if (vFile != null) {
            val targetFile = PsiManager.getInstance(project).findFile(vFile)
            gotoPsiElement(targetFile!!.findElementAt(start))
        }
    }

    fun go(source: String, entity: String, id: String) {
        val app = ApplicationManager.getApplication()
        app.invokeLater({ gotoByID(source, entity, id) }, ModalityState.NON_MODAL)
    }

    fun go(source: String, pos: Int) {
        val app = ApplicationManager.getApplication()
        app.invokeLater({ gotoByPosition(source, pos) }, ModalityState.NON_MODAL)
    }

    fun go(source: String) {
        val app = ApplicationManager.getApplication()
        app.invokeLater({ gotoBySource(source) }, ModalityState.NON_MODAL)
    }

    fun go(location: JsonNode) {
        val jsonID = location["id"]
        val jsonEntity = location["entity"]
        val jsonSource = location["source"]
        val jsonRange = location["range"]
        if (jsonID != null && jsonEntity != null && jsonSource != null) {
            val id = jsonID.asText()
            val entity = jsonEntity.asText()
            var source = jsonSource.asText()
            if (source == "null") {
                val cache: MutableMap<String, SectionData> = getProjectCache(project)
                val section = entityToSection(entity) ?: return
                val components: SectionData = (if (cache == null) null else cache[section]) ?: return
                val files = components.ids.get(id) as  List<VirtualFile>
                if (files != null && files.size > 0) source = files[0].path
            }
            go(source, entity, id)
        } else if (jsonRange != null && jsonSource != null) {
            go(jsonSource.asText(), jsonRange["start"].asInt())
        } else if (jsonSource != null) go(jsonSource.asText())
    }

    companion object {
        private fun entityToSection(entity: String): String {
            var section: String
            when (entity) {
                "component" -> section = "components"
                "document" -> section = "docs"
                "context" -> section = "contexts"
                "aspect" -> section = "aspects"
                else -> section = ""
            }
            return section
        }
    }
}
