package com.github.danieldeng2.waccplugin.language

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class WaccFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, WaccLanguage) {
    override fun getFileType(): FileType = WaccFileType

    override fun toString(): String = "WACC File"
}
