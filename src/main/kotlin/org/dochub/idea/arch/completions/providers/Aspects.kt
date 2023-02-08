package org.dochub.idea.arch.completions.providers

import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement
import org.dochub.idea.arch.completions.CompletionKey

class Aspects : FilteredCustomProvider() {

    override val rootPattern: ElementPattern<out PsiElement> = Root.makeRootPattern(KEYWORD)
    override val keys: Collection<CompletionKey> = COMPLETION_KEYS
    override val keyDocumentLevel: Int = 2

    companion object {
        private const val KEYWORD = "aspects"
        private val COMPLETION_KEYS = listOf(
            CompletionKey("title"),
            CompletionKey("location")
        )
        val rootPattern: ElementPattern<out PsiElement> = Root.makeRootPattern(KEYWORD)
    }
}
