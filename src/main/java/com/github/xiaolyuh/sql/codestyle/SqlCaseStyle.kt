package com.github.xiaolyuh.sql.codestyle

import com.dbn.common.util.Naming
import com.github.xiaolyuh.sql.SqlElementFactory
import com.intellij.psi.PsiElement
import com.intellij.util.ui.PresentableEnum

enum class SqlCaseStyle(val myId: Int, private val myDescription: String) : PresentableEnum {
    LOWER(1, "改为小写") {
        override fun doModifyKeyword(
            psiElement: PsiElement,
        ) {
            val leafPsiElement =
                SqlElementFactory.createSqlElement(psiElement.project, psiElement.text.lowercase()).firstChild
            psiElement.replace(leafPsiElement)
        }
    },
    UPPER(2, "改为大写") {
        override fun doModifyKeyword(
            psiElement: PsiElement,
        ) {
            val leafPsiElement =
                SqlElementFactory.createSqlElement(psiElement.project, psiElement.text.uppercase()).firstChild
            psiElement.replace(leafPsiElement)
        }
    },
    CAPITALIZE(3, "首字母大写") {
        override fun doModifyKeyword(
            psiElement: PsiElement,
        ) {
            val leafPsiElement =
                SqlElementFactory.createSqlElement(psiElement.project, Naming.capitalize(psiElement.text)).firstChild
            psiElement.replace(leafPsiElement)
        }
    },

    NOT_CHANGE(4, "不改变")
    ;

    open fun doModifyKeyword(
        psiElement: PsiElement,
    ) {
    }

    override fun getPresentableText(): String {
        return myDescription
    }

    companion object {
        fun getByCode(code: Int): SqlCaseStyle {
            for (value in entries) {
                if (value.myId == code) {
                    return value
                }
            }
            throw RuntimeException("" + code)
        }
    }
}
