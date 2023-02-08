package org.dochub.idea.arch.completions.providers

import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement
import org.dochub.idea.arch.completions.CompletionKey


class Contexts : FilteredCustomProvider() {

    override val rootPattern: ElementPattern<out PsiElement> = Root.makeRootPattern(KEYWORD)
    override val keys: Collection<CompletionKey> = COMPLETION_KEYS
    override val keyDocumentLevel: Int = KEY_DOCUMENT_LEVEL


    companion object {
        private const val KEYWORD = "contexts"
        private const val KEY_DOCUMENT_LEVEL = 2

        private val COMPLETION_KEYS = listOf(
            CompletionKey("title", CompletionKey.ValueType.TEXT),
            CompletionKey("location", CompletionKey.ValueType.TEXT),
            CompletionKey("extra-links", CompletionKey.ValueType.TEXT),
            CompletionKey("components", CompletionKey.ValueType.LIST),
            CompletionKey("uml", CompletionKey.ValueType.MAP)
        )
        val rootPattern: ElementPattern<out PsiElement?> = Root.makeRootPattern(KEYWORD)
    }
}
