package org.dochub.idea.arch.inspections

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import org.dochub.idea.arch.quickfix.aspects.AspectRootQuickFix
import org.dochub.idea.arch.quickfix.components.ComponentRootQuickFix
import org.dochub.idea.arch.quickfix.contexts.ContextRootQuickFix
import org.dochub.idea.arch.quickfix.docs.DocRootQuickFix
import org.dochub.idea.arch.quickfix.namespaces.NamespaceRootQuickFix
import org.jetbrains.annotations.NonNls
import org.jetbrains.yaml.psi.YAMLDocument
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl


private var patterns: Array<ElementPattern<out PsiElement>>? = null
private var emptyChecker: PatternCondition<PsiElement>? = null

fun getPatterns(): Array<ElementPattern<out PsiElement>>? {
    if (patterns == null) {
        emptyChecker = object : PatternCondition<PsiElement>("Empty checker") {
            override fun accepts(element: PsiElement, context: ProcessingContext): Boolean {
                val parent = element.parent
                return if (parent is YAMLKeyValueImpl) {
                    parent.value == null
                } else false
            }
        }
        patterns = arrayOf(
            makeRootPattern("Doc field checker", "docs", DocRootQuickFix.requiredProps),
            makeRootPattern("Aspect field checker", "aspects", AspectRootQuickFix.requiredProps),
            makeRootPattern("Component field checker", "components", ComponentRootQuickFix.requiredProps),
            makeRootPattern("Context field checker", "contexts", ContextRootQuickFix.requiredProps),
            makeRootPattern("Namespace field checker", "namespaces", NamespaceRootQuickFix.requiredProps)
        )
    }
    return patterns
}

private class RequiredFieldChecker(@NonNls debugMethodName: String, fields: Array<String>) : PatternCondition<PsiElement>(debugMethodName) {
    var fields: List<String?>? = null

    init {
        this.fields = fields.asList()
    }

    override fun accepts(element: PsiElement, context: ProcessingContext): Boolean {
        return fields!!.indexOf(element.text) >= 0
    }
}

fun makeRootPattern(
    debugMethodName: String,
    keyword: String,
    fields: Array<String>
): ElementPattern<out PsiElement?> {
    return PlatformPatterns.psiElement()
        .beforeLeaf(":")
        .with(emptyChecker!!)
        .andOr(
            PlatformPatterns.psiElement()
                .with(
                    RequiredFieldChecker(
                        debugMethodName,
                        fields
                    )
                )
                .withSuperParent(3, YAMLKeyValue::class.java)
                .withSuperParent(
                    5,
                    PlatformPatterns.psiElement(YAMLKeyValue::class.java)
                        .withName(PlatformPatterns.string().equalTo(keyword))
                )
                .withSuperParent(7, YAMLDocument::class.java)
        )
}
