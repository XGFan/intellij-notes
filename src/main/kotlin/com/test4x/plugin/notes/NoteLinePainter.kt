package com.test4x.plugin.notes

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.editor.EditorLinePainter
import com.intellij.openapi.editor.LineExtensionInfo
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.awt.Color

//import org.jetbrains.kotlin.com.intellij.psi.PsiIdentifier
//import org.jetbrains.kotlin.com.intellij.psi.PsiMethod

class NoteLinePainter : EditorLinePainter() {
    override fun getLineExtensions(
        project: Project,
        file: VirtualFile,
        lineNumber: Int
    ): MutableCollection<LineExtensionInfo> {
        val service = ServiceManager.getService(project, NotesService::class.java)
        val arrayList = ArrayList<LineExtensionInfo>()
        val location = file.toLocation()
        service.get(location, lineNumber)?.let {
            val textAttributes = TextAttributes()
            textAttributes.foregroundColor = Color.LIGHT_GRAY
            arrayList.add(LineExtensionInfo("    $it", textAttributes))
        }
        return arrayList
    }
}