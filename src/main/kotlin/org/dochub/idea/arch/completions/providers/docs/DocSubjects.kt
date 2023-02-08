package org.dochub.idea.arch.completions.providers.docs

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import org.dochub.idea.arch.completions.providers.Docs
import org.dochub.idea.arch.completions.providers.suggets.IDSuggestComplex
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLSequenceItem

class DocSubjects : IDSuggestComplex() {
    override val sections: Array<String>
        get() = arrayOf("components", "aspects")
    override val pattern: ElementPattern<out PsiElement?>
        get() = PlatformPatterns.psiElement()
            .withSuperParent(2, psi(YAMLSequenceItem::class.java))
            .withSuperParent(
                4,
                psi(YAMLKeyValue::class.java)
                    .withName(PlatformPatterns.string().equalTo(keyword))
                    .and(Docs.rootPattern)
            )

    companion object {
        private const val keyword = "subjects"
    }
}
