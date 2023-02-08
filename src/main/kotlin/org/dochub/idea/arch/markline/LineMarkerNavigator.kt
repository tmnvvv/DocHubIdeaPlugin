package org.dochub.idea.arch.markline

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.util.messages.Topic
import java.awt.event.MouseEvent


var ON_NAVIGATE_MESSAGE = Topic.create("Navigate to", NavigateMessage::class.java)

fun makeLineMarkerInfo(
    naviHandler: DocHubNavigationHandler?,
    element: PsiElement
): LineMarkerInfo<PsiElement> {
    return LineMarkerInfo<PsiElement>(
        element,
        element.textRange,
        AllIcons.Actions.Preview,
        { "Показать в DocHub" },
        naviHandler,
        GutterIconRenderer.Alignment.LEFT,
        { "DocHub" }
    )
}

interface NavigateMessage {
    fun go(entity: String?, id: String?)
}

class DocHubNavigationHandler(private val entity: String?, private val id: String?) : GutterIconNavigationHandler<PsiElement> {
    override fun navigate(e: MouseEvent, elt: PsiElement) {
        val publisher = elt.project.messageBus.syncPublisher(ON_NAVIGATE_MESSAGE)
        publisher.go(entity, id)
    }
}
