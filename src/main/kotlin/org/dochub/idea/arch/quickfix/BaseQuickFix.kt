package org.dochub.idea.arch.quickfix

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.codeInspection.util.IntentionFamilyName
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.intellij.util.IncorrectOperationException

abstract class BaseQuickFix : BaseIntentionAction {
    protected var element: PsiElement?
    abstract fun getFixPattern(element: PsiElement?): ElementPattern<out PsiElement?>?
    abstract fun makeFix(element: PsiElement, holder: AnnotationHolder)

    constructor(element: PsiElement?) {
        this.element = element
    }

    constructor() {
        element = null
    }

    override fun getFamilyName(): @IntentionFamilyName String {
        return String()
    }

    override fun isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean {
        return false
    }

    @Throws(IncorrectOperationException::class)
    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
    }

    companion object {
        fun <T : PsiElement?> psi(aClass: Class<T>?): PsiElementPattern.Capture<T> {
            return PlatformPatterns.psiElement(aClass)
        }

        fun psi(type: IElementType?): PsiElementPattern.Capture<PsiElement> {
            return PlatformPatterns.psiElement(type)
        }
    }
}
