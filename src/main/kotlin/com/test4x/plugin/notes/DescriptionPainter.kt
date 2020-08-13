package com.test4x.plugin.notes

import com.intellij.ide.bookmarks.BookmarkManager
import com.intellij.openapi.editor.EditorLinePainter
import com.intellij.openapi.editor.LineExtensionInfo
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.awt.Color

class DescriptionPainter : EditorLinePainter() {
    override fun getLineExtensions(
        project: Project,
        file: VirtualFile,
        lineNumber: Int
    ): MutableCollection<LineExtensionInfo> {
        val bookmarkManager = BookmarkManager.getInstance(project)
        val bookmark = bookmarkManager.findEditorBookmark(
            FileDocumentManager.getInstance().getDocument(file)!!
            , lineNumber
        )
        val arrayList = ArrayList<LineExtensionInfo>()
        bookmark?.let {
            val textAttributes = TextAttributes()
            textAttributes.foregroundColor = Color.LIGHT_GRAY
            arrayList.add(LineExtensionInfo("    ${it.description}", textAttributes))
        }
        return arrayList
    }
}