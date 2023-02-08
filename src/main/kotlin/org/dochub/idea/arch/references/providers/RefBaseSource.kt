package org.dochub.idea.arch.references.providers

import com.intellij.openapi.util.TextRange
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import org.dochub.idea.arch.utils.findFile
import org.dochub.idea.arch.utils.getText


open class RefBaseSource : BaseReferencesProvider() {

    private inner class FileSourceReference(element: PsiElement, var source: PsiFile?) :
        PsiReferenceBase<PsiElement>(element) {
        override fun getAbsoluteRange(): TextRange {
            return super.getAbsoluteRange()
        }

        override fun resolve(): PsiElement? {
            return source
        }

        override fun getVariants(): Array<Any> {
            return super.getVariants()
        }
    }

     override val refPattern: ElementPattern<out PsiElement?>
        get() = PlatformPatterns.psiElement()

    override fun getReferencesByElement(
        element: PsiElement,
        context: ProcessingContext
    ): Array<PsiReference> {
        val project = element.manager.project
        val ref = getText(element)
        val containingFile = element.containingFile
        val currDir = containingFile.parent
        return if (currDir != null) {
            val dirPath = currDir.virtualFile.canonicalPath!!.substring(project.basePath!!.length)
            val vTargetFile = findFile("$dirPath/$ref", project)
            if (vTargetFile != null) {
                val targetFile = PsiManager.getInstance(element.manager.project).findFile(vTargetFile)
                arrayOf(FileSourceReference(element, targetFile))
            } else PsiReference.EMPTY_ARRAY
        } else PsiReference.EMPTY_ARRAY
    }
}
