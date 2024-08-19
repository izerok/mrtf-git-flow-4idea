package com.github.xiaolyuh.http.ui

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

class HttpExecutionConsoleToolWindowFactory : ToolWindowFactory, DumbAware {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentManager = toolWindow.contentManager
        val httpExecutionToolWindow = HttpExecutionToolWindow()

        contentManager.component.add(httpExecutionToolWindow.mainPanel)
    }

    override fun shouldBeAvailable(project: Project): Boolean {
        return false
    }

}
