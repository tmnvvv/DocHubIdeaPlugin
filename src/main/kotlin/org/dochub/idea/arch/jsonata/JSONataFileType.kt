package org.dochub.idea.arch.jsonata

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.util.NlsSafe
import org.jetbrains.annotations.NonNls
import javax.swing.Icon

class JSONataFileType private constructor() : LanguageFileType(JSONataLanguage.INSTANCE) {
    @NonNls
    override fun getName(): String {
        return "JSONata"
    }

    override fun getDescription(): @NlsContexts.Label String {
        return "JSON query and transformation language"
    }

    override fun getDefaultExtension(): @NlsSafe String {
        return "jsonata"
    }

    override fun getIcon(): Icon? {
        return JSONataLanguage.ICON
    }

    companion object {
        val INSTANCE = JSONataFileType()
    }
}