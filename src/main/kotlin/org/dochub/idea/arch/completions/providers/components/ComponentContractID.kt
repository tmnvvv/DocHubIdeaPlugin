package org.dochub.idea.arch.completions.providers.components

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import org.dochub.idea.arch.completions.providers.Components
import org.dochub.idea.arch.completions.providers.suggets.IDSuggestDocuments
import org.jetbrains.yaml.psi.YAMLKeyValue

class ComponentContractID() : IDSuggestDocuments() {
    override val pattern: ElementPattern<out PsiElement?>
        get() = PlatformPatterns.psiElement()
            .withSuperParent(
                2,
                psi(YAMLKeyValue::class.java)
                    .withName("contract")
                    .withSuperParent(
                        4,
                        psi(YAMLKeyValue::class.java)
                            .withName("links")
                            .and(Components.rootPattern)
                    )
            )
}