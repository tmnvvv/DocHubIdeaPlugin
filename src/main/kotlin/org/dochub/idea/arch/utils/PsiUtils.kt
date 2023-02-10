package org.dochub.idea.arch.utils

import com.intellij.psi.PsiElement
import com.intellij.util.ObjectUtils
import org.jetbrains.yaml.psi.YAMLDocument

fun getText(element: PsiElement?): String {
    return element?.text?.replaceFirst("IntellijIdeaRulezzz".toRegex(), "") ?: ""
}

fun getYamlDocumentByPsiElement(element: PsiElement): PsiElement {
    var document = element
    while (true) {
        if (ObjectUtils.tryCast(document, YAMLDocument::class.java) != null) break
        document = document.parent
    }
    return document
}