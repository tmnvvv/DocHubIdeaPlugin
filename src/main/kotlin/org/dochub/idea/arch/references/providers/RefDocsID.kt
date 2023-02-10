package org.dochub.idea.arch.references.providers

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.StandardPatterns
import com.intellij.psi.PsiElement
import org.dochub.idea.arch.completions.providers.Components
import org.dochub.idea.arch.references.ID_PATTERN
import org.jetbrains.yaml.psi.YAMLDocument
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLMapping

class RefDocsID : RefBaseID() {
    override val keyword: String
        get() = Companion.keyword
    override val refPattern: ElementPattern<out PsiElement?>
        get() = pattern()

    companion object {
        private const val keyword = "docs"
        fun pattern(): ElementPattern<out PsiElement?> {
            return PlatformPatterns.or( // Ссылки в идентификаторах документов
                PlatformPatterns.psiElement(YAMLKeyValue::class.java)
                    .withParent(psi(YAMLMapping::class.java))
                    .withSuperParent(
                        2,
                        psi(YAMLKeyValue::class.java)
                            .withName(PlatformPatterns.string().equalTo(keyword))
                            .withSuperParent(2, psi(YAMLDocument::class.java))
                    ),
                PlatformPatterns.psiElement()
                    .notEmpty()
                    .afterLeaf(":")
                    .withText(StandardPatterns.string().matches(ID_PATTERN))
                    .withParent(
                        psi(YAMLKeyValue::class.java)
                            .withName("contract")
                            .withSuperParent(
                                4,
                                psi(YAMLKeyValue::class.java)
                                    .withName("links")
                                    .and(Components.rootPattern)
                            )
                    )
            )
        }
    }
}