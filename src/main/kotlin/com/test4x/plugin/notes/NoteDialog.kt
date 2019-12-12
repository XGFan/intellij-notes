package com.test4x.plugin.notes

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.editor.impl.DocumentImpl
import com.intellij.openapi.fileTypes.FileTypes
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.EditorTextField
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JPanel

class NoteDialog(private val codeLocation: CodeLocation, project: Project) : DialogWrapper(project, true) {

    private val editorTextField: EditorTextField
    private val service = ServiceManager.getService(project, NotesService::class.java)

    init {
        title = codeLocation.toText()
        editorTextField = EditorTextField(
            DocumentImpl(service.get(codeLocation) ?: ""),
            project,
            FileTypes.PLAIN_TEXT,
            false,
            true
        ).apply {
            preferredSize = Dimension(450, 100)
            addSettingsProvider {
                it.settings.isUseSoftWraps = true  //do not work with oneLineMode
                it.setVerticalScrollbarVisible(false)
            }
        }
        init()
    }

    override fun createCenterPanel(): JComponent? {
        val dialogPanel = JPanel(BorderLayout())
        dialogPanel.add(editorTextField, BorderLayout.CENTER)
        return dialogPanel
    }

    override fun doOKAction() {
        if (editorTextField.text.isNotBlank()) {
            service.set(codeLocation, editorTextField.text.trim())
        } else {
            service.clear(codeLocation)
        }
        super.doOKAction()
    }


    override fun getPreferredFocusedComponent(): JComponent? {
        return editorTextField
    }
}