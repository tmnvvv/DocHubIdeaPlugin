package org.dochub.idea.arch.inspections;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.dochub.idea.arch.quickfix.aspects.AspectRootQuickFix;
import org.dochub.idea.arch.quickfix.components.ComponentRootQuickFix;
import org.dochub.idea.arch.quickfix.contexts.ContextRootQuickFix;
import org.dochub.idea.arch.quickfix.docs.DocRootQuickFix;
import org.dochub.idea.arch.quickfix.namespaces.NamespaceRootQuickFix;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl;

import java.util.List;

public class EmptyErrorPatterns {

    private static ElementPattern<? extends PsiElement>[] patterns = null;
    private static PatternCondition<PsiElement> emptyChecker = null;

    static ElementPattern<? extends PsiElement>[] getPatterns() {
        if (patterns == null) {
            emptyChecker = new PatternCondition<PsiElement>("Empty checker") {
                @Override
                public boolean accepts(@NotNull PsiElement element, ProcessingContext context) {
                    PsiElement parent = element.getParent();
                    if (parent instanceof YAMLKeyValueImpl) {
                        return ((YAMLKeyValueImpl) parent).getValue() == null;
                    }
                    return false;
                }
            };

            patterns = new ElementPattern[] {
                    makeRootPattern("Doc field checker", "docs", DocRootQuickFix.requiredProps),
                    makeRootPattern("Aspect field checker", "aspects", AspectRootQuickFix.requiredProps),
                    makeRootPattern("Component field checker", "components", ComponentRootQuickFix.requiredProps),
                    makeRootPattern("Context field checker", "contexts", ContextRootQuickFix.requiredProps),
                    makeRootPattern("Namespace field checker", "namespaces", NamespaceRootQuickFix.requiredProps),
            };
        }
        return patterns;
    }

    private static class RequiredFieldChecker extends PatternCondition<PsiElement> {
        List<String> fields = null;

        public RequiredFieldChecker(@Nullable @NonNls String debugMethodName, String []fields) {
            super(debugMethodName);
            this.fields = List.of(fields);
        }

        @Override
        public boolean accepts(@NotNull PsiElement element, ProcessingContext context) {
            return fields.contains(element.getText());
        }
    }

    static ElementPattern<? extends PsiElement> makeRootPattern(
            String debugMethodName,
            String keyword,
            String []fields) {
        return  PlatformPatterns.psiElement()
                .beforeLeaf(":")
                .with(emptyChecker)
                .andOr(
                        PlatformPatterns.psiElement()
                                .with(new RequiredFieldChecker(
                                        debugMethodName,
                                        fields
                                ))
                                .withSuperParent(3, YAMLKeyValue.class)
                                .withSuperParent(5,
                                        PlatformPatterns.psiElement(YAMLKeyValue.class)
                                                .withName(PlatformPatterns.string().equalTo(keyword))
                                )
                                .withSuperParent(7, YAMLDocument.class)
                );
    }
}
