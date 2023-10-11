package org.dochub.idea.arch.annotators;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class SyntaxHighlighter {
    public static final TextAttributesKey SEPARATOR =
            createTextAttributesKey("DOCHUB_ANT_SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey IDENTIFIER =
            createTextAttributesKey("DOCHUB_ANT_ID", DefaultLanguageHighlighterColors.IDENTIFIER);
}
