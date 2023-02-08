package org.dochub.idea.arch.completions.providers.contexts

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import org.dochub.idea.arch.completions.providers.Contexts
import org.dochub.idea.arch.completions.providers.CustomProvider
import org.jetbrains.yaml.psi.YAMLKeyValue

class Uml : CustomProvider() {
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
        private const val keyword = "uml"
        private val keys = arrayOf(
            "\$notation", "\$autor", "\$moment", "\$before", "\$after"
        )
        val rootPattern: ElementPattern<out PsiElement?> = PlatformPatterns.or(
            PlatformPatterns.psiElement()
                .withSuperParent(
                    2,
                    psi(YAMLKeyValue::class.java)
                        .withName(PlatformPatterns.string().equalTo(keyword))
                        .and(Contexts.rootPattern)
                ),
            PlatformPatterns.psiElement()
                .withSuperParent(
                    3,
                    psi(YAMLKeyValue::class.java)
                        .withName(PlatformPatterns.string().equalTo(keyword))
                        .and(Contexts.rootPattern)
                )
        )
    }
}
