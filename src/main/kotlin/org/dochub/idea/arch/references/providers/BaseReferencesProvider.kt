package org.dochub.idea.arch.references.providers

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.patterns.StandardPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.tree.IElementType
import com.intellij.util.ProcessingContext
import org.jetbrains.yaml.psi.YAMLScalarText

open class BaseReferencesProvider : PsiReferenceProvider() {

    open val refPattern: ElementPattern<out PsiElement>
        get() = StandardPatterns.instanceOf(PsiElement::class.java)

    open fun getSourcePattern(ref: Any?): ElementPattern<out PsiElement> {
        return StandardPatterns.instanceOf(PsiElement::class.java)
    }

    fun isElementFound(element: PsiElement?, ref: Any?): Boolean {
        return false
    }

    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        if (element.javaClass == YAMLScalarText::class.java) {
        }
        // return new PsiReference[0];
        return PsiReference.EMPTY_ARRAY
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