package org.dochub.idea.arch.completions.providers

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import org.dochub.idea.arch.completions.CompletionKey
import org.jetbrains.yaml.psi.YAMLDocument
import org.jetbrains.yaml.psi.YAMLKeyValue
import java.util.List


class Technologies : FilteredCustomProvider() {


    override val rootPattern: ElementPattern<out PsiElement> = Technologies.rootPattern

    override val keys: Collection<CompletionKey> = COMPLETION_KEYS

    override val keyDocumentLevel: Int = 1


    companion object {

        const val KEYWORD = "technologies"
        val keys = listOf("sections", "items")

        private val COMPLETION_KEYS: Collection<CompletionKey> = List.of(
            CompletionKey("sections", CompletionKey.ValueType.MAP),
            CompletionKey("items", CompletionKey.ValueType.MAP)
        )


        val rootPattern: ElementPattern<out PsiElement?> = PlatformPatterns.or(
            PlatformPatterns.psiElement()
                .withSuperParent(
                    2,
                    psi(YAMLKeyValue::class.java)
                        .withName(PlatformPatterns.string().equalTo(KEYWORD))
                )
                .withSuperParent(4, psi(YAMLDocument::class.java)),
            PlatformPatterns.psiElement()
                .withSuperParent(
                    3,
                    psi(YAMLKeyValue::class.java)
                        .withName(PlatformPatterns.string().equalTo(KEYWORD))
                )
                .withSuperParent(5, psi(YAMLDocument::class.java))
        )
    }
}
