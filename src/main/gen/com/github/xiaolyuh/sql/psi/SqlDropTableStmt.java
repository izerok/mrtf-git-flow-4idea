// This is a generated file. Not intended for manual editing.
package com.github.xiaolyuh.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlDropTableStmt extends PsiElement {

  @Nullable
  SqlDatabaseName getDatabaseName();

  @Nullable
  SqlTableName getTableName();

  @NotNull
  PsiElement getDrop();

  @Nullable
  PsiElement getExists();

  @Nullable
  PsiElement getIf();

  @NotNull
  PsiElement getTable();

}
