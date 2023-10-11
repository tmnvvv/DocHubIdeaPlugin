package org.dochub.idea.arch.tools;

import com.intellij.openapi.editor.event.VisibleAreaEvent;
import com.intellij.openapi.editor.event.VisibleAreaListener;
import com.intellij.ui.jcef.JBCefBrowser;
import org.jetbrains.annotations.NotNull;


public class SchemaViewer extends JBCefBrowser implements VisibleAreaListener {
    public SchemaViewer(String url) {
        super(url);
    }

    @Override
    public void visibleAreaChanged(@NotNull VisibleAreaEvent e) {

    }
}
