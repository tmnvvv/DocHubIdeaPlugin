// This is a generated file. Not intended for manual editing.
package org.dochub.idea.arch.jsonata;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static org.dochub.idea.arch.jsonata.psi.JSONataTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class JSONataParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return root(b, l + 1);
  }

  /* ********************************************************** */
  // (number | call | deep | block | array | object) [modifier]
  public static boolean argument(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ARGUMENT, "<argument>");
    r = argument_0(b, l + 1);
    r = r && argument_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // number | call | deep | block | array | object
  private static boolean argument_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_0")) return false;
    boolean r;
    r = consumeToken(b, NUMBER);
    if (!r) r = call(b, l + 1);
    if (!r) r = deep(b, l + 1);
    if (!r) r = block(b, l + 1);
    if (!r) r = array(b, l + 1);
    if (!r) r = object(b, l + 1);
    return r;
  }

  // [modifier]
  private static boolean argument_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "argument_1")) return false;
    modifier(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '[' [!']' jsonata (!']' ',' jsonata) *] ']'
  public static boolean array(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array")) return false;
    if (!nextTokenIs(b, BRACK1)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ARRAY, null);
    r = consumeToken(b, BRACK1);
    p = r; // pin = 1
    r = r && report_error_(b, array_1(b, l + 1));
    r = p && consumeToken(b, BRACK2) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [!']' jsonata (!']' ',' jsonata) *]
  private static boolean array_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_1")) return false;
    array_1_0(b, l + 1);
    return true;
  }

  // !']' jsonata (!']' ',' jsonata) *
  private static boolean array_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = array_1_0_0(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, jsonata(b, l + 1));
    r = p && array_1_0_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // !']'
  private static boolean array_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, BRACK2);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (!']' ',' jsonata) *
  private static boolean array_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_1_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!array_1_0_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "array_1_0_2", c)) break;
    }
    return true;
  }

  // !']' ',' jsonata
  private static boolean array_1_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_1_0_2_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = array_1_0_2_0_0(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, COMMA));
    r = p && jsonata(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // !']'
  private static boolean array_1_0_2_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_1_0_2_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, BRACK2);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '(' block_body [';'] ')'
  public static boolean block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BLOCK, "<block>");
    r = consumeToken(b, "(");
    r = r && block_body(b, l + 1);
    r = r && block_2(b, l + 1);
    r = r && consumeToken(b, ")");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [';']
  private static boolean block_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_2")) return false;
    consumeToken(b, ";");
    return true;
  }

  /* ********************************************************** */
  // block_item (';' block_item) *
  public static boolean block_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_body")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BLOCK_BODY, "<block body>");
    r = block_item(b, l + 1);
    r = r && block_body_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (';' block_item) *
  private static boolean block_body_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_body_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!block_body_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "block_body_1", c)) break;
    }
    return true;
  }

  // ';' block_item
  private static boolean block_body_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_body_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ";");
    r = r && block_item(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // function | set_variable | jsonata
  public static boolean block_item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_item")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BLOCK_ITEM, "<block item>");
    r = function(b, l + 1);
    if (!r) r = set_variable(b, l + 1);
    if (!r) r = jsonata(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // variable + '(' call_params ')'
  public static boolean call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call")) return false;
    if (!nextTokenIs(b, VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = call_0(b, l + 1);
    r = r && consumeToken(b, "(");
    r = r && call_params(b, l + 1);
    r = r && consumeToken(b, ")");
    exit_section_(b, m, CALL, r);
    return r;
  }

  // variable +
  private static boolean call_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, VARIABLE);
    while (r) {
      int c = current_position_(b);
      if (!consumeToken(b, VARIABLE)) break;
      if (!empty_element_parsed_guard_(b, "call_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // [jsonata (',' jsonata) *]
  public static boolean call_params(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_params")) return false;
    Marker m = enter_section_(b, l, _NONE_, CALL_PARAMS, "<call params>");
    call_params_0(b, l + 1);
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  // jsonata (',' jsonata) *
  private static boolean call_params_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_params_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = jsonata(b, l + 1);
    r = r && call_params_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (',' jsonata) *
  private static boolean call_params_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_params_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!call_params_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "call_params_0_1", c)) break;
    }
    return true;
  }

  // ',' jsonata
  private static boolean call_params_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_params_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && jsonata(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '=' | '!=' | '>' | '<' | '>=' | '<=' | ' in ' | ' and ' | ' or '
  static boolean comp_operators(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comp_operators")) return false;
    boolean r;
    r = consumeToken(b, "=");
    if (!r) r = consumeToken(b, "!=");
    if (!r) r = consumeToken(b, ">");
    if (!r) r = consumeToken(b, "<");
    if (!r) r = consumeToken(b, ">=");
    if (!r) r = consumeToken(b, "<=");
    if (!r) r = consumeToken(b, " in ");
    if (!r) r = consumeToken(b, " and ");
    if (!r) r = consumeToken(b, " or ");
    return r;
  }

  /* ********************************************************** */
  // '&'
  static boolean concat_operators(PsiBuilder b, int l) {
    return consumeToken(b, "&");
  }

  /* ********************************************************** */
  // '?' jsonata ':'
  static boolean cond_operators(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "cond_operators")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "?");
    r = r && jsonata(b, l + 1);
    r = r && consumeToken(b, COLON);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // deep_left ('.' (id | sys_variable | call | array | object) [modifier])*
  public static boolean deep(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deep")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, DEEP, "<deep>");
    r = deep_left(b, l + 1);
    r = r && deep_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ('.' (id | sys_variable | call | array | object) [modifier])*
  private static boolean deep_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deep_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!deep_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "deep_1", c)) break;
    }
    return true;
  }

  // '.' (id | sys_variable | call | array | object) [modifier]
  private static boolean deep_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deep_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ".");
    r = r && deep_1_0_1(b, l + 1);
    r = r && deep_1_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // id | sys_variable | call | array | object
  private static boolean deep_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deep_1_0_1")) return false;
    boolean r;
    r = consumeToken(b, ID);
    if (!r) r = consumeToken(b, SYS_VARIABLE);
    if (!r) r = call(b, l + 1);
    if (!r) r = array(b, l + 1);
    if (!r) r = object(b, l + 1);
    return r;
  }

  // [modifier]
  private static boolean deep_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deep_1_0_2")) return false;
    modifier(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // (id | variable | sys_variable | string | block | call | range | array | object) [modifier]
  static boolean deep_left(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deep_left")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = deep_left_0(b, l + 1);
    r = r && deep_left_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // id | variable | sys_variable | string | block | call | range | array | object
  private static boolean deep_left_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deep_left_0")) return false;
    boolean r;
    r = consumeToken(b, ID);
    if (!r) r = consumeToken(b, VARIABLE);
    if (!r) r = consumeToken(b, SYS_VARIABLE);
    if (!r) r = consumeToken(b, STRING);
    if (!r) r = block(b, l + 1);
    if (!r) r = call(b, l + 1);
    if (!r) r = range(b, l + 1);
    if (!r) r = array(b, l + 1);
    if (!r) r = object(b, l + 1);
    return r;
  }

  // [modifier]
  private static boolean deep_left_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deep_left_1")) return false;
    modifier(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // [variable (',' variable) *]
  public static boolean func_params(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "func_params")) return false;
    Marker m = enter_section_(b, l, _NONE_, FUNC_PARAMS, "<func params>");
    func_params_0(b, l + 1);
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  // variable (',' variable) *
  private static boolean func_params_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "func_params_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, VARIABLE);
    r = r && func_params_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (',' variable) *
  private static boolean func_params_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "func_params_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!func_params_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "func_params_0_1", c)) break;
    }
    return true;
  }

  // ',' variable
  private static boolean func_params_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "func_params_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, COMMA, VARIABLE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'function'
  public static boolean func_word(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "func_word")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FUNC_WORD, "<func word>");
    r = consumeToken(b, "function");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // variable ':=' func_word '(' func_params ')' '{' jsonata '}'
  public static boolean function(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function")) return false;
    if (!nextTokenIs(b, VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, VARIABLE);
    r = r && consumeToken(b, ":=");
    r = r && func_word(b, l + 1);
    r = r && consumeToken(b, "(");
    r = r && func_params(b, l + 1);
    r = r && consumeToken(b, ")");
    r = r && consumeToken(b, BRACE1);
    r = r && jsonata(b, l + 1);
    r = r && consumeToken(b, BRACE2);
    exit_section_(b, m, FUNCTION, r);
    return r;
  }

  /* ********************************************************** */
  // regex | chain | transform | argument (operators argument | transform_do) *
  public static boolean jsonata(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonata")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, JSONATA, "<jsonata>");
    r = consumeToken(b, REGEX);
    if (!r) r = consumeToken(b, CHAIN);
    if (!r) r = transform(b, l + 1);
    if (!r) r = jsonata_3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // argument (operators argument | transform_do) *
  private static boolean jsonata_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonata_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = argument(b, l + 1);
    r = r && jsonata_3_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (operators argument | transform_do) *
  private static boolean jsonata_3_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonata_3_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!jsonata_3_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "jsonata_3_1", c)) break;
    }
    return true;
  }

  // operators argument | transform_do
  private static boolean jsonata_3_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonata_3_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = jsonata_3_1_0_0(b, l + 1);
    if (!r) r = transform_do(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // operators argument
  private static boolean jsonata_3_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "jsonata_3_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = operators(b, l + 1);
    r = r && argument(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '+' | '-' | '*' | '/' | '%'
  static boolean math_operators(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "math_operators")) return false;
    boolean r;
    r = consumeToken(b, "+");
    if (!r) r = consumeToken(b, "-");
    if (!r) r = consumeToken(b, "*");
    if (!r) r = consumeToken(b, "/");
    if (!r) r = consumeToken(b, "%");
    return r;
  }

  /* ********************************************************** */
  // '#' variable
  public static boolean mod_bind_index(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mod_bind_index")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MOD_BIND_INDEX, "<mod bind index>");
    r = consumeToken(b, "#");
    r = r && consumeToken(b, VARIABLE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '@' variable
  public static boolean mod_bind_self(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mod_bind_self")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MOD_BIND_SELF, "<mod bind self>");
    r = consumeToken(b, "@");
    r = r && consumeToken(b, VARIABLE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '[' jsonata ']'
  public static boolean mod_filter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mod_filter")) return false;
    if (!nextTokenIs(b, BRACK1)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BRACK1);
    r = r && jsonata(b, l + 1);
    r = r && consumeToken(b, BRACK2);
    exit_section_(b, m, MOD_FILTER, r);
    return r;
  }

  /* ********************************************************** */
  // '^' '(' (['>'|'<'] jsonata (',' ['>'|'<'] jsonata)*) ')'
  public static boolean mod_order(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mod_order")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MOD_ORDER, "<mod order>");
    r = consumeToken(b, "^");
    r = r && consumeToken(b, "(");
    r = r && mod_order_2(b, l + 1);
    r = r && consumeToken(b, ")");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ['>'|'<'] jsonata (',' ['>'|'<'] jsonata)*
  private static boolean mod_order_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mod_order_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = mod_order_2_0(b, l + 1);
    r = r && jsonata(b, l + 1);
    r = r && mod_order_2_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ['>'|'<']
  private static boolean mod_order_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mod_order_2_0")) return false;
    mod_order_2_0_0(b, l + 1);
    return true;
  }

  // '>'|'<'
  private static boolean mod_order_2_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mod_order_2_0_0")) return false;
    boolean r;
    r = consumeToken(b, ">");
    if (!r) r = consumeToken(b, "<");
    return r;
  }

  // (',' ['>'|'<'] jsonata)*
  private static boolean mod_order_2_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mod_order_2_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!mod_order_2_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "mod_order_2_2", c)) break;
    }
    return true;
  }

  // ',' ['>'|'<'] jsonata
  private static boolean mod_order_2_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mod_order_2_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && mod_order_2_2_0_1(b, l + 1);
    r = r && jsonata(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ['>'|'<']
  private static boolean mod_order_2_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mod_order_2_2_0_1")) return false;
    mod_order_2_2_0_1_0(b, l + 1);
    return true;
  }

  // '>'|'<'
  private static boolean mod_order_2_2_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mod_order_2_2_0_1_0")) return false;
    boolean r;
    r = consumeToken(b, ">");
    if (!r) r = consumeToken(b, "<");
    return r;
  }

  /* ********************************************************** */
  // (mod_filter | mod_order | mod_bind_index | mod_bind_self) *
  public static boolean modifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modifier")) return false;
    Marker m = enter_section_(b, l, _NONE_, MODIFIER, "<modifier>");
    while (true) {
      int c = current_position_(b);
      if (!modifier_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "modifier", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  // mod_filter | mod_order | mod_bind_index | mod_bind_self
  private static boolean modifier_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modifier_0")) return false;
    boolean r;
    r = mod_filter(b, l + 1);
    if (!r) r = mod_order(b, l + 1);
    if (!r) r = mod_bind_index(b, l + 1);
    if (!r) r = mod_bind_self(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // '{' [!'}' prop (!'}' ',' prop) *] '}'
  public static boolean object(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object")) return false;
    if (!nextTokenIs(b, BRACE1)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, OBJECT, null);
    r = consumeToken(b, BRACE1);
    p = r; // pin = 1
    r = r && report_error_(b, object_1(b, l + 1));
    r = p && consumeToken(b, BRACE2) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [!'}' prop (!'}' ',' prop) *]
  private static boolean object_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_1")) return false;
    object_1_0(b, l + 1);
    return true;
  }

  // !'}' prop (!'}' ',' prop) *
  private static boolean object_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = object_1_0_0(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, prop(b, l + 1));
    r = p && object_1_0_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // !'}'
  private static boolean object_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, BRACE2);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (!'}' ',' prop) *
  private static boolean object_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_1_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!object_1_0_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "object_1_0_2", c)) break;
    }
    return true;
  }

  // !'}' ',' prop
  private static boolean object_1_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_1_0_2_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = object_1_0_2_0_0(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, COMMA));
    r = p && prop(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // !'}'
  private static boolean object_1_0_2_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_1_0_2_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, BRACE2);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // cond_operators | math_operators | comp_operators | concat_operators
  public static boolean operators(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "operators")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, OPERATORS, "<operators>");
    r = cond_operators(b, l + 1);
    if (!r) r = math_operators(b, l + 1);
    if (!r) r = comp_operators(b, l + 1);
    if (!r) r = concat_operators(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // [] (id | string) ':' jsonata
  public static boolean prop(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "prop")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PROP, "<prop>");
    r = prop_0(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, prop_1(b, l + 1));
    r = p && report_error_(b, consumeToken(b, COLON)) && r;
    r = p && jsonata(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // []
  private static boolean prop_0(PsiBuilder b, int l) {
    return true;
  }

  // id | string
  private static boolean prop_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "prop_1")) return false;
    boolean r;
    r = consumeToken(b, ID);
    if (!r) r = consumeToken(b, STRING);
    return r;
  }

  /* ********************************************************** */
  // '[' jsonata '..' jsonata ']'
  public static boolean range(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "range")) return false;
    if (!nextTokenIs(b, BRACK1)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BRACK1);
    r = r && jsonata(b, l + 1);
    r = r && consumeToken(b, "..");
    r = r && jsonata(b, l + 1);
    r = r && consumeToken(b, BRACK2);
    exit_section_(b, m, RANGE, r);
    return r;
  }

  /* ********************************************************** */
  // jsonata
  static boolean root(PsiBuilder b, int l) {
    return jsonata(b, l + 1);
  }

  /* ********************************************************** */
  // variable ':=' jsonata
  public static boolean set_variable(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "set_variable")) return false;
    if (!nextTokenIs(b, VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, VARIABLE);
    r = r && consumeToken(b, ":=");
    r = r && jsonata(b, l + 1);
    exit_section_(b, m, SET_VARIABLE, r);
    return r;
  }

  /* ********************************************************** */
  // '|' transform_location '|' jsonata [','  array] '|'
  public static boolean transform(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "transform")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TRANSFORM, "<transform>");
    r = consumeToken(b, "|");
    r = r && transform_location(b, l + 1);
    r = r && consumeToken(b, "|");
    r = r && jsonata(b, l + 1);
    r = r && transform_4(b, l + 1);
    r = r && consumeToken(b, "|");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [','  array]
  private static boolean transform_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "transform_4")) return false;
    transform_4_0(b, l + 1);
    return true;
  }

  // ','  array
  private static boolean transform_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "transform_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && array(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '~>' (transform | call | variable)
  public static boolean transform_do(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "transform_do")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TRANSFORM_DO, "<transform do>");
    r = consumeToken(b, "~>");
    r = r && transform_do_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // transform | call | variable
  private static boolean transform_do_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "transform_do_1")) return false;
    boolean r;
    r = transform(b, l + 1);
    if (!r) r = call(b, l + 1);
    if (!r) r = consumeToken(b, VARIABLE);
    return r;
  }

  /* ********************************************************** */
  // id ('.' id) *
  public static boolean transform_location(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "transform_location")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ID);
    r = r && transform_location_1(b, l + 1);
    exit_section_(b, m, TRANSFORM_LOCATION, r);
    return r;
  }

  // ('.' id) *
  private static boolean transform_location_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "transform_location_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!transform_location_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "transform_location_1", c)) break;
    }
    return true;
  }

  // '.' id
  private static boolean transform_location_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "transform_location_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ".");
    r = r && consumeToken(b, ID);
    exit_section_(b, m, null, r);
    return r;
  }

}
