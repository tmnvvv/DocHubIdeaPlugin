package org.dochub.idea.arch.completions.providers.contexts;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.dochub.idea.arch.completions.providers.Contexts;
import org.dochub.idea.arch.completions.providers.CustomProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;

public class Uml extends CustomProvider {
    private static final String keyword = "uml";
    private static final String[] keys = {
            "$notation", "$autor", "$moment", "$before", "$after"
    };

    public static final ElementPattern<? extends PsiElement> rootPattern = PlatformPatterns.or(
            PlatformPatterns.psiElement()
                    .withSuperParent(2,
                            psi(YAMLKeyValue.class)
                                    .withName(PlatformPatterns.string().equalTo(keyword))
                                    .and(Contexts.rootPattern)
                    ),
            PlatformPatterns.psiElement()
                    .withSuperParent(3,
                            psi(YAMLKeyValue.class)
                                    .withName(PlatformPatterns.string().equalTo(keyword))
                                    .and(Contexts.rootPattern)
                    )
    );

    @Override
    public void appendToCompletion(CompletionContributor completion) {
        completion.extend(
                CompletionType.BASIC,
                rootPattern,
                new CompletionProvider<>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               @NotNull ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        for (final String key : keys) {
                            resultSet.addElement(LookupElementBuilder.create(key));
                        }
                    }
                }
        );
    }

}
