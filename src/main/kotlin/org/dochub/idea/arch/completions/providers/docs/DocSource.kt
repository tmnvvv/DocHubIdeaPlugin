package org.dochub.idea.arch.completions.providers.docs

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import org.dochub.idea.arch.completions.providers.CustomProvider
import org.dochub.idea.arch.completions.providers.Docs
import org.dochub.idea.arch.utils.scanDirByContext
import org.jetbrains.yaml.psi.YAMLKeyValue

class DocSource : CustomProvider() {
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
                    try {
                        val thisFile = FileDocumentManager.getInstance().getFile(
                            parameters.editor.document
                        )
                        if (thisFile != null) {
                            val suggests: List<String> = scanDirByContext(
                                thisFile.parent.path,
                                parameters.position.context!!.text
                                    .replaceFirst("IntellijIdeaRulezzz".toRegex(), ""), arrayOf(".yaml", ".md", ".puml")
                            )
                            for (suggest in suggests) {
                                resultSet.addElement(LookupElementBuilder.create(suggest))
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        )
    }

    companion object {
        private const val keyword = "source"
        val rootPattern: ElementPattern<out PsiElement?> = PlatformPatterns.psiElement()
            .withSuperParent(
                2,
                psi(YAMLKeyValue::class.java)
                    .withName(PlatformPatterns.string().equalTo(keyword))
                    .and(Docs.rootPattern)
            )
    }
}
