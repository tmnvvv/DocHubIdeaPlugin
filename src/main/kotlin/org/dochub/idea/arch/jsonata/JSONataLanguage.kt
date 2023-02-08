package org.dochub.idea.arch.jsonata

import com.intellij.lang.Language
import com.intellij.openapi.util.IconLoader


object JSONataLanguage : Language("JSONata") {

    val INSTANCE: JSONataLanguage = JSONataLanguage
    val ICON = IconLoader.getIcon("/images/jsonata.png", JSONataLanguage::class.java)
}