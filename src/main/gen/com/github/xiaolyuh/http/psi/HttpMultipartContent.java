// This is a generated file. Not intended for manual editing.
package com.github.xiaolyuh.http.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface HttpMultipartContent extends PsiElement {

  @NotNull
  List<HttpMultipartBody> getMultipartBodyList();

  @NotNull
  PsiElement getMultipartSeperateEnd();

}
