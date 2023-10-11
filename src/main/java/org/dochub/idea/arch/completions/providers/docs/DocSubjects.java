package org.dochub.idea.arch.completions.providers.docs;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import org.dochub.idea.arch.completions.providers.Docs;
import org.dochub.idea.arch.completions.providers.suggets.IDSuggestComplex;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLSequenceItem;

public class DocSubjects extends IDSuggestComplex {

    private static final String keyword = "subjects";

    @Override
    protected String[] getSections() {
        return new String[]{"components", "aspects"};
    }

    @Override
    protected ElementPattern<? extends PsiElement> getPattern() {
        return PlatformPatterns.psiElement()
                .withSuperParent(2, psi(YAMLSequenceItem.class))
                .withSuperParent(4,
                        psi(YAMLKeyValue.class)
                                .withName(PlatformPatterns.string().equalTo(keyword))
                                .and(Docs.rootPattern)
                );
    }
}
