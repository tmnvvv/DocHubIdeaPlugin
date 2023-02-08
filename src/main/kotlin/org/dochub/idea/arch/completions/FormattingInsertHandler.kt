package org.dochub.idea.arch.completions

import com.intellij.codeInsight.AutoPopupController

import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.editorActions.TabOutScopesTracker
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.editor.ScrollType
import org.apache.commons.lang.StringUtils
import com.intellij.util.text.CharArrayUtil


class FormattingInsertHandler(private val key: CompletionKey, private val documentLevel: Int) : InsertHandler<LookupElement> {


        override fun handleInsert(context: InsertionContext, item: LookupElement) {
        val editor = context.editor
        val document = editor.document
        val caretOffset = editor.caretModel.offset
        val chars = document.charsSequence
        if (CharArrayUtil.regionMatches(chars, caretOffset, ":")) {
            document.deleteString(caretOffset, caretOffset + 1)
        }
        val toInsert = charsToInsert
        document.insertString(caretOffset, toInsert)
        editor.caretModel.moveToOffset(caretOffset + toInsert.length)
        TabOutScopesTracker.getInstance().registerEmptyScopeAtCaret(context.editor)
        editor.scrollingModel.scrollToCaret(ScrollType.RELATIVE)
        editor.selectionModel.removeSelection()
        AutoPopupController.getInstance(editor.project!!).scheduleAutoPopup(editor)
    }

    private val charsToInsert: String
        private get() {
            val result = StringBuilder(":")
            when (key.valueType) {
                CompletionKey.ValueType.MAP -> result.append("\n")
                    .append(StringUtils.repeat(" ", (documentLevel + 1) * 2))

                CompletionKey.ValueType.LIST -> result.append("\n")
                    .append(StringUtils.repeat(" ", (documentLevel + 1) * 2))
                    .append("- ")

                else -> result.append(" ")
            }
            return result.toString()
        }

}
