package org.dochub.idea.arch.completions.providers.contexts

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext
import org.dochub.idea.arch.completions.providers.CustomProvider
import org.jetbrains.yaml.psi.YAMLKeyValue

class UmlNotations : CustomProvider() {

    override fun appendToCompletion(completion: CompletionContributor) {
        completion.extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement()
                .withSuperParent(
                    2,
                    psi(YAMLKeyValue::class.java)
                        .withName(PlatformPatterns.string().equalTo(keyword))
                        .and(Uml.rootPattern)
                ),
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
        private const val keyword = "\$notation"
        private val keys = arrayOf(
            "Sber", "C4Model", "plantuml"
        )
    }
}
