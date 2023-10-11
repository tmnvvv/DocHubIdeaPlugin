package org.dochub.idea.arch.inspections;

import com.intellij.codeInspection.*;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// Смысла в использовании инеспектора пока нет
public class DocHubInspection extends LocalInspectionTool {
    private static final Logger LOG = Logger.getInstance(DocHubInspection.class);

    @Override
    public ProblemDescriptor[] checkFile(@NotNull final PsiFile psiFile,
                                         @NotNull final InspectionManager manager,
                                         final boolean isOnTheFly) {
        if (InjectedLanguageManager.getInstance(manager.getProject()).isInjectedFragment(psiFile)) {
            // Игнорируем вставки кода
            return noProblemsFound();
        }

        PsiElement[] errors = PsiTreeUtil.collectElements(psiFile, element -> {
            for(ElementPattern<? extends PsiElement> pattern: EmptyErrorPatterns.getPatterns()) {
                if(pattern.accepts(element)) return true;
            }
            return false;
        });

        List<CommonProblemDescriptor> problems = new ArrayList<>();
        for(PsiElement element : errors) {
            problems.add(manager.createProblemDescriptor(
                    element,
                    "Field is required",
                    (LocalQuickFix) null,
                    ProblemHighlightType.ERROR,
                    true));
        }

        return problems.toArray(ProblemDescriptor.EMPTY_ARRAY);
    }

    @NotNull
    private ProblemDescriptor[] noProblemsFound() {
        return ProblemDescriptor.EMPTY_ARRAY;
    }

    @Nullable
    private Module moduleOf(@NotNull final PsiFile psiFile) {
        return ModuleUtil.findModuleForPsiElement(psiFile);
    }

}
