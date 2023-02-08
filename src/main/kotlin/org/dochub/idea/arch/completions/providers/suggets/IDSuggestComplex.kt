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
import org.dochub.idea.arch.indexing.SectionData
import org.dochub.idea.arch.utils.getYamlDocumentByPsiElement
import org.dochub.idea.arch.utils.scanYamlPsiTreeToID

open class IDSuggestComplex : BaseSuggest() {
    override val pattern: ElementPattern<out PsiElement>
        get() = PlatformPatterns.psiElement()
    open val sections: Array<String>
        get() = arrayOf()
    private val cacheSectionKey: Key<CachedValue<List<String>>> = Key.create("\$complex-ids")
    override fun appendToCompletion(completion: CompletionContributor) {
        completion.extend(
            CompletionType.BASIC,
            pattern,
            object : CompletionProvider<CompletionParameters>() {
                public override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    resultSet: CompletionResultSet
                ) {
                    val psiPosition = parameters.position
                    val project = parameters.position.project
                    val document: PsiElement = getYamlDocumentByPsiElement(psiPosition)
                    val cacheManager = CachedValuesManager.getManager(project)
                    val ids = CachedValuesManager.getCachedValue(
                        parameters.originalFile,
                        cacheSectionKey,
                        CachedValueProvider {
                            val suggest: MutableList<String> =
                                ArrayList()
                            val globalCache: Map<String, SectionData> = getProjectCache(project)
                            if (globalCache != null) {
                                for (section in sections) {
                                    val localIds: List<String> = scanYamlPsiTreeToID(document, section)
                                    for (id in localIds) if (suggest.indexOf(id) < 0) suggest.add(id)
                                    val projectIds: SectionData? = globalCache[section]
                                    if (section != null) {
                                        if (projectIds != null) {
                                            for (id in projectIds.ids.keys) {
                                                if (suggest.indexOf(id) < 0) suggest.add(id)
                                            }
                                        }
                                    }
                                }
                            }
                            CachedValueProvider.Result.create<List<String>>(
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