package org.dochub.idea.arch.completions.providers.components;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import org.dochub.idea.arch.completions.providers.Components;
import org.dochub.idea.arch.completions.providers.CustomProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLSequenceItem;

public class ComponentLinks extends CustomProvider {
    private static final String keyword = "links";
    private static final String[] keys = {
            "id", "title", "contract", "direction"
    };

    @Override
    public void appendToCompletion(CompletionContributor completion) {
        completion.extend(
                CompletionType.BASIC,
                PlatformPatterns.or(
                    PlatformPatterns.psiElement()
                            .withSuperParent(3, psi(YAMLSequenceItem.class))
                            .withSuperParent(5,
                                    psi(YAMLKeyValue.class)
                                            .withName(PlatformPatterns.string().equalTo(keyword))
                                            .and(Components.rootPattern)
                            ),
                        PlatformPatterns.psiElement()
                                .withSuperParent(2, psi(YAMLSequenceItem.class))
                                .withSuperParent(4,
                                        psi(YAMLKeyValue.class)
                                                .withName(PlatformPatterns.string().equalTo(keyword))
                                                .and(Components.rootPattern)
                                )
                )
                ,
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
