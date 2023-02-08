package org.dochub.idea.arch.completions.providers.suggets

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.util.Key
import com.intellij.psi.PsiElement
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.util.ProcessingContext
import org.dochub.idea.arch.indexing.SectionData
import org.dochub.idea.arch.utils.getYamlDocumentByPsiElement
import org.dochub.idea.arch.utils.scanYamlPsiTreeToLocation

open class LocationSuggest : BaseSuggest() {
    private var cacheSectionKey: Key<CachedValue<List<String>>> = Key.create(section + "-loc")
    protected open val section: String
        protected get() = "undefined"


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
                    val locations = CachedValuesManager.getCachedValue(
                        parameters.originalFile,
                        cacheSectionKey,
                        CachedValueProvider {
                            val suggest: MutableList<String> = scanYamlPsiTreeToLocation(document, section)
                            val globalCache: Map<String, SectionData> = getProjectCache(project)
                            val section: SectionData? = globalCache[section]
                            if (section != null) {
                                for (i in 0 until section.locations.size) suggest.add(section.locations.get(i))
                            }
                            CachedValueProvider.Result.create<List<String>>(
                                suggest,
                                PsiModificationTracker.MODIFICATION_COUNT,
                                ProjectRootManager.getInstance(project)
                            )
                        }
                    )
                    for (location in locations) {
                        resultSet.addElement(LookupElementBuilder.create(location))
                    }
                }
            }
        )
    }
}