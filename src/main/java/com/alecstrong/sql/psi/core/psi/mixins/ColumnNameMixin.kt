package com.alecstrong.sql.psi.core.psi.mixins

import com.alecstrong.sql.psi.core.AnnotationException
import com.alecstrong.sql.psi.core.SqlAnnotationHolder
import com.alecstrong.sql.psi.core.psi.SqlColumnReference
import com.alecstrong.sql.psi.core.psi.SqlNamedElementImpl
import com.github.xiaolyuh.sql.parser.SqlParser
import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder

internal abstract class ColumnNameMixin(
    node: ASTNode
) : SqlNamedElementImpl(node) {
    override val parseRule: (PsiBuilder, Int) -> Boolean = SqlParser::column_name

    override fun getReference() = SqlColumnReference(this)

    override fun annotate(annotationHolder: SqlAnnotationHolder) {
        try {
            val source = reference.unsafeResolve()
            if (source == null) {
                annotationHolder.createErrorAnnotation(this, "No column found with name $name")
            }
        } catch (e: AnnotationException) {
            annotationHolder.createErrorAnnotation(e.element ?: this, e.msg)
        }
    }
}
