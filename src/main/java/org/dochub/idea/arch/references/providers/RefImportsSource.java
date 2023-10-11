package org.dochub.idea.arch.references.providers;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLSequenceItem;

public class RefImportsSource extends RefBaseSource {

    private static final String keyword = "imports";

    @Override
    public ElementPattern<? extends PsiElement> getRefPattern() {
        return PlatformPatterns.psiElement()
                .withParent(psi(YAMLSequenceItem.class)
                        .withSuperParent(2,
                                psi(YAMLKeyValue.class)
                                        .withName(PlatformPatterns.string().equalTo(keyword))
                                        .withSuperParent(2, psi(YAMLDocument.class))
                        )
                );
    }
}
