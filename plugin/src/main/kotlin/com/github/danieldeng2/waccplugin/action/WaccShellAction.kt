package com.github.danieldeng2.waccplugin.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.wm.ToolWindowManager

class WaccShellAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val toolWindow = ToolWindowManager.getInstance(e.project!!).getToolWindow("WACC")
        if (toolWindow != null) {
            toolWindow.activate(null)
            WaccShellHelpers.newShellWindow(e.project!!, toolWindow)
        }
    }
}
