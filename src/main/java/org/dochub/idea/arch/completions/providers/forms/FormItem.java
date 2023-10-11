package org.dochub.idea.arch.completions.providers.forms;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.dochub.idea.arch.completions.providers.CustomProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;

public class FormItem extends CustomProvider {

    private static final String keyword = "forms";

    private static final String[] keys = {
            "entity", "fields"
    };

    public static final ElementPattern<? extends PsiElement> rootPattern = PlatformPatterns.or(
            PlatformPatterns.psiElement()
                    .withSuperParent(4,
                            psi(YAMLKeyValue.class)
                                    .withName(PlatformPatterns.string().equalTo(keyword))
                                    .withSuperParent(2, psi(YAMLDocument.class))
                    ),
            PlatformPatterns.psiElement()
                    .withSuperParent(5,
                            psi(YAMLKeyValue.class)
                                    .withName(PlatformPatterns.string().equalTo(keyword))
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
