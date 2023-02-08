package org.dochub.idea.arch.completions.providers.imports

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import org.dochub.idea.arch.completions.providers.CustomProvider
import org.dochub.idea.arch.utils.getText
import org.dochub.idea.arch.utils.scanDirByContext
import org.jetbrains.yaml.psi.YAMLDocument
import org.jetbrains.yaml.psi.YAMLKeyValue

class ImportItem : CustomProvider() {
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
                                getText(parameters.position.context), arrayOf(".yaml")
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
        private const val keyword = "imports"
        val rootPattern: ElementPattern<out PsiElement?> = PlatformPatterns.or(
            PlatformPatterns.psiElement()
                .withSuperParent(
                    4,
                    psi(YAMLKeyValue::class.java)
                        .withName(PlatformPatterns.string().equalTo(keyword))
                        .withSuperParent(2, psi(YAMLDocument::class.java))
                ),
            PlatformPatterns.psiElement()
                .withSuperParent(
                    5,
                    psi(YAMLKeyValue::class.java)
                        .withName(PlatformPatterns.string().equalTo(keyword))
                        .withSuperParent(2, psi(YAMLDocument::class.java))
                )
        )
    }
}
