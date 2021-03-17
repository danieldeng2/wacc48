package com.github.danieldeng2.waccplugin.action

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

class WaccShellToolWindow : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        WaccShellHelpers.newShellWindow(project, toolWindow)
    }
}
