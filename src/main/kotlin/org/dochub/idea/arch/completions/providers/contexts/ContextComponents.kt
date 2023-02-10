package org.dochub.idea.arch.completions.providers.contexts

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import org.dochub.idea.arch.completions.providers.Contexts
import org.dochub.idea.arch.completions.providers.suggets.IDSuggestComponents
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLSequenceItem

class ContextComponents : IDSuggestComponents() {

    override val pattern: ElementPattern<out PsiElement?>
        get() = PlatformPatterns.psiElement()
            .withSuperParent(2, psi(YAMLSequenceItem::class.java))
            .withSuperParent(
                4,
                psi(YAMLKeyValue::class.java)
                    .withName(PlatformPatterns.string().equalTo(keyword))
                    .and(Contexts.rootPattern)
            )

    companion object {
        private const val keyword = "components"
    }
}