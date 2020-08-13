package com.test4x.plugin.notes.action

import com.intellij.ide.bookmarks.BookmarkManager
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.ex.EditorGutterComponentEx


class EditNoteAction : AnAction("Set Bookmark with Description") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val bookmarkManager = BookmarkManager.getInstance(project) ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: e.getData(CommonDataKeys.EDITOR_EVEN_IF_INACTIVE) ?: return
        val document = editor.document
        val gutterLineAtCursor =
            e.getData(EditorGutterComponentEx.LOGICAL_LINE_AT_CURSOR)
        val myLine = gutterLineAtCursor ?: editor.caretModel
            .logicalPosition.line
        val bookmark = bookmarkManager.findEditorBookmark(document, myLine)
        if (bookmark == null) {
            ActionManager.getInstance().getAction("ToggleBookmark")?.actionPerformed(e)
        }
        ActionManager.getInstance().getAction("EditBookmark")?.actionPerformed(e)
    }
}
