package org.dochub.idea.arch.references.providers

import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import com.intellij.util.containers.toArray
import org.dochub.idea.arch.indexing.SectionData
import org.dochub.idea.arch.indexing.getProjectCache
import org.jetbrains.yaml.psi.YAMLDocument
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl
import org.dochub.idea.arch.utils.getText


open class RefBaseID : BaseReferencesProvider() {

    protected open val keyword: String
        protected get() = "undefined"


    override fun getSourcePattern(ref: Any?): ElementPattern<out PsiElement> {
        return makeSourcePattern(keyword, ref as String?)
    }


    private inner class FileSourceReference(element: PsiElement, var targetID: String, var source: PsiFile) :
        PsiReferenceBase<PsiElement>(element) {

        override fun getAbsoluteRange(): TextRange {
            return super.getAbsoluteRange()
        }

        override fun resolve(): PsiElement {
            val elements: MutableList<PsiElement> = ArrayList()
            PsiTreeUtil.processElements(source) { element: PsiElement ->
                if (getSourcePattern(targetID).accepts(element)) {
                    elements.add(element)
                    return@processElements false
                }
                true
            }
            return if (elements.size > 0) elements[0] else source
        }

        override fun getVariants(): Array<Any> {
            return super.getVariants()
        }
    }

    override fun getReferencesByElement(
        element: PsiElement,
        context: ProcessingContext
    ): Array<PsiReference> {
        val project = element.manager.project
        var id: String? = if (element is YAMLKeyValue) getText(element.key) else if (element is YAMLPlainTextImpl) getText(element) else return PsiReference.EMPTY_ARRAY
        val cache: Map<String, SectionData> = getProjectCache(project)
        val components: SectionData? = cache[keyword]
        val refs: MutableList<PsiReference> = ArrayList()
        if (id != null && components != null) {
            val files = components.ids.get(id) as ArrayList<VirtualFile>
            for (i in files.indices) {
                val targetFile = PsiManager.getInstance(project).findFile(files[i])
                targetFile?.let { FileSourceReference(element, id, it) }?.let { refs.add(it) }
            }
        }
        var result = PsiReference.EMPTY_ARRAY
        if (refs.size > 0) {
            result = arrayOfNulls(refs.size)
            refs.toArray<PsiReference>(result)
        }
        return result
    }

    companion object {
        fun makeSourcePattern(keyword: String, id: String?): ElementPattern<out PsiElement> {
            return PlatformPatterns.psiElement(YAMLKeyValue::class.java)
                .withName(id)
                .notEmpty()
                .withSuperParent(
                    2,
                    psi(YAMLKeyValue::class.java)
                        .withName(PlatformPatterns.string().equalTo(keyword))
                        .withSuperParent(2, psi(YAMLDocument::class.java))
                )
        }
    }
}
