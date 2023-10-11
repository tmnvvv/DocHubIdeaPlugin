package org.dochub.idea.arch.completions.providers;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;

public class CustomProvider {

    public void appendToCompletion(CompletionContributor completion) {
    }

    public static <T extends PsiElement> PsiElementPattern.Capture<T> psi(final Class<T> aClass) {
        return PlatformPatterns.psiElement(aClass);
    }

    public static PsiElementPattern.Capture<PsiElement> psi(IElementType type) {
        return PlatformPatterns.psiElement(type);
    }

}
