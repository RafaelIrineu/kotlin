/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.debugger.coroutines

import com.intellij.debugger.DebuggerManagerEx
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import com.intellij.openapi.project.ProjectManager
import com.intellij.xdebugger.impl.XDebuggerUtilImpl

class ShowCreationStackTraceAction : ToggleAction() {
    private val settings = CoroutinesViewPopupSettings.getInstance()

    override fun isSelected(e: AnActionEvent): Boolean {
        return settings.showCoroutineCreationStackTrace
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        val changed = settings.showCoroutineCreationStackTrace != state
        settings.showCoroutineCreationStackTrace = state
        if (changed) {
            for (project in ProjectManager.getInstance().openProjects) {
                for (session in DebuggerManagerEx.getInstanceEx(project).sessions) {
                    session.refresh(false)
                }
                XDebuggerUtilImpl.rebuildAllSessionsViews(project)
            }
        }
    }
}