package org.dochub.idea.arch.annotators;

import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocHubAnnotator {
    static public class EntityRef {
        public int start;
        public String prefix;
        public String entity;
        public String id;
    }

    static public List<EntityRef> parseComment(PsiElement element) {
        PsiComment literalExpression = (PsiComment) element;
        String comment = literalExpression.getText() != null ? literalExpression.getText() : null;

        ArrayList<EntityRef> result = new ArrayList<>();
        Matcher groups = Pattern.compile("(\\@dochub\\:)([a-z]+)\\/([a-zA-Z0-9\\.\\_\\-]+)").matcher(comment);
        while(groups.find()) {
            EntityRef ref = new EntityRef();
            ref.start = groups.start();
            ref.prefix = groups.group(1);
            ref.entity = groups.group(2);
            ref.id = groups.group(3);
            result.add(ref);
        }
        return result;
    }
}
