package com.alecstrong.sql.psi.core.psi.mixins

import com.alecstrong.sql.psi.core.SqlAnnotationHolder
import com.alecstrong.sql.psi.core.psi.SqlCompositeElementImpl
import com.github.xiaolyuh.sql.psi.*
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

internal abstract class BinaryLikeExprMixin(
    node: ASTNode
) : SqlCompositeElementImpl(node),
    SqlBinaryLikeExpr {

    private val hasMatchOperator: Boolean
        get() = binaryLikeOperator.node.findChildByType(SqlTypes.MATCH) != null

    override fun annotate(annotationHolder: SqlAnnotationHolder) {
        if (hasMatchOperator) {
            checkForMatchUsageError(annotationHolder)
        }
    }

    /**
     * Check for common cases where the MATCH operator would fail. For example, the left hand side of the MATCH operator
     * must be a column in an FTS table and that table must not be on the right hand side of a LEFT JOIN.
     */
    private fun checkForMatchUsageError(annotationHolder: SqlAnnotationHolder) {
        val isMatchUsageError = when (val firstExpression = exprList.first()) {
            is SqlColumnExpr -> {
                when (val resolvedReference = firstExpression.columnName.reference?.resolve()) {
                    is SqlCreateVirtualTableStmt ->
                        isMatchUsageErrorOnSynthesizedColumn(firstExpression, resolvedReference)

                    is SqlColumnName -> isMatchUsageErrorOnRegularColumn(firstExpression, resolvedReference)
                    null -> false  // Column is invalid, which is a different error that's handled by the column name element
                    else -> true
                }
            }

            else -> true
        }

        if (isMatchUsageError) {
            annotationHolder.createErrorAnnotation(
                this,
                "Unable to use function MATCH in the requested context"
            )
        }
    }

    private fun isMatchUsageErrorOnSynthesizedColumn(
        expression: SqlColumnExpr,
        table: SqlCreateVirtualTableStmt
    ): Boolean {
        return if (table.usesFtsModule) {
            queryAvailable(expression)
                .filter { it.table?.name == table.tableName.name }
                .any { query -> query.synthesizedColumns.any { it.nullable } }
        } else {
            true
        }
    }

    private fun isMatchUsageErrorOnRegularColumn(
        expression: SqlColumnExpr,
        columnName: SqlColumnName
    ): Boolean {
        val table = PsiTreeUtil.findFirstParent(columnName) { it is SqlCreateVirtualTableStmt }
                as? SqlCreateVirtualTableStmt

        return if (table?.usesFtsModule == true) {
            queryAvailable(expression)
                .filter { it.table?.name == table.tableName.name }
                .any { query ->
                    query.columns.filter { it.element.text == expression.columnName.name }.any { it.nullable }
                }
        } else {
            true
        }
    }
}