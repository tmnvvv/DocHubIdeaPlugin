package org.dochub.idea.arch.completions.providers.namespaces

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import org.dochub.idea.arch.completions.providers.suggets.IDSuggestComplex
import org.jetbrains.yaml.psi.YAMLDocument
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLMapping

class NamespaceID : IDSuggestComplex() {
    override val sections: Array<String>
        get() = arrayOf("components", "contexts", "aspects")
    override val pattern: ElementPattern<out PsiElement?>
        get() = PlatformPatterns.or(
            PlatformPatterns.psiElement()
                .withSuperParent(2, psi(YAMLMapping::class.java))
                .withSuperParent(
                    3,
                    psi(YAMLKeyValue::class.java)
                        .withName(PlatformPatterns.string().equalTo(keyword))
                        .withSuperParent(2, psi(YAMLDocument::class.java))
                ),
            PlatformPatterns.psiElement()
                .withSuperParent(
                    2,
                    psi(YAMLKeyValue::class.java)
                        .withName(PlatformPatterns.string().equalTo(keyword))
                        .withSuperParent(2, psi(YAMLDocument::class.java))
                )
        )

    companion object {
        private const val keyword = "namespaces"
    }
}
