package org.dochub.idea.arch.inspections

import com.intellij.codeInspection.*
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil

// Смысла в использовании инеспектора пока нет
class DocHubInspection : LocalInspectionTool() {

    override fun checkFile(
        psiFile: PsiFile,
        manager: InspectionManager,
        isOnTheFly: Boolean
    ): Array<ProblemDescriptor>? {
        if (InjectedLanguageManager.getInstance(manager.project).isInjectedFragment(psiFile)) {
            // Игнорируем вставки кода
            return noProblemsFound()
        }
        val errors = PsiTreeUtil.collectElements(
            psiFile
        ) { element: PsiElement? ->
            for (pattern in getPatterns()!!) {
                if (pattern.accepts(element)) return@collectElements true
            }
            false
        }
        val problems: MutableList<CommonProblemDescriptor> = mutableListOf()
        for (element in errors) {
            problems.add(
                manager.createProblemDescriptor(
                    element!!,
                    "Field is required",
                    null as LocalQuickFix?,
                    ProblemHighlightType.ERROR,
                    true
                )
            )
        }
        return problems.toArray { ProblemDescriptor.EMPTY_ARRAY }
    }

    private fun noProblemsFound(): Array<ProblemDescriptor> {
        return ProblemDescriptor.EMPTY_ARRAY
    }

    private fun moduleOf(psiFile: PsiFile): Module? {
        return ModuleUtil.findModuleForPsiElement(psiFile)
    }

    companion object {
        private val LOG = Logger.getInstance(
            DocHubInspection::class.java
        )
    }
}
