package org.dochub.idea.arch.completions;

import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.editorActions.TabOutScopesTracker;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.util.text.CharArrayUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.repeat;

public class FormattingInsertHandler implements InsertHandler {
    private final CompletionKey key;

    private final int documentLevel;

    public FormattingInsertHandler(CompletionKey key, int documentLevel) {
        this.key = key;
        this.documentLevel = documentLevel;
    }

    @Override
    public void handleInsert(@NotNull final InsertionContext context, @NotNull final LookupElement item) {
        final Editor editor = context.getEditor();

        final Document document = editor.getDocument();
        final int caretOffset = editor.getCaretModel().getOffset();

        final CharSequence chars = document.getCharsSequence();


        if (CharArrayUtil.regionMatches(chars, caretOffset, ":")) {
            document.deleteString(caretOffset, caretOffset + 1);
        }

        String toInsert = getCharsToInsert();

        document.insertString(caretOffset, toInsert);
        editor.getCaretModel().moveToOffset(caretOffset + toInsert.length());

        TabOutScopesTracker.getInstance().registerEmptyScopeAtCaret(context.getEditor());
        editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
        editor.getSelectionModel().removeSelection();
        AutoPopupController.getInstance(Objects.requireNonNull(editor.getProject())).scheduleAutoPopup(editor);
    }

    private String getCharsToInsert() {
        StringBuilder result = new StringBuilder(":");
        switch (key.getValueType()) {
            case MAP:
                result.append("\n")
                        .append(repeat(" ", (documentLevel + 1) * 2));
                break;
            case LIST:
                result.append("\n")
                        .append(repeat(" ", (documentLevel + 1) * 2))
                        .append("- ");
                break;
            default:
                result.append(" ");
        }
        return result.toString();
    }

}
