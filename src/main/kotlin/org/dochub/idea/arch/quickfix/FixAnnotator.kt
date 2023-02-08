package org.dochub.idea.arch.quickfix

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement
import org.dochub.idea.arch.quickfix.aspects.AspectRootQuickFix
import org.dochub.idea.arch.quickfix.components.ComponentRootQuickFix
import org.dochub.idea.arch.quickfix.contexts.ContextRootQuickFix
import org.dochub.idea.arch.quickfix.docs.DocRootQuickFix
import org.dochub.idea.arch.quickfix.namespaces.NamespaceRootQuickFix

class FixAnnotator : Annotator {
    private val fixes = arrayOf<BaseQuickFix>(
        ComponentRootQuickFix(),
        AspectRootQuickFix(),
        DocRootQuickFix(),
        ContextRootQuickFix(),
        NamespaceRootQuickFix()
    )

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        for (fix in fixes) {
            val pattern: ElementPattern<*>? = fix.getFixPattern(element)
            if (pattern!!.accepts(element)) {
                fix.makeFix(element, holder)
            }
        }
    }
}
