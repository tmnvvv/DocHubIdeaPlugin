package org.dochub.idea.arch.markline

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import org.dochub.idea.arch.annotators.DocHubAnnotator.parseComment

class LineMarkerJava : LineMarkerProviderDescriptor() {

    override fun collectSlowLineMarkers(
        elements: MutableList<out PsiElement>,
        result: MutableCollection<in LineMarkerInfo<*>>
    ) {
        var i = 0
        val size = elements.size
        while (i < size) {
            val element = elements[i]
            collectNavigationMarkers(element, result)
            i++
        }
    }


    protected fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in LineMarkerInfo<*>>
    ) {
    }

    override fun getName(): String? {
        return null
    }

    private fun getLineMarkerInfoForAnnotator(element: PsiElement): LineMarkerInfo<*>? {
        var result: LineMarkerInfo<*>? = null
        val refs = parseComment(element)
        if (refs.size > 0) {
            val ref = refs[0]
            result = makeLineMarkerInfo(
                DocHubNavigationHandler(ref.entity, ref.id),
                element
            )
        }
        return result
    }

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        var result: LineMarkerInfo<*>? = null
        if (element is PsiComment) {
            result = getLineMarkerInfoForAnnotator(element)
        }
        return result
    }
}
