package org.dochub.idea.arch.completions.providers;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.dochub.idea.arch.completions.CompletionKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLTokenTypes;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLScalar;

import java.util.Collection;
import java.util.List;

public class Root extends FilteredCustomProvider {
    private static final Collection<CompletionKey> COMPLETION_KEYS = List.of(
            new CompletionKey("imports", CompletionKey.ValueType.LIST),
            new CompletionKey("aspects", CompletionKey.ValueType.MAP),
            new CompletionKey("components", CompletionKey.ValueType.MAP),
            new CompletionKey("contexts", CompletionKey.ValueType.MAP),
            new CompletionKey("docs", CompletionKey.ValueType.MAP),
            new CompletionKey("forms", CompletionKey.ValueType.MAP),
            new CompletionKey("technologies", CompletionKey.ValueType.MAP),
            new CompletionKey("datasets", CompletionKey.ValueType.MAP)
    );

    private static final ElementPattern<? extends PsiElement> ROOT_PATTERN = PlatformPatterns.or(
            psi(YAMLTokenTypes.TEXT)
                    .withParents(YAMLScalar.class, YAMLMapping.class, YAMLDocument.class)
            , psi(YAMLTokenTypes.TEXT)
                    .withParents(YAMLScalar.class, YAMLDocument.class)
    );

    private static final PatternCondition<PsiElement> isNextLine = new PatternCondition<PsiElement>("") {
        @Override
        public boolean accepts(@NotNull PsiElement psiElement, ProcessingContext context) {
//            String text = PsiUtils.getText(psiElement.getContext());
            return true;
        }
    };

    public static ElementPattern<? extends PsiElement> makeRootPattern(String keyword) {
        return PlatformPatterns.or(
                PlatformPatterns.psiElement()
                        .with(isNextLine)
                        .withSuperParent(5,
                                psi(YAMLKeyValue.class)
                                        .withName(PlatformPatterns.string().equalTo(keyword))
                        )
                        .withSuperParent(7, psi(YAMLDocument.class))
                ,
                PlatformPatterns.psiElement()
                        .with(isNextLine)
                        .withSuperParent(4,
                                psi(YAMLKeyValue.class)
                                        .withName(PlatformPatterns.string().equalTo(keyword))
                        )
                        .withSuperParent(6, psi(YAMLDocument.class))
        );
    }

    ;

    @Override
    protected @NotNull ElementPattern<? extends PsiElement> getRootPattern() {
        return ROOT_PATTERN;
    }

    @Override
    protected @NotNull Collection<CompletionKey> getKeys() {
        return COMPLETION_KEYS;
    }

    @Override
    protected int getKeyDocumentLevel() {
        return 0;
    }
}
