package org.dochub.idea.arch.completions.providers.technologies;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import org.dochub.idea.arch.completions.CompletionKey;
import org.dochub.idea.arch.completions.providers.FilteredCustomProvider;
import org.dochub.idea.arch.completions.providers.Technologies;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.util.Collection;
import java.util.List;

public class SectionItem extends FilteredCustomProvider {

    private static final String keyword = "items";

    private static final Collection<CompletionKey> COMPLETION_KEYS = List.of(
            new CompletionKey("title"),
            new CompletionKey("status"),
            new CompletionKey("section"),
            new CompletionKey("link"),
            new CompletionKey("aliases", CompletionKey.ValueType.LIST)
    );

    public static final ElementPattern<? extends PsiElement> rootPattern = PlatformPatterns.or(
            PlatformPatterns.psiElement()
                    .withSuperParent(4,
                            psi(YAMLKeyValue.class)
                                    .withName(PlatformPatterns.string().equalTo(keyword))
                                    .and(Technologies.rootPattern)
                    ),
            PlatformPatterns.psiElement()
                    .withSuperParent(5,
                            psi(YAMLKeyValue.class)
                                    .withName(PlatformPatterns.string().equalTo(keyword))
                                    .and(Technologies.rootPattern)
                    )
    );

    @Override
    protected @NotNull ElementPattern<? extends PsiElement> getRootPattern() {
        return rootPattern;
    }

    @Override
    protected @NotNull Collection<CompletionKey> getKeys() {
        return COMPLETION_KEYS;
    }

    @Override
    protected int getKeyDocumentLevel() {
        return 3;
    }
}
