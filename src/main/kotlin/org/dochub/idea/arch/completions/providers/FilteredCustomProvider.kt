package org.dochub.idea.arch.completions.providers

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.ElementPattern
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import org.dochub.idea.arch.completions.CompletionKey
import org.dochub.idea.arch.completions.FormattingInsertHandler
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLMapping
import java.util.*
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Predicate
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.reflect.KClass

abstract class FilteredCustomProvider : CustomProvider() {

    protected abstract val rootPattern: ElementPattern<out PsiElement>
    protected abstract val keys: Collection<CompletionKey>
    protected abstract val keyDocumentLevel: Int

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
                    val parent = PsiTreeUtil.getParentOfType(
                        parameters.position,
                        YAMLKeyValue::class.java
                    )
                    var containerToScan: PsiElement? = null
                    containerToScan = if (parent == null) {
                        // Это корень файла
                        parameters.position.parent.context
                    } else {
                        PsiTreeUtil.getChildOfType(parent, YAMLMapping::class.java)
                    }
                    // Тут возможны 2 варианта, либо наш родитель содержит уже филды, либо это просто имя компонента
                    // Если containerToScan != null значит есть хоть 1 поле
                    val alreadyDefinedAttributes = Optional.ofNullable(containerToScan)
                        .map { c: PsiElement ->
                            getChildsOfClass(
                                c,
                                YAMLKeyValue::class.java
                            ).stream()
                                .map { obj: YAMLKeyValue -> obj.keyText }
                                .collect(Collectors.toSet())
                        }
                        .orElse(emptySet())
                    keys.stream().filter(Predicate { k: CompletionKey ->
                        !alreadyDefinedAttributes.contains(
                            k.key
                        )
                    })
                        .map<LookupElementBuilder>(Function { key: CompletionKey ->
                            createLookupElement(
                                key
                            )
                        })
                        .forEach(Consumer { element: LookupElementBuilder? ->
                            resultSet.addElement(
                                element!!
                            )
                        })
                }
            }
        )
    }

    private fun createLookupElement(key: CompletionKey): LookupElementBuilder {
        return LookupElementBuilder.create(key.key)
            .withInsertHandler(FormattingInsertHandler(key, keyDocumentLevel))
    }

    private fun <T : PsiElement?> getChildsOfClass(parent: PsiElement, classz: Class<T>): Collection<T> {
        return Stream.of(*parent.children).filter { obj: PsiElement? ->
            classz.isInstance(
                obj
            )
        }.map { obj: PsiElement? ->
            classz.cast(
                obj
            )
        }.collect(Collectors.toSet())
    }
}
