package org.dochub.idea.arch.completions.providers.suggets

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.util.Key
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.util.ProcessingContext
import org.dochub.idea.arch.utils.getYamlDocumentByPsiElement
import org.dochub.idea.arch.utils.scanYamlPsiTreeToID

open class IDSuggest : BaseSuggest() {

    override val pattern: ElementPattern<out PsiElement>
        get() = PlatformPatterns.psiElement()

    private var cacheSectionKey: Key<CachedValue<List<String>>> = Key.create(section + "-ids")

    open val section: String
        get() = "undefined"


    override fun appendToCompletion(completion: CompletionContributor) {
        completion.extend(
            CompletionType.BASIC,
            pattern,
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    resultSet: CompletionResultSet
                ) {
                    val psiPosition = parameters.position
                    val project = parameters.position.project
                    val document: PsiElement = getYamlDocumentByPsiElement(psiPosition)
                    val cacheManager = CachedValuesManager.getManager(project)
                    val ids = CachedValuesManager.getCachedValue<List<String>>(
                        parameters.originalFile,
                        cacheSectionKey,
                        CachedValueProvider {
                            val suggest: MutableList<String> = scanYamlPsiTreeToID(document, section)
                            val globalCache = getProjectCache(project)
                            if (globalCache != null) {
                                val section = globalCache[section]
                                if (section != null) {
                                    for (id in section.ids.keys) {
                                        if (suggest.indexOf(id) < 0) suggest.add(id)
                                    }
                                }
                            }
                            CachedValueProvider.Result.create(
                                suggest,
                                PsiModificationTracker.MODIFICATION_COUNT,
                                ProjectRootManager.getInstance(project)
                            )
                        }
                    )
                    for (id in ids) {
                        resultSet.addElement(LookupElementBuilder.create(id))
                    }
                }
            }
        )
    }
}
