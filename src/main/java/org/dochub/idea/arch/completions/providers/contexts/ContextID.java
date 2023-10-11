package org.dochub.idea.arch.completions.providers.contexts;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import org.dochub.idea.arch.completions.providers.suggets.IDSuggestContexts;
import org.dochub.idea.arch.completions.providers.CustomProvider;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;

public class ContextID extends IDSuggestContexts {
    private static final String keyword = "contexts";

    @Override
    protected ElementPattern<? extends PsiElement> getPattern() {
        return PlatformPatterns.or(
                        PlatformPatterns.psiElement()
                                .withSuperParent(2, CustomProvider.psi(YAMLMapping.class))
                                .withSuperParent(3,
                                        CustomProvider.psi(YAMLKeyValue.class)
                                                .withName(PlatformPatterns.string().equalTo(keyword))
                                                .withSuperParent(2, CustomProvider.psi(YAMLDocument.class))
                                ),
                        PlatformPatterns.psiElement()
                                .withSuperParent(2,
                                        CustomProvider.psi(YAMLKeyValue.class)
                                                .withName(PlatformPatterns.string().equalTo(keyword))
                                                .withSuperParent(2, CustomProvider.psi(YAMLDocument.class))
                                )
                );
    }

}
