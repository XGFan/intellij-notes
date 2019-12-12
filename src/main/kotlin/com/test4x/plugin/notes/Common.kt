package com.test4x.plugin.notes

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

fun VirtualFile.toLocation(project: Project? = null): String? {
    val path = this.path
    return if (path.contains("!")) {
        path.substring(path.indexOf("!") + 1, path.length)
    } else {
        val projectFile = project?.projectFile
        when (projectFile?.name) {
            "project.ipr" -> { //path/to/project/project.ipr
                path.drop(projectFile.parent.path.length)
            }
            "misc.xml" -> { //path/to/project/.idea/misc.xml
                path.drop(projectFile.parent.parent.path.length)
            }
            else -> {
                path
            }
        }
    }
}

fun AnActionEvent.codeLocation(): CodeLocation? {
    val project = project ?: return null
    val location = this.getData(CommonDataKeys.VIRTUAL_FILE)?.toLocation(project) ?: return null
    val line = this.getData(CommonDataKeys.EDITOR)?.currentLine() ?: return null
    return CodeLocation(location, line)
}

fun Editor.currentLine(): Int {
    return this.caretModel.primaryCaret.logicalPosition.line
}

typealias CodeLocation = Pair<String, Int>

val CodeLocation.file
    get() = first
val CodeLocation.line
    get() = second

fun CodeLocation.toText(): String = "$file:$line"
