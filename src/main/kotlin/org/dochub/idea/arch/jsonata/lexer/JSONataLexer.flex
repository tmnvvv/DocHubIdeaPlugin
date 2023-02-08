package org.dochub.idea.arch.jsonata.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static org.dochub.idea.arch.jsonata.psi.JSONataTypes.*;

%%

%{
  public JSONataLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class JSONataLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

COMMENT=\/\*[\s\S]*?\*\/
SPACE=[ \t\n\x0B\f\r]+
STRING=\"[^\"]*\"|'[^']*'
NUMBER=-?(0|[1-9][[:digit:]]*)(\.[[:digit:]]+)?([eE][+-]?[[:digit:]]+)?
BOOLEAN=true|false|null
ID=[:letter:]+[a-zA-Z_0-9]*
RANGE=\[([1-9]|0)+\.\.([1-9]|0)+\]*
VARIABLE=\$([a-z]|[A-Z]|\_)*
SYS_VARIABLE=\*\*|\*|\%
REGEX=\/.+[^\\]\/

%%
<YYINITIAL> {
  {WHITE_SPACE}       { return WHITE_SPACE; }

  ","                 { return COMMA; }
  ":"                 { return COLON; }
  "{"                 { return BRACE1; }
  "}"                 { return BRACE2; }
  "["                 { return BRACK1; }
  "]"                 { return BRACK2; }
  "chain"             { return CHAIN; }

  {COMMENT}           { return COMMENT; }
  {SPACE}             { return SPACE; }
  {STRING}            { return STRING; }
  {NUMBER}            { return NUMBER; }
  {BOOLEAN}           { return BOOLEAN; }
  {ID}                { return ID; }
  {VARIABLE}          { return VARIABLE; }
  {SYS_VARIABLE}      { return SYS_VARIABLE; }
  {REGEX}             { return REGEX; }

}

[^] { return BAD_CHARACTER; }
