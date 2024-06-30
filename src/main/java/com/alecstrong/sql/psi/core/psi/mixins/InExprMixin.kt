package com.alecstrong.sql.psi.core.psi.mixins

import com.alecstrong.sql.psi.core.SqlAnnotationHolder
import com.alecstrong.sql.psi.core.psi.SqlCompositeElementImpl
import com.github.xiaolyuh.sql.psi.SqlInExpr
import com.intellij.lang.ASTNode

internal abstract class InExprMixin(
    node: ASTNode
) : SqlCompositeElementImpl(node),
    SqlInExpr {
    override fun annotate(annotationHolder: SqlAnnotationHolder) {
        val query = compoundSelectStmt?.queryExposed()
            ?: tableName?.let { table -> tableAvailable(this, table.name) }
            ?: emptyList()
        if (query.flatMap { it.columns }.size > 1) {
            annotationHolder.createErrorAnnotation(
                this,
                "Only a single result allowed for a SELECT that is part of an expression"
            )
        }
    }
}