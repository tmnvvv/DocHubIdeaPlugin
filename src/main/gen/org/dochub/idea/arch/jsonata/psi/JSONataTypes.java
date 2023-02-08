// This is a generated file. Not intended for manual editing.
package org.dochub.idea.arch.jsonata.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import org.intellij.sdk.language.psi.impl.*;

public interface JSONataTypes {

  IElementType ARGUMENT = new JSONataElementType("ARGUMENT");
  IElementType ARRAY = new JSONataElementType("ARRAY");
  IElementType BLOCK = new JSONataElementType("BLOCK");
  IElementType BLOCK_BODY = new JSONataElementType("BLOCK_BODY");
  IElementType BLOCK_ITEM = new JSONataElementType("BLOCK_ITEM");
  IElementType CALL = new JSONataElementType("CALL");
  IElementType CALL_PARAMS = new JSONataElementType("CALL_PARAMS");
  IElementType DEEP = new JSONataElementType("DEEP");
  IElementType FUNCTION = new JSONataElementType("FUNCTION");
  IElementType FUNC_PARAMS = new JSONataElementType("FUNC_PARAMS");
  IElementType FUNC_WORD = new JSONataElementType("FUNC_WORD");
  IElementType JSONATA = new JSONataElementType("JSONATA");
  IElementType MODIFIER = new JSONataElementType("MODIFIER");
  IElementType MOD_BIND_INDEX = new JSONataElementType("MOD_BIND_INDEX");
  IElementType MOD_BIND_SELF = new JSONataElementType("MOD_BIND_SELF");
  IElementType MOD_FILTER = new JSONataElementType("MOD_FILTER");
  IElementType MOD_ORDER = new JSONataElementType("MOD_ORDER");
  IElementType OBJECT = new JSONataElementType("OBJECT");
  IElementType OPERATORS = new JSONataElementType("OPERATORS");
  IElementType PROP = new JSONataElementType("PROP");
  IElementType RANGE = new JSONataElementType("RANGE");
  IElementType SET_VARIABLE = new JSONataElementType("SET_VARIABLE");
  IElementType TRANSFORM = new JSONataElementType("TRANSFORM");
  IElementType TRANSFORM_DO = new JSONataElementType("TRANSFORM_DO");
  IElementType TRANSFORM_LOCATION = new JSONataElementType("TRANSFORM_LOCATION");

  IElementType BOOLEAN = new JSONataTokenType("boolean");
  IElementType BRACE1 = new JSONataTokenType("{");
  IElementType BRACE2 = new JSONataTokenType("}");
  IElementType BRACK1 = new JSONataTokenType("[");
  IElementType BRACK2 = new JSONataTokenType("]");
  IElementType CHAIN = new JSONataTokenType("chain");
  IElementType COLON = new JSONataTokenType(":");
  IElementType COMMA = new JSONataTokenType(",");
  IElementType COMMENT = new JSONataTokenType("comment");
  IElementType ID = new JSONataTokenType("id");
  IElementType NUMBER = new JSONataTokenType("number");
  IElementType REGEX = new JSONataTokenType("regex");
  IElementType STRING = new JSONataTokenType("string");
  IElementType SYS_VARIABLE = new JSONataTokenType("sys_variable");
  IElementType VARIABLE = new JSONataTokenType("variable");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ARGUMENT) {
        return new JSONataArgumentImpl(node);
      }
      else if (type == ARRAY) {
        return new JSONataArrayImpl(node);
      }
      else if (type == BLOCK) {
        return new JSONataBlockImpl(node);
      }
      else if (type == BLOCK_BODY) {
        return new JSONataBlockBodyImpl(node);
      }
      else if (type == BLOCK_ITEM) {
        return new JSONataBlockItemImpl(node);
      }
      else if (type == CALL) {
        return new JSONataCallImpl(node);
      }
      else if (type == CALL_PARAMS) {
        return new JSONataCallParamsImpl(node);
      }
      else if (type == DEEP) {
        return new JSONataDeepImpl(node);
      }
      else if (type == FUNCTION) {
        return new JSONataFunctionImpl(node);
      }
      else if (type == FUNC_PARAMS) {
        return new JSONataFuncParamsImpl(node);
      }
      else if (type == FUNC_WORD) {
        return new JSONataFuncWordImpl(node);
      }
      else if (type == JSONATA) {
        return new JSONataJsonataImpl(node);
      }
      else if (type == MODIFIER) {
        return new JSONataModifierImpl(node);
      }
      else if (type == MOD_BIND_INDEX) {
        return new JSONataModBindIndexImpl(node);
      }
      else if (type == MOD_BIND_SELF) {
        return new JSONataModBindSelfImpl(node);
      }
      else if (type == MOD_FILTER) {
        return new JSONataModFilterImpl(node);
      }
      else if (type == MOD_ORDER) {
        return new JSONataModOrderImpl(node);
      }
      else if (type == OBJECT) {
        return new JSONataObjectImpl(node);
      }
      else if (type == OPERATORS) {
        return new JSONataOperatorsImpl(node);
      }
      else if (type == PROP) {
        return new JSONataPropImpl(node);
      }
      else if (type == RANGE) {
        return new JSONataRangeImpl(node);
      }
      else if (type == SET_VARIABLE) {
        return new JSONataSetVariableImpl(node);
      }
      else if (type == TRANSFORM) {
        return new JSONataTransformImpl(node);
      }
      else if (type == TRANSFORM_DO) {
        return new JSONataTransformDoImpl(node);
      }
      else if (type == TRANSFORM_LOCATION) {
        return new JSONataTransformLocationImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
