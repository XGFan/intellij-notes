package com.test4x.plugin.notes.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.ServiceManager
import com.test4x.plugin.notes.NotesService
import com.test4x.plugin.notes.codeLocation


class ClearNoteAction : AnAction("Clear Note") {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val codeLocation = event.codeLocation() ?: return
        ServiceManager.getService(project, NotesService::class.java).clear(codeLocation)
    }
}