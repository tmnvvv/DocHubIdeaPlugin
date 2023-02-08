package org.dochub.idea.arch.completions.providers.contexts

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import org.dochub.idea.arch.completions.providers.Contexts
import org.dochub.idea.arch.completions.providers.suggets.LocationSuggestContexts
import org.jetbrains.yaml.psi.YAMLKeyValue

class ContextLocation : LocationSuggestContexts() {

    override val pattern: ElementPattern<out PsiElement?>
        protected get() = PlatformPatterns.or(
            PlatformPatterns.psiElement()
                .withSuperParent(
                    2,
                    psi(YAMLKeyValue::class.java)
                        .withName(PlatformPatterns.string().equalTo(keyword))
                        .and(Contexts.rootPattern)
                ),
            PlatformPatterns.psiElement()
                .withSuperParent(
                    3,
                    psi(YAMLKeyValue::class.java)
                        .withName(PlatformPatterns.string().equalTo(keyword))
                        .and(Contexts.rootPattern)
                )
        )

    companion object {
        private const val keyword = "location"
    }
}