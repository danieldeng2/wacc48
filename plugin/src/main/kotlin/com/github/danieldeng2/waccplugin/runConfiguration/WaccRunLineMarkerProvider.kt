package com.github.danieldeng2.waccplugin.runConfiguration

import com.intellij.execution.lineMarker.ExecutorAction
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

class WaccRunLineMarkerProvider : RunLineMarkerContributor() {
    override fun getInfo(e: PsiElement): Info? {
        if (e.parent.parent is PsiFile && e.text == "begin") {
            val actions: Array<AnAction> = ExecutorAction.getActions(0)
            return Info(actions[0])
        }
        return null
    }
}
