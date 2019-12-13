package com.test4x.plugin.notes

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.fileEditor.impl.LoadTextUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.util.concurrent.ConcurrentHashMap

class NotesService(project: Project) {
    companion object {
        const val NoteFile = ".notes.txt"
        fun getInstance(project: Project): NotesService {
            return ServiceManager.getService(project, NotesService::class.java)
        }
    }

    private var lastTimestamp = System.currentTimeMillis() - 50 * 1000
    private val cache = ConcurrentHashMap<String, String>()
    private val localFile: VirtualFile

    init {
        val projectFileSystem = project.projectFile?.parent?.parent!!
        this.localFile = projectFileSystem.findChild(NoteFile)
            ?: ApplicationManager.getApplication().runWriteAction<VirtualFile> {
                projectFileSystem.createChildData(
                    this::class.java,
                    NoteFile
                )
            }
        val loadText = LoadTextUtil.loadText(this.localFile)
        loadText.split("\n").forEach {
            val index = it.indexOf("=")
            if (index != -1) {
                cache[it.substring(0, index)] = it.substring(index + 1)
            }
        }
    }

    fun get(codeLocation: CodeLocation): String? {
        return cache[codeLocation.toText()]
    }

    fun set(codeLocation: CodeLocation, doc: String) {
        cache[codeLocation.toText()] = doc
        saveFile()
    }

    fun clear(codeLocation: CodeLocation): String? {
        return cache.remove(codeLocation.toText())?.also {
            saveFile()
        }
    }

    private fun saveFile() {
        if (System.currentTimeMillis() - lastTimestamp >= 60 * 1000) {
            val toByteArray =
                cache.map { "${it.key}=${it.value}" }.joinToString(separator = "\n").toByteArray(Charsets.UTF_8)
            ApplicationManager.getApplication().runWriteAction {
                localFile.setBinaryContent(toByteArray)
            }
            this.lastTimestamp = System.currentTimeMillis()
        }
    }
}