package org.dochub.idea.arch.completions.providers.datasets;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import org.dochub.idea.arch.completions.providers.Dataset;
import org.dochub.idea.arch.completions.providers.suggets.IDSuggestDatasets;
import org.dochub.idea.arch.completions.providers.CustomProvider;
import org.jetbrains.yaml.psi.YAMLKeyValue;

public class DatasetOrigin extends IDSuggestDatasets {

    private static final String keyword = "origin";

    @Override
    protected ElementPattern<? extends PsiElement> getPattern() {
        return  PlatformPatterns.psiElement()
                .withSuperParent(2,
                        CustomProvider.psi(YAMLKeyValue.class)
                                .withName(PlatformPatterns.string().equalTo(keyword))
                                .and(Dataset.rootPattern)
                );
    }
}
