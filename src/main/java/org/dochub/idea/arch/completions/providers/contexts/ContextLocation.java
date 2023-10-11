package org.dochub.idea.arch.completions.providers.contexts;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import org.dochub.idea.arch.completions.providers.Contexts;
import org.dochub.idea.arch.completions.providers.suggets.LocationSuggestContexts;
import org.dochub.idea.arch.completions.providers.CustomProvider;
import org.jetbrains.yaml.psi.YAMLKeyValue;

public class ContextLocation extends LocationSuggestContexts {
    private static final String keyword = "location";

    @Override
    protected ElementPattern<? extends PsiElement> getPattern() {
        return PlatformPatterns.or(
                PlatformPatterns.psiElement()
                        .withSuperParent(2,
                                CustomProvider.psi(YAMLKeyValue.class)
                                        .withName(PlatformPatterns.string().equalTo(keyword))
                                        .and(Contexts.rootPattern)
                        ),
                PlatformPatterns.psiElement()
                        .withSuperParent(3,
                                CustomProvider.psi(YAMLKeyValue.class)
                                        .withName(PlatformPatterns.string().equalTo(keyword))
                                        .and(Contexts.rootPattern)
                        )
        );
    }
}
