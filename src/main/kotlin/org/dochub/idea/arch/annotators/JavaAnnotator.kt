package org.dochub.idea.arch.annotators

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import org.dochub.idea.arch.annotators.DocHubAnnotator.parseComment

class JavaAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is PsiComment) {
            return
        }
        val refs = parseComment(element)
        for (i in refs.indices) {
            val ref = refs[i]
            val offset = element.getTextRange().startOffset + ref.start
            val prefixRange = TextRange.from(offset, ref.prefix!!.length)
            val idRange = TextRange.from(offset, ref.prefix!!.length + ref.entity!!.length + ref.id!!.length + 1)
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(prefixRange).textAttributes(DefaultLanguageHighlighterColors.KEYWORD).create()
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(idRange).textAttributes(SyntaxHighlighter.IDENTIFIER).create()
        }
    }
}
