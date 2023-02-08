package org.dochub.idea.arch.jsonata

import com.intellij.lexer.FlexAdapter
import org.dochub.idea.arch.jsonata.lexer.JSONataLexer

class JSONataLexerAdapter : FlexAdapter(JSONataLexer(null))