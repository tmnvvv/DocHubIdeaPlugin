package org.dochub.idea.arch.references.providers

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.YAMLDocument
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLMapping

class RefContextID : RefBaseID() {
    override val keyword: String
        get() = Companion.keyword
    override val refPattern: ElementPattern<out PsiElement?>
        get() = pattern()

    companion object {
        private const val keyword = "contexts"
        fun pattern(): ElementPattern<out PsiElement?> {
            return PlatformPatterns.psiElement(YAMLKeyValue::class.java)
                .withParent(psi(YAMLMapping::class.java))
                .withSuperParent(
                    2,
                    psi(YAMLKeyValue::class.java)
                        .withName(PlatformPatterns.string().equalTo(keyword))
                        .withSuperParent(2, psi(YAMLDocument::class.java))
                )
        }
    }
}