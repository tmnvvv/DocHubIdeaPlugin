package org.dochub.idea.arch.jsonata;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import org.dochub.idea.arch.jsonata.psi.JSONataTypes;
import org.jetbrains.annotations.NotNull;



public class JSONataSyntaxHighlighter extends SyntaxHighlighterBase {
    private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{TextAttributesKey.createTextAttributesKey("JSONATA_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)};
    private static final TextAttributesKey[] NUMBER_KEYS = new TextAttributesKey[]{TextAttributesKey.createTextAttributesKey("JSONATA_NUMBER", DefaultLanguageHighlighterColors.NUMBER)};
    private static final TextAttributesKey[] OPERATOR_KEYS = new TextAttributesKey[]{TextAttributesKey.createTextAttributesKey("JSONATA_OPERATOR", DefaultLanguageHighlighterColors.INSTANCE_METHOD)};
    private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{TextAttributesKey.createTextAttributesKey("JSONATA_STRING", DefaultLanguageHighlighterColors.STRING)};
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{TextAttributesKey.createTextAttributesKey("JSONATA_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)};
    private static final TextAttributesKey[] BRACKETS_KEYS = new TextAttributesKey[]{TextAttributesKey.createTextAttributesKey("JSONATA_BRACKETS", DefaultLanguageHighlighterColors.CLASS_REFERENCE)};
    private static final TextAttributesKey[] VARIABLES_KEYS = new TextAttributesKey[]{TextAttributesKey.createTextAttributesKey("JSONATA_VARIABLE", DefaultLanguageHighlighterColors.LOCAL_VARIABLE)};
    private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{TextAttributesKey.createTextAttributesKey("JSONATA_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];


    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new JSONataLexerAdapter();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if (JSONataParserDefinition.KEYWORDS.contains(tokenType)) {
            return KEYWORD_KEYS;
        } else if (JSONataParserDefinition.BRACKETS.contains(tokenType)) {
            return BRACKETS_KEYS;
        } else if (JSONataParserDefinition.OPERATORS.contains(tokenType)) {
            return OPERATOR_KEYS;
        } else if (JSONataParserDefinition.NUMBERS.contains(tokenType)) {
            return NUMBER_KEYS;
        } else if (tokenType == JSONataTypes.STRING) {
            return STRING_KEYS;
        } else if (tokenType == JSONataTypes.VARIABLE) {
            return VARIABLES_KEYS;
        } else if (tokenType == JSONataTypes.COMMENT) {
            return COMMENT_KEYS;
        }   else if (tokenType == TokenType.BAD_CHARACTER) {
            return BAD_CHAR_KEYS;
        }
        return EMPTY_KEYS;
    }
}
