// This is a generated file. Not intended for manual editing.
package org.intellij.sdk.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static org.dochub.idea.arch.jsonata.psi.JSONataTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import org.intellij.sdk.language.psi.*;

public class JSONataArgumentImpl extends ASTWrapperPsiElement implements JSONataArgument {

  public JSONataArgumentImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull JSONataVisitor visitor) {
    visitor.visitArgument(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JSONataVisitor) accept((JSONataVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public JSONataArray getArray() {
    return findChildByClass(JSONataArray.class);
  }

  @Override
  @Nullable
  public JSONataBlock getBlock() {
    return findChildByClass(JSONataBlock.class);
  }

  @Override
  @Nullable
  public JSONataCall getCall() {
    return findChildByClass(JSONataCall.class);
  }

  @Override
  @Nullable
  public JSONataDeep getDeep() {
    return findChildByClass(JSONataDeep.class);
  }

  @Override
  @Nullable
  public JSONataModifier getModifier() {
    return findChildByClass(JSONataModifier.class);
  }

  @Override
  @Nullable
  public JSONataObject getObject() {
    return findChildByClass(JSONataObject.class);
  }

  @Override
  @Nullable
  public PsiElement getNumber() {
    return findChildByType(NUMBER);
  }

}
