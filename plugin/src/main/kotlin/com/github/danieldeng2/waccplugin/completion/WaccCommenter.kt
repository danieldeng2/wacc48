package com.github.danieldeng2.waccplugin.completion

import com.intellij.lang.Commenter

class WaccCommenter : Commenter {
    override fun getLineCommentPrefix(): String = "#"

    override fun getBlockCommentPrefix(): String = ""

    override fun getBlockCommentSuffix(): String? = null

    override fun getCommentedBlockCommentPrefix(): String? = null

    override fun getCommentedBlockCommentSuffix(): String? = null
}
