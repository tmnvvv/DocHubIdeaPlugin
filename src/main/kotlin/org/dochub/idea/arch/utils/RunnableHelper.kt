package org.dochub.idea.arch.utils

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.project.Project

fun runRead(project: Project, runnable: Runnable, name: String) {
    CommandProcessor.getInstance().executeCommand(
        project,
        ReadAction(runnable),
        name,
        "DocHub"
    )
}

fun runWrite(project: Project?, runnable: Runnable, name: String) {
    CommandProcessor.getInstance().executeCommand(
        project, WriteAction(runnable),
        name,
        "DocHub"
    )
}

class ReadAction(var runnable: Runnable) : Runnable {
    override fun run() {
        ApplicationManager.getApplication().runReadAction(runnable)
    }
}

class WriteAction(var runnable: Runnable) : Runnable {
    override fun run() {
        ApplicationManager.getApplication().runWriteAction(runnable)
    }
}
