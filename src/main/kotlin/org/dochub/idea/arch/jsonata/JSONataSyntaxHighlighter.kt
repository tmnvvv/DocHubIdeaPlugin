package org.dochub.idea.arch.jsonata

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.dochub.idea.arch.jsonata.psi.JSONataTypes


class JSONataSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer {
        return JSONataLexerAdapter()
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey?> {
        if (tokenType == JSONataTypes.COMMENT) {
            return COMMENT_KEYS
        } else if (tokenType == JSONataTypes.VARIABLE) {
            return VARIABLE_KEYS
        } else if (tokenType == JSONataTypes.STRING) {
            return STRING_KEYS
        }
        return EMPTY_KEYS
    }

    companion object {
        private val STRING_KEYS = arrayOf(DefaultLanguageHighlighterColors.STRING)
        private val VARIABLE_KEYS = arrayOf(DefaultLanguageHighlighterColors.KEYWORD)
        private val COMMENT_KEYS = arrayOf(DefaultLanguageHighlighterColors.LINE_COMMENT)
        private val EMPTY_KEYS = arrayOfNulls<TextAttributesKey>(0)
    }
}
