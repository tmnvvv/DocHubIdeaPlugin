package org.dochub.idea.arch.tools

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.ide.CopyPasteManager
import java.awt.datatransfer.StringSelection


fun copy(data: String) {
    val app = ApplicationManager.getApplication()
    app.invokeLater({
        val copyPasteManager = CopyPasteManager.getInstance()
        copyPasteManager.setContents(StringSelection(data))
    }, ModalityState.NON_MODAL)
}

