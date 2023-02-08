package org.dochub.idea.arch.completions.providers.suggets

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.openapi.project.Project
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import org.dochub.idea.arch.completions.providers.CustomProvider
import org.dochub.idea.arch.indexing.SectionData

open class BaseSuggest : CustomProvider() {

    protected open val pattern: ElementPattern<out PsiElement> = PlatformPatterns.psiElement()

    protected fun getProjectCache(project: Project): Map<String, SectionData> {
        return getProjectCache(project)
    }

    override fun appendToCompletion(completion: CompletionContributor) {
        TODO("Not yet implemented")
    }
}