package org.dochub.idea.arch.completions.providers.docs

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import org.dochub.idea.arch.completions.providers.Docs
import org.dochub.idea.arch.completions.providers.suggets.IDSuggestDatasets
import org.jetbrains.yaml.psi.YAMLKeyValue

class DocOrigin : IDSuggestDatasets() {
    override val pattern: ElementPattern<out PsiElement?>
        get() = PlatformPatterns.psiElement()
            .withSuperParent(
                2,
                psi(YAMLKeyValue::class.java)
                    .withName(PlatformPatterns.string().equalTo(keyword))
                    .and(Docs.rootPattern)
            )

    companion object {
        private const val keyword = "origin"
    }
}