package com.alecstrong.sql.psi.core.psi.mixins

import com.alecstrong.sql.psi.core.SqlAnnotationHolder
import com.alecstrong.sql.psi.core.psi.QueryElement.QueryResult
import com.alecstrong.sql.psi.core.psi.SqlCompositeElementImpl
import com.github.xiaolyuh.sql.psi.SqlCreateIndexStmt
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

internal abstract class CreateIndexMixin(
    node: ASTNode
) : SqlCompositeElementImpl(node),
    SqlCreateIndexStmt {
    override fun queryAvailable(child: PsiElement): Collection<QueryResult> {
        if (child in indexedColumnList || child == expr) {
            return listOf(tablesAvailable(child).first { it.tableName.name == tableName?.name }.query)
        }
        return super.queryAvailable(child)
    }

    override fun annotate(annotationHolder: SqlAnnotationHolder) {
        if (containingFile.indexes().any { it != this && it.indexName.text == indexName.text }) {
            annotationHolder.createErrorAnnotation(indexName, "Duplicate index name ${indexName.text}")
        }
        super.annotate(annotationHolder)
    }
}