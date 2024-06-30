package com.alecstrong.sql.psi.core.psi.mixins

import com.alecstrong.sql.psi.core.psi.QueryElement
import com.alecstrong.sql.psi.core.psi.QueryElement.QueryResult
import com.alecstrong.sql.psi.core.psi.SqlCompositeElementImpl
import com.github.xiaolyuh.sql.psi.SqlColumnExpr
import com.github.xiaolyuh.sql.psi.SqlResultColumn
import com.intellij.lang.ASTNode

internal abstract class ResultColumnMixin(
    node: ASTNode
) : SqlCompositeElementImpl(node),
    SqlResultColumn {
    private val queryExposed: Collection<QueryResult> by com.alecstrong.sql.psi.core.ModifiableFileLazy(containingFile) lazy@{
        tableName?.let { tableNameElement ->
            // table_name '.' '*'
            return@lazy queryAvailable(this).filter { it.table?.name == tableNameElement.name }
        }
        expr?.let {
            var column: QueryElement.QueryColumn
            if (it is SqlColumnExpr) {
                val reference = (it.columnName as ColumnNameMixin).reference
                column = reference.resolveToQuery()
                    ?: QueryElement.QueryColumn(it.columnName.reference?.resolve() ?: it)
            } else {
                column = QueryElement.QueryColumn(it)
            }

            // expr [ '.' column_alias ]
            columnAlias?.let { alias ->
                column = column.copy(element = alias)
            }

            return@lazy listOf(QueryResult(columns = listOf(column)))
        }

        // *
        val queryAvailable = queryAvailable(this)
        if (queryAvailable.size <= 1) {
            return@lazy queryAvailable
        }

        return@lazy queryAvailable.fold(emptyList<QueryResult>()) { left, right ->
            left + right.copy(table = null, columns = right.columns.filter { !it.hiddenByUsing })
        }
    }

    override fun queryExposed() = queryExposed
}
