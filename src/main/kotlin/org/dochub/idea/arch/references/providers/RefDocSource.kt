package org.dochub.idea.arch.references.providers

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import org.dochub.idea.arch.completions.providers.Docs
import org.jetbrains.yaml.psi.YAMLKeyValue

class RefDocSource : RefBaseSource() {
    override val refPattern: ElementPattern<out PsiElement?>
        get() = PlatformPatterns.psiElement()
            .withParent(
                psi(YAMLKeyValue::class.java)
                    .withName(PlatformPatterns.string().equalTo("source"))
                    .and(Docs.rootPattern)
            )
}
