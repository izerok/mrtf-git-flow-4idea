// This is a generated file. Not intended for manual editing.
package com.github.xiaolyuh.sql.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface SqlPragmaStmt extends PsiElement {

  @Nullable
  SqlDatabaseName getDatabaseName();

  @Nullable
  SqlPragmaName getPragmaName();

  @Nullable
  SqlPragmaValue getPragmaValue();

  @NotNull
  PsiElement getPragma();

}
