package org.dochub.idea.arch.completions.providers.docs;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import org.dochub.idea.arch.completions.providers.Docs;
import org.dochub.idea.arch.completions.providers.suggets.LocationSuggestDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;

public class DocLocation extends LocationSuggestDocument {

    private static final String keyword = "location";

    @Override
    protected ElementPattern<? extends PsiElement> getPattern() {
        return PlatformPatterns.psiElement()
                        .withSuperParent(2,
                                psi(YAMLKeyValue.class)
                                        .withName(PlatformPatterns.string().equalTo(keyword))
                                        .and(Docs.rootPattern)
                        );
    }
}
