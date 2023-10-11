package org.dochub.idea.arch.findUsages

import com.intellij.lang.HelpID
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.dochub.idea.arch.wordScanner.JSONataWordScanner

class JSONataFindUsagesProvider : FindUsagesProvider {

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean = psiElement is PsiNamedElement

    override fun getHelpId(psiElement: PsiElement): String = HelpID.FIND_OTHER_USAGES

    override fun getType(element: PsiElement): String = ""

    override fun getDescriptiveName(element: PsiElement): String {
        return if (element is PsiNamedElement) {
            element.name ?: ""
        } else {
            element.text
        }
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        return getDescriptiveName(element)
    }

    override fun getWordsScanner(): WordsScanner {
        return JSONataWordScanner()
    }
}
