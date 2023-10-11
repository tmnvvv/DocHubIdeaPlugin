package org.dochub.idea.arch.completions.providers.docs;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.dochub.idea.arch.completions.providers.CustomProvider;
import org.dochub.idea.arch.completions.providers.Docs;
import org.dochub.idea.arch.utils.SuggestUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.util.List;

public class DocSource extends CustomProvider {

    private static final String keyword = "source";

    public static final ElementPattern<? extends PsiElement> rootPattern =
            PlatformPatterns.psiElement()
                    .withSuperParent(2,
                            psi(YAMLKeyValue.class)
                                    .withName(PlatformPatterns.string().equalTo(keyword))
                                    .and(Docs.rootPattern)
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
                                        parameters.getPosition().getContext().getText()
                                                .replaceFirst("IntellijIdeaRulezzz", ""),
                                        new String[]{".yaml", ".md", ".puml"}
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
