// This is a generated file. Not intended for manual editing.
package org.intellij.sdk.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface JSONataDeep extends PsiElement {

  @NotNull
  List<JSONataArray> getArrayList();

  @Nullable
  JSONataBlock getBlock();

  @NotNull
  List<JSONataCall> getCallList();

  @NotNull
  List<JSONataModifier> getModifierList();

  @NotNull
  List<JSONataObject> getObjectList();

  @Nullable
  JSONataRange getRange();

  @Nullable
  PsiElement getString();

  @Nullable
  PsiElement getVariable();

}
