package org.dochub.idea.arch.references.providers

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.YAMLDocument
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLSequenceItem

class RefImportsSource : RefBaseSource() {

    override val refPattern: ElementPattern<out PsiElement?>
        get() = PlatformPatterns.psiElement()
            .withParent(
                psi(YAMLSequenceItem::class.java)
                    .withSuperParent(
                        2,
                        psi(YAMLKeyValue::class.java)
                            .withName(PlatformPatterns.string().equalTo(keyword))
                            .withSuperParent(2, psi(YAMLDocument::class.java))
                    )
            )

    companion object {
        private const val keyword = "imports"
    }
}