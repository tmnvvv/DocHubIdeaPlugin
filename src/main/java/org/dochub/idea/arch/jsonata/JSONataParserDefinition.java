package org.dochub.idea.arch.jsonata;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.dochub.idea.arch.jsonata.psi.JSONataTypes;
import org.jetbrains.annotations.NotNull;

import static org.dochub.idea.arch.jsonata.psi.JSONataTypes.*;


public class JSONataParserDefinition implements ParserDefinition {

    public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);
    public static final TokenSet COMMENTS = TokenSet.create(COMMENT);
    public static final TokenSet BRACKETS = TokenSet.create(LBRACKET, RBRACKET);
    public static final TokenSet STRING_LITERALS = TokenSet.create(STRING);
    public static final TokenSet OPERATORS = TokenSet.create(
            CHAIN, DOUBLE_QUOTE,
            COLON, VERTICAL_BAR,
            EQ, NOT_EQ,
            QUESTION_MARK,
            SEMICOLON, DOT,
            BACKSLASH, DOUBLE_DOT,
            CONCAT, ASSIGN, LESS,
            GREATER, LESS_OR_EQUAL,
            GREATER_OR_EQUAL, REMAINDER
    );

    public static final TokenSet KEYWORDS = TokenSet.create(IN, AND, OR, FUNCTION);
    public static final TokenSet NUMBERS = TokenSet.create(NUMBER);
    public static final IFileElementType FILE = new IFileElementType(JSONataLanguage.INSTANCE);

    public static final TokenSet VARIABLES = TokenSet.create(VARIABLE);


    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new JSONataLexerAdapter();
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return WHITE_SPACES;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public PsiParser createParser(final Project project) {
        return new JSONataParser();
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return FILE;
    }

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new JSONataFile(viewProvider);
    }

    @Override
    public @NotNull SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return JSONataTypes.Factory.createElement(node);
    }
}
