package org.dochub.idea.arch.references.providers;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.dochub.idea.arch.indexing.CacheBuilder;
import org.dochub.idea.arch.utils.PsiUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RefBaseID extends BaseReferencesProvider {
    protected String getKeyword() {
        return "undefined";
    }

    public static ElementPattern<? extends PsiElement>  makeSourcePattern(String keyword, String id) {
        return PlatformPatterns.psiElement(YAMLKeyValue.class)
                .withName(id)
                .notEmpty()
                .withSuperParent(2,
                        psi(YAMLKeyValue.class)
                                .withName(PlatformPatterns.string().equalTo(keyword))
                                .withSuperParent(2, psi(YAMLDocument.class))
                );
    }

    @Override
    public ElementPattern<? extends PsiElement> getSourcePattern(Object ref) {
        return makeSourcePattern(getKeyword(), (String)ref);
    }

    private class FileSourceReference extends PsiReferenceBase {
        PsiFile source;
        String targetID;

        public FileSourceReference(@NotNull PsiElement element, String targetID, PsiFile source) {
            super(element);
            this.source = source;
            this.targetID = targetID;
        }

        @Override
        public @NotNull TextRange getAbsoluteRange() {
            return super.getAbsoluteRange();
        }

        @Override
        public @Nullable PsiElement resolve() {
            List<PsiElement> elements = new ArrayList<>();

            PsiTreeUtil.processElements(source, element -> {
                if (getSourcePattern(this.targetID).accepts(element)) {
                    elements.add(element);
                    return false;
                }
                return true;
            });

            return elements.size() > 0 ? elements.get(0) : source;
        }

        @Override
        public Object @NotNull [] getVariants() {
            return super.getVariants();
        }
    }

    @Override
    public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                           @NotNull ProcessingContext context) {
        Project project = element.getManager().getProject();
        String id = null;
        if (element instanceof YAMLKeyValue)
            id = PsiUtils.getText(((YAMLKeyValue) element).getKey());
        else if (element instanceof YAMLPlainTextImpl)
            id = PsiUtils.getText(element);
        else
            return PsiReference.EMPTY_ARRAY;

        Map<String, CacheBuilder.SectionData> cache = CacheBuilder.getProjectCache(project);
        CacheBuilder.SectionData components = cache == null ? null : cache.get(getKeyword());
        List<PsiReference> refs = new ArrayList<>();

        if (id != null && components != null) {
            List<VirtualFile> files = (List<VirtualFile>) components.ids.get(id);
            if (files != null) {
                for (int i = 0; i < files.size(); i++) {
                    PsiFile targetFile = PsiManager.getInstance(project).findFile(files.get(i));
                    refs.add(new FileSourceReference(element, id, targetFile));
                }
            }
        }

        PsiReference[] result = PsiReference.EMPTY_ARRAY;

        if (refs.size() > 0) {
            result = new PsiReference[refs.size()];
            refs.toArray(result);
        }
        ;

        return result;
    }
}
