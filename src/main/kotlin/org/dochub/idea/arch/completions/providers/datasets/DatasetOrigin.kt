package org.dochub.idea.arch.completions.providers.datasets

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import org.dochub.idea.arch.completions.providers.Dataset
import org.dochub.idea.arch.completions.providers.suggets.IDSuggestDatasets
import org.jetbrains.yaml.psi.YAMLKeyValue

class DatasetOrigin : IDSuggestDatasets() {
    override val pattern: ElementPattern<out PsiElement?>
        get() = PlatformPatterns.psiElement()
            .withSuperParent(
                2,
                psi(YAMLKeyValue::class.java)
                    .withName(PlatformPatterns.string().equalTo(keyword))
                    .and(Dataset.rootPattern)
            )

    companion object {
        private const val keyword = "origin"
    }
}