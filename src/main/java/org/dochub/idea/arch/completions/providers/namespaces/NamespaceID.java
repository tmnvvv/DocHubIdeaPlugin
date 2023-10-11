package org.dochub.idea.arch.completions.providers.namespaces;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import org.dochub.idea.arch.completions.providers.suggets.IDSuggestComplex;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;

public class NamespaceID extends IDSuggestComplex {

    private static final String keyword = "namespaces";

    @Override
    protected String[] getSections() {
        return new String[]{"components", "contexts", "aspects"};
    }

    @Override
    protected ElementPattern<? extends PsiElement> getPattern() {
        return PlatformPatterns.or(
                        PlatformPatterns.psiElement()
                                .withSuperParent(2, psi(YAMLMapping.class))
                                .withSuperParent(3,
                                        psi(YAMLKeyValue.class)
                                                .withName(PlatformPatterns.string().equalTo(keyword))
                                                .withSuperParent(2, psi(YAMLDocument.class))
                                ),
                        PlatformPatterns.psiElement()
                                .withSuperParent(2,
                                        psi(YAMLKeyValue.class)
                                                .withName(PlatformPatterns.string().equalTo(keyword))
                                                .withSuperParent(2, psi(YAMLDocument.class))
                                )
                );
    }

}
