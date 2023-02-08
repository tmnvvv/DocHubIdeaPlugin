package org.dochub.idea.arch.jsonata.psi

import com.intellij.psi.tree.IElementType
import org.dochub.idea.arch.jsonata.JSONataLanguage
import org.jetbrains.annotations.NonNls

class JSONataElementType(@NonNls debugName: String) :
    IElementType(debugName, JSONataLanguage.INSTANCE)