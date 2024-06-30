package com.alecstrong.sql.psi.core.psi.mixins

import com.alecstrong.sql.psi.core.SqlAnnotationHolder
import com.alecstrong.sql.psi.core.psi.LazyQuery
import com.alecstrong.sql.psi.core.psi.QueryElement.QueryResult
import com.github.xiaolyuh.sql.psi.*
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

internal abstract class CompoundSelectStmtMixin(
    node: ASTNode
) : WithClauseContainer(node),
    SqlCompoundSelectStmt {
    private val queryExposed: Collection<QueryResult> by com.alecstrong.sql.psi.core.ModifiableFileLazy(containingFile) {
        if (detectRecursion() != null) {
            return@ModifiableFileLazy emptyList<QueryResult>()
        }
        if (parent is SqlWithClause) {
            // Compound information not needed.
            return@ModifiableFileLazy selectStmtList.first().queryExposed()
        }
        return@ModifiableFileLazy selectStmtList.drop(1)
            .fold(selectStmtList.first().queryExposed()) { query, compounded ->
                val columns = query.flatMap { it.columns }
                val compoundedColumns = compounded.queryExposed().flatMap { it.columns }
                return@fold listOf(query.first().copy(
                    columns = columns.zip(compoundedColumns) { column, compounded ->
                        column.copy(compounded = column.compounded + compounded)
                    }
                ))
            }
    }

    override fun queryExposed() = queryExposed

    override fun tablesAvailable(child: PsiElement): Collection<LazyQuery> {
        val tablesAvailable = super.tablesAvailable(child)
        val parent = parent
        if (parent is SqlWithClause) {
            if (parent.node.findChildByType(SqlTypes.RECURSIVE) != null
                && child != selectStmtList.first()
            ) {
                return tablesAvailable + parent.tablesExposed()
            }
            val myIndex = parent.compoundSelectStmtList.indexOf(this)
            return tablesAvailable + parent.tablesExposed().filterIndexed { index, _ -> index != myIndex }
        }
        return tablesAvailable
    }

    override fun queryAvailable(child: PsiElement): Collection<QueryResult> {
        if (child is SqlOrderingTerm && selectStmtList.size == 1) {
            val exposed = (selectStmtList.first() as SelectStmtMixin).fromQuery()
                .map { it.copy(columns = it.columns.filter { !it.hiddenByUsing }) }
            val exposedColumns = exposed.flatMap { it.columns }

            // Ordering terms are also applicable in the select statement's from clause.
            return queryExposed().filter { it !in exposed }
                .map { QueryResult(it.table, it.columns.filter { it !in exposedColumns }) }
                .plus(exposed)
        } else if (child is SqlExpr || child is SqlOrderingTerm) {
            return queryExposed()
        }
        return super.queryAvailable(child)
    }

    override fun annotate(annotationHolder: SqlAnnotationHolder) {
        val numColumns = selectStmtList[0].queryExposed().flatMap { it.columns }.count()

        detectRecursion()?.let { recursion ->
            annotationHolder.createErrorAnnotation(this, "Recursive subquery found: $recursion")
        }

        selectStmtList.drop(1)
            .forEach {
                val count = it.queryExposed().flatMap { it.columns }.count()
                if (count != numColumns) {
                    annotationHolder.createErrorAnnotation(
                        it, "Unexpected number of columns in compound" +
                                " statement found: $count expected: $numColumns"
                    )
                }
            }
    }

    private fun detectRecursion(): String? {
        val view = parent as? SqlCreateViewStmt ?: return null

        val viewTree = linkedSetOf(view.viewName.name)

        fun SqlCreateViewStmt.recursion(): String? {
            PsiTreeUtil.findChildrenOfType(compoundSelectStmt, TableNameMixin::class.java).forEach { it ->
                val name = it.name
                if (!viewTree.add(name)) {
                    return viewTree.joinToString(" -> ") + " -> $name"
                }
                containingFile.viewForName(name)?.recursion()?.let { return it }
                viewTree.remove(name)
            }
            return null
        }

        return view.recursion()
    }
}