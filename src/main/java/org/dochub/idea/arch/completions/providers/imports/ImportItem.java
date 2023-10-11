package org.dochub.idea.arch.completions.providers.imports;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.dochub.idea.arch.completions.providers.CustomProvider;
import org.dochub.idea.arch.utils.PsiUtils;
import org.dochub.idea.arch.utils.SuggestUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.util.List;

public class ImportItem extends CustomProvider {

    private static final String keyword = "imports";

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
                                    .withSuperParent(2, psi(YAMLDocument.class))
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
                        try {
                            VirtualFile thisFile = FileDocumentManager.getInstance().getFile(
                                    parameters.getEditor().getDocument()
                            );

                            if (thisFile != null) {
                                List<String> suggests = SuggestUtils.scanDirByContext(
                                        thisFile.getParent().getPath(),
                                        PsiUtils.getText(parameters.getPosition().getContext()),
                                        new String[]{".yaml"}
                                );

                                for (String suggest : suggests) {
                                    resultSet.addElement(LookupElementBuilder.create(suggest));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }
}
