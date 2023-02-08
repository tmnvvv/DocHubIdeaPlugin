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

public class JSONataDeepImpl extends ASTWrapperPsiElement implements JSONataDeep {

  public JSONataDeepImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull JSONataVisitor visitor) {
    visitor.visitDeep(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JSONataVisitor) accept((JSONataVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<JSONataArray> getArrayList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, JSONataArray.class);
  }

  @Override
  @Nullable
  public JSONataBlock getBlock() {
    return findChildByClass(JSONataBlock.class);
  }

  @Override
  @NotNull
  public List<JSONataCall> getCallList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, JSONataCall.class);
  }

  @Override
  @NotNull
  public List<JSONataModifier> getModifierList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, JSONataModifier.class);
  }

  @Override
  @NotNull
  public List<JSONataObject> getObjectList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, JSONataObject.class);
  }

  @Override
  @Nullable
  public JSONataRange getRange() {
    return findChildByClass(JSONataRange.class);
  }

  @Override
  @Nullable
  public PsiElement getString() {
    return findChildByType(STRING);
  }

  @Override
  @Nullable
  public PsiElement getVariable() {
    return findChildByType(VARIABLE);
  }

}
