package org.dochub.idea.arch.completions.providers

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType

abstract class CustomProvider {

    abstract fun appendToCompletion(completion: CompletionContributor)

    companion object {
        fun <T : PsiElement> psi(aClass: Class<T>): PsiElementPattern.Capture<T> {
            return PlatformPatterns.psiElement(aClass)
        }

        fun psi(type: IElementType): PsiElementPattern.Capture<PsiElement> {
            return PlatformPatterns.psiElement(type)
        }
    }
}