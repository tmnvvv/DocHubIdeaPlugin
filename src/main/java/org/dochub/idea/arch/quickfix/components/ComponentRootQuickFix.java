package org.dochub.idea.arch.quickfix.components;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import org.dochub.idea.arch.quickfix.BaseStructureQuickFix;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;

public class ComponentRootQuickFix extends BaseStructureQuickFix {
    public static String[] requiredProps = {
            "title", "entity"
    };

    public ComponentRootQuickFix() {
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
                                .withName(PlatformPatterns.string().equalTo("components"))
                                .withSuperParent(2, psi(YAMLDocument.class))
                );
    }
}
