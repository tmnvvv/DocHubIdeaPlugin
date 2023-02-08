package org.dochub.idea.arch.annotators

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey

object SyntaxHighlighter {

    val SEPARATOR = TextAttributesKey.createTextAttributesKey(
        "DOCHUB_ANT_SEPARATOR",
        DefaultLanguageHighlighterColors.OPERATION_SIGN
    )
    val IDENTIFIER =
        TextAttributesKey.createTextAttributesKey("DOCHUB_ANT_ID", DefaultLanguageHighlighterColors.IDENTIFIER)
}
