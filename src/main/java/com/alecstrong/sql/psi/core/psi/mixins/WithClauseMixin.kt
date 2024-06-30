package com.alecstrong.sql.psi.core.psi.mixins

import com.alecstrong.sql.psi.core.SqlAnnotationHolder
import com.alecstrong.sql.psi.core.psi.QueryElement.QueryResult
import com.alecstrong.sql.psi.core.psi.SqlCompositeElementImpl
import com.github.xiaolyuh.sql.psi.SqlWithClause
import com.intellij.lang.ASTNode

internal abstract class WithClauseMixin(
    node: ASTNode
) : SqlCompositeElementImpl(node),
    SqlWithClause {
    override fun annotate(annotationHolder: SqlAnnotationHolder) {
        cteTableNameList.zip(compoundSelectStmtList)
            .forEach { (name, selectStmt) ->
                val query = QueryResult(name.tableName, selectStmt.queryExposed().flatMap { it.columns })
                if (name.columnAliasList.isNotEmpty() && name.columnAliasList.size != query.columns.size) {
                    annotationHolder.createErrorAnnotation(name, "Incorrect number of columns")
                }
            }
    }
}