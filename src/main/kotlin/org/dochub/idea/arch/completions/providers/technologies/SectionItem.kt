package org.dochub.idea.arch.completions.providers.technologies

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import org.dochub.idea.arch.completions.CompletionKey
import org.dochub.idea.arch.completions.providers.FilteredCustomProvider
import org.dochub.idea.arch.completions.providers.Technologies
import org.jetbrains.yaml.psi.YAMLKeyValue


class SectionItem : FilteredCustomProvider() {

    override val rootPattern: ElementPattern<out PsiElement> = SectionItem.rootPattern
    override val keys: Collection<CompletionKey> = COMPLETION_KEYS
    override val keyDocumentLevel: Int = 3


    companion object {

        private const val keyword = "items"
        private val COMPLETION_KEYS = listOf(
            CompletionKey("title"),
            CompletionKey("status"),
            CompletionKey("section"),
            CompletionKey("link"),
            CompletionKey("aliases", CompletionKey.ValueType.LIST))

        val rootPattern: ElementPattern<out PsiElement> = PlatformPatterns.or(
            PlatformPatterns.psiElement()
                .withSuperParent(
                    4,
                    psi(YAMLKeyValue::class.java)
                        .withName(PlatformPatterns.string().equalTo(keyword))
                        .and(Technologies.rootPattern)
                ),
            PlatformPatterns.psiElement()
                .withSuperParent(
                    5,
                    psi(YAMLKeyValue::class.java)
                        .withName(PlatformPatterns.string().equalTo(keyword))
                        .and(Technologies.rootPattern)
                )
        )
    }
}
