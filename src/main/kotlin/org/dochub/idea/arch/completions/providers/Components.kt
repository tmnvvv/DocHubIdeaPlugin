package org.dochub.idea.arch.completions.providers

import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement
import org.dochub.idea.arch.completions.CompletionKey


class Components : FilteredCustomProvider() {

    override val rootPattern: ElementPattern<out PsiElement> = Root.makeRootPattern(KEYWORD);
    override val keys: Collection<CompletionKey> = COMPLETION_KEYS
    override val keyDocumentLevel: Int = KEY_DOCUMENT_LEVEL

    companion object {
        private const val KEYWORD = "components"
        private const val KEY_DOCUMENT_LEVEL = 2

        private val COMPLETION_KEYS = listOf(
            CompletionKey("title", CompletionKey.ValueType.TEXT),
            CompletionKey("entity", CompletionKey.ValueType.TEXT),
            CompletionKey("aspects", CompletionKey.ValueType.LIST),
            CompletionKey("links", CompletionKey.ValueType.LIST),
            CompletionKey("technologies", CompletionKey.ValueType.LIST)
        )

        val rootPattern: ElementPattern<out PsiElement> = Root.makeRootPattern(KEYWORD)
    }
}
