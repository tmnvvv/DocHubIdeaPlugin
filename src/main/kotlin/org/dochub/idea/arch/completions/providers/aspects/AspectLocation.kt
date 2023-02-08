package org.dochub.idea.arch.completions.providers.aspects

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import org.dochub.idea.arch.completions.providers.Aspects
import org.dochub.idea.arch.completions.providers.suggets.LocationSuggestAspects
import org.jetbrains.yaml.psi.YAMLKeyValue

class AspectLocation : LocationSuggestAspects() {
     override val pattern: ElementPattern<out PsiElement?>
        get() = PlatformPatterns.or(
            PlatformPatterns.psiElement()
                .withSuperParent(
                    2,
                    psi(YAMLKeyValue::class.java)
                        .withName(PlatformPatterns.string().equalTo(keyword))
                        .and(Aspects.rootPattern)
                ),
            PlatformPatterns.psiElement()
                .withSuperParent(
                    3,
                    psi(YAMLKeyValue::class.java)
                        .withName(PlatformPatterns.string().equalTo(keyword))
                        .and(Aspects.rootPattern)
                )
        )

    companion object {
        private const val keyword = "location"
    }
}
