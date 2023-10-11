package org.dochub.idea.arch.quickfix.contexts;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import org.dochub.idea.arch.quickfix.BaseStructureQuickFix;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;

public class ContextRootQuickFix extends BaseStructureQuickFix {
    public static String[] requiredProps = {
            "title", "location", "components"
    };

    public ContextRootQuickFix() {
        super();
    }

    @Override
    protected String[] getRequiredStructure() {
        return requiredProps;
    }

    @Override
    public ElementPattern<? extends PsiElement> getFixPattern(PsiElement element) {
        return PlatformPatterns.psiElement()
                .beforeLeaf(":")
                .withSuperParent(2, psi(YAMLMapping.class))
                .withSuperParent(3,
                        psi(YAMLKeyValue.class)
                                .withName(PlatformPatterns.string().equalTo("contexts"))
                                .withSuperParent(2, psi(YAMLDocument.class))
                );
    }
}
