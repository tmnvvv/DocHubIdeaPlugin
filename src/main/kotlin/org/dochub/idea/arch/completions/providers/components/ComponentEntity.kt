package org.dochub.idea.arch.completions.providers.components

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext
import org.dochub.idea.arch.completions.providers.Components
import org.dochub.idea.arch.completions.providers.CustomProvider
import org.jetbrains.yaml.psi.YAMLKeyValue

class ComponentEntity : CustomProvider() {
    override fun appendToCompletion(completion: CompletionContributor) {
        completion.extend(
            CompletionType.BASIC,
            PlatformPatterns.or(
                PlatformPatterns.psiElement()
                    .withSuperParent(
                        2,
                        psi(YAMLKeyValue::class.java)
                            .withName(PlatformPatterns.string().equalTo(keyword))
                            .and(Components.rootPattern)
                    ),
                PlatformPatterns.psiElement()
                    .withSuperParent(
                        3,
                        psi(YAMLKeyValue::class.java)
                            .withName(PlatformPatterns.string().equalTo(keyword))
                            .and(Components.rootPattern)
                    )
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
        private const val keyword = "entity"
        private val keys = arrayOf(
            "package", "node", "folder", "frame", "cloud", "database", "interface",
            "actor", "usecase", "boundary", "control", "entity", "collections", "queue",
            "card", "circle", "file", "hexagon", "label", "person", "stack", "storage",
            "component"
        )
    }
}