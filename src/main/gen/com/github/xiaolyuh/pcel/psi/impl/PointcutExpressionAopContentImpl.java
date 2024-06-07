// This is a generated file. Not intended for manual editing.
package com.github.xiaolyuh.pcel.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.github.xiaolyuh.pcel.psi.PointcutExpressionTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.github.xiaolyuh.pcel.psi.*;

public class PointcutExpressionAopContentImpl extends ASTWrapperPsiElement implements PointcutExpressionAopContent {

  public PointcutExpressionAopContentImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PointcutExpressionVisitor visitor) {
    visitor.visitAopContent(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PointcutExpressionVisitor) accept((PointcutExpressionVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PointcutExpressionAopMethodReference getAopMethodReference() {
    return findChildByClass(PointcutExpressionAopMethodReference.class);
  }

  @Override
  @Nullable
  public PointcutExpressionAopReal getAopReal() {
    return findChildByClass(PointcutExpressionAopReal.class);
  }

}
