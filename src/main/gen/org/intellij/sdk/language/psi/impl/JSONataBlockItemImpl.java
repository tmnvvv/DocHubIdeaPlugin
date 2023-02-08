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

public class JSONataBlockItemImpl extends ASTWrapperPsiElement implements JSONataBlockItem {

  public JSONataBlockItemImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull JSONataVisitor visitor) {
    visitor.visitBlockItem(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JSONataVisitor) accept((JSONataVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public JSONataFunction getFunction() {
    return findChildByClass(JSONataFunction.class);
  }

  @Override
  @Nullable
  public JSONataJsonata getJsonata() {
    return findChildByClass(JSONataJsonata.class);
  }

  @Override
  @Nullable
  public JSONataSetVariable getSetVariable() {
    return findChildByClass(JSONataSetVariable.class);
  }

}
