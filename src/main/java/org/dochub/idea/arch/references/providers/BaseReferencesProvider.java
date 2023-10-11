package org.dochub.idea.arch.references.providers;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.patterns.StandardPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class BaseReferencesProvider  extends PsiReferenceProvider {

    public ElementPattern<? extends PsiElement> getRefPattern() {
        return StandardPatterns.instanceOf(PsiElement.class);
    }

    public ElementPattern<? extends PsiElement> getSourcePattern(Object ref) {
        return StandardPatterns.instanceOf(PsiElement.class);
    }

    public boolean isElementFound(PsiElement element, Object ref) {
        return false;
    }

    @Override
    public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        // return new PsiReference[0];
        return PsiReference.EMPTY_ARRAY;
    }

    public static <T extends PsiElement> PsiElementPattern.Capture<T> psi(final Class<T> aClass) {
        return PlatformPatterns.psiElement(aClass);
    }

    public static PsiElementPattern.Capture<PsiElement> psi(IElementType type) {
        return PlatformPatterns.psiElement(type);
    }
}
