package org.dochub.idea.arch.quickfix.components

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import org.dochub.idea.arch.quickfix.BaseStructureQuickFix
import org.jetbrains.yaml.psi.YAMLDocument
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLMapping

class ComponentRootQuickFix : BaseStructureQuickFix() {

    override fun getFixPattern(element: PsiElement?): ElementPattern<out PsiElement> {
        return PlatformPatterns.psiElement()
            .beforeLeaf(":")
            .withSuperParent(2, psi(YAMLMapping::class.java))
            .withSuperParent(
                3,
                psi(YAMLKeyValue::class.java)
                    .withName(PlatformPatterns.string().equalTo("components"))
                    .withSuperParent(2, psi(YAMLDocument::class.java))
            )
    }

    companion object {
        val requiredProps = arrayOf("title", "entity")
    }
}