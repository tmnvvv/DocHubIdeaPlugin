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

public class JSONataModifierImpl extends ASTWrapperPsiElement implements JSONataModifier {

  public JSONataModifierImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull JSONataVisitor visitor) {
    visitor.visitModifier(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JSONataVisitor) accept((JSONataVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<JSONataModBindIndex> getModBindIndexList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, JSONataModBindIndex.class);
  }

  @Override
  @NotNull
  public List<JSONataModBindSelf> getModBindSelfList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, JSONataModBindSelf.class);
  }

  @Override
  @NotNull
  public List<JSONataModFilter> getModFilterList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, JSONataModFilter.class);
  }

  @Override
  @NotNull
  public List<JSONataModOrder> getModOrderList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, JSONataModOrder.class);
  }

}
