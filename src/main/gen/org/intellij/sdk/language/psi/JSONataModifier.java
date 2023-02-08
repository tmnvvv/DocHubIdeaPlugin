// This is a generated file. Not intended for manual editing.
package org.intellij.sdk.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface JSONataModifier extends PsiElement {

  @NotNull
  List<JSONataModBindIndex> getModBindIndexList();

  @NotNull
  List<JSONataModBindSelf> getModBindSelfList();

  @NotNull
  List<JSONataModFilter> getModFilterList();

  @NotNull
  List<JSONataModOrder> getModOrderList();

}
