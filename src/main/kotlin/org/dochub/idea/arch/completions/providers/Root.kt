package org.dochub.idea.arch.completions.providers

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import org.dochub.idea.arch.completions.CompletionKey
import org.jetbrains.yaml.YAMLTokenTypes
import org.jetbrains.yaml.psi.YAMLDocument
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLMapping
import org.jetbrains.yaml.psi.YAMLScalar


class Root : FilteredCustomProvider() {

    override val rootPattern: ElementPattern<out PsiElement> = ROOT_PATTERN
    override val keys: Collection<CompletionKey> = Root.COMPLETION_KEYS
    override val keyDocumentLevel: Int = 0


    companion object {

        private val COMPLETION_KEYS = listOf(
            CompletionKey("imports", CompletionKey.ValueType.LIST),
            CompletionKey("aspects", CompletionKey.ValueType.MAP),
            CompletionKey("components", CompletionKey.ValueType.MAP),
            CompletionKey("contexts", CompletionKey.ValueType.MAP),
            CompletionKey("docs", CompletionKey.ValueType.MAP),
            CompletionKey("forms", CompletionKey.ValueType.MAP),
            CompletionKey("technologies", CompletionKey.ValueType.MAP),
            CompletionKey("datasets", CompletionKey.ValueType.MAP)
        )

        private val ROOT_PATTERN: ElementPattern<out PsiElement> = PlatformPatterns.or(
            psi(YAMLTokenTypes.TEXT)
                .withParents(YAMLScalar::class.java, YAMLMapping::class.java, YAMLDocument::class.java),
            psi(YAMLTokenTypes.TEXT)
                .withParents(YAMLScalar::class.java, YAMLDocument::class.java)
        )

        private val isNextLine: PatternCondition<PsiElement> = object : PatternCondition<PsiElement>("") {
            override fun accepts(psiElement: PsiElement, context: ProcessingContext): Boolean {
                return true
            }
        }

        fun makeRootPattern(keyword: String): ElementPattern<out PsiElement> {
            return PlatformPatterns.or(
                PlatformPatterns.psiElement()
                    .with(isNextLine)
                    .withSuperParent(
                        5,
                        psi(YAMLKeyValue::class.java)
                            .withName(PlatformPatterns.string().equalTo(keyword))
                    )
                    .withSuperParent(7, psi(YAMLDocument::class.java)),
                PlatformPatterns.psiElement()
                    .with(isNextLine)
                    .withSuperParent(
                        4,
                        psi(YAMLKeyValue::class.java)
                            .withName(PlatformPatterns.string().equalTo(keyword))
                    )
                    .withSuperParent(6, psi(YAMLDocument::class.java))
            )
        }
    }
}
