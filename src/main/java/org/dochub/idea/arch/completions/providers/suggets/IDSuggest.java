package org.dochub.idea.arch.completions.providers.suggets;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.Key;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.util.ProcessingContext;
import org.dochub.idea.arch.indexing.CacheBuilder;
import org.dochub.idea.arch.utils.PsiUtils;
import org.dochub.idea.arch.utils.SuggestUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class IDSuggest extends BaseSuggest {

    private final Key cacheSectionKey;
    protected ElementPattern<? extends PsiElement> getPattern() {
        return PlatformPatterns.psiElement();
    }


    protected String getSection() {
        return "undefined";
    }

    public IDSuggest() {
        cacheSectionKey = Key.create(getSection() + "-ids");
    }

    @Override
    public void appendToCompletion(CompletionContributor completion) {
        completion.extend(
                CompletionType.BASIC,
                getPattern(),
                new CompletionProvider<>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               @NotNull ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        PsiElement psiPosition = parameters.getPosition();
                        Project project = parameters.getPosition().getProject();
                        PsiElement document = PsiUtils.getYamlDocumentByPsiElement(psiPosition);

                        CachedValuesManager cacheManager = CachedValuesManager.getManager(project);

                        List<String> ids =  cacheManager.getCachedValue(
                                parameters.getOriginalFile(),
                                cacheSectionKey,
                                () -> {
                                    List<String> suggest = SuggestUtils.scanYamlPsiTreeToID(document, getSection());
                                    Map<String, CacheBuilder.SectionData> globalCache = getProjectCache(project);
                                    if (globalCache != null) {
                                        CacheBuilder.SectionData section = globalCache.get(getSection());
                                        if (section != null) {
                                            for (String id : section.ids.keySet()) {
                                                if (suggest.indexOf(id) < 0)
                                                    suggest.add(id);
                                            }
                                        }
                                    }

                                    return CachedValueProvider.Result.create(
                                            suggest,
                                            PsiModificationTracker.MODIFICATION_COUNT,
                                            ProjectRootManager.getInstance(project)
                                    );
                                }
                        );

                        for (String id : ids) {
                            resultSet.addElement(LookupElementBuilder.create(id));
                        }
                    }
                }
        );
    }
}
