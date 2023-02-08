// This is a generated file. Not intended for manual editing.
package org.intellij.sdk.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface JSONataJsonata extends PsiElement {

  @NotNull
  List<JSONataArgument> getArgumentList();

  @NotNull
  List<JSONataOperators> getOperatorsList();

  @Nullable
  JSONataTransform getTransform();

  @NotNull
  List<JSONataTransformDo> getTransformDoList();

  @Nullable
  PsiElement getRegex();

}
