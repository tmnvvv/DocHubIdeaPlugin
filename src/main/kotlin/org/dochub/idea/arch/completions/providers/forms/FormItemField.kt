package org.dochub.idea.arch.completions.providers.forms

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import org.dochub.idea.arch.completions.providers.CustomProvider
import org.jetbrains.yaml.psi.YAMLKeyValue

class FormItemField : CustomProvider() {
    override fun appendToCompletion(completion: CompletionContributor) {
        completion.extend(
            CompletionType.BASIC,
            rootPattern,
            object : CompletionProvider<CompletionParameters>() {
                public override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    resultSet: CompletionResultSet
                ) {
                    for (key in keys) {
                        resultSet.addElement(LookupElementBuilder.create(key))
                    }
                }
            }
        )
    }

    companion object {
        private const val keyword = "fields"
        private val keys = arrayOf(
            "title", "required"
        )
        val rootPattern: ElementPattern<out PsiElement?> = PlatformPatterns.or(
            PlatformPatterns.psiElement()
                .withSuperParent(
                    5,
                    psi(YAMLKeyValue::class.java)
                        .withName(PlatformPatterns.string().equalTo(keyword))
                ),
            PlatformPatterns.psiElement()
                .withSuperParent(
                    4,
                    psi(YAMLKeyValue::class.java)
                        .withName(PlatformPatterns.string().equalTo(keyword))
                )
        )
    }
}