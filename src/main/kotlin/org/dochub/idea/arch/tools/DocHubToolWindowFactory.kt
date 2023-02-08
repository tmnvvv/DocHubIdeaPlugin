package org.dochub.idea.arch.tools

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import java.awt.BorderLayout

class DocHubToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myToolWindow = DocHubToolWindow(project)
        val group = DefaultActionGroup()
        group.add(object : AnAction("Reset", "Перезагрузить", AllIcons.Actions.Refresh) {
            override fun actionPerformed(e: AnActionEvent) {
                myToolWindow.reloadHtml()
            }
        })
        group.add(object : AnAction("Debug", "Отладка", AllIcons.Actions.StartDebugger) {
            override fun actionPerformed(e: AnActionEvent) {
                myToolWindow.openDevtools()
            }
        })
        group.add(object : AnAction("Back", "Назад", AllIcons.Actions.Back) {
            override fun actionPerformed(e: AnActionEvent) {
                myToolWindow.cefBrowser.goBack()
            }
        })
        val actionBar = ActionManager.getInstance().createActionToolbar("DH Tools", group, true)
        actionBar.targetComponent = toolWindow.component
        toolWindow.component.add(actionBar.component, BorderLayout.PAGE_START)
        toolWindow.component.add(myToolWindow.component)
        toolWindow.component.isVisible = true
    }
}