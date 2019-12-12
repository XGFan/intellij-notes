package com.test4x.plugin.notes.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.test4x.plugin.notes.NoteDialog
import com.test4x.plugin.notes.codeLocation


class EditNoteAction : AnAction("Edit Note") {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val codeLocation = event.codeLocation() ?: return
        NoteDialog(codeLocation, project).show()
    }
}
