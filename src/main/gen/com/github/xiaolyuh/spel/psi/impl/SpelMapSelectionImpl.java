// This is a generated file. Not intended for manual editing.
package com.github.xiaolyuh.spel.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.github.xiaolyuh.spel.psi.SpelTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.github.xiaolyuh.spel.psi.*;

public class SpelMapSelectionImpl extends ASTWrapperPsiElement implements SpelMapSelection {

  public SpelMapSelectionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SpelVisitor visitor) {
    visitor.visitMapSelection(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SpelVisitor) accept((SpelVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public SpelFieldName getFieldName() {
    return findChildByClass(SpelFieldName.class);
  }

}
