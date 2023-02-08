package org.dochub.idea.arch.tools

import com.intellij.openapi.editor.event.VisibleAreaEvent
import com.intellij.openapi.editor.event.VisibleAreaListener
import com.intellij.ui.jcef.JBCefBrowser

class SchemaViewer(url: String) : JBCefBrowser(url), VisibleAreaListener {
    override fun visibleAreaChanged(e: VisibleAreaEvent) {}
}