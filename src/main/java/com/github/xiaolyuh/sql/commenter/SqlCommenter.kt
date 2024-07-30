package com.github.xiaolyuh.sql.commenter

import com.intellij.lang.Commenter

class SqlCommenter : Commenter {
    override fun getLineCommentPrefix(): String {
        return "--"
    }

    override fun getBlockCommentPrefix(): String {
        return "/*"
    }

    override fun getBlockCommentSuffix(): String {
        return "*/"
    }

    override fun getCommentedBlockCommentPrefix(): String? {
        return null
    }

    override fun getCommentedBlockCommentSuffix(): String? {
        return null
    }
}
