package com.test4x.plugin.notes

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.util.io.createFile
import com.intellij.util.io.exists
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
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
    private val file: Path

    init {
        val projectFileSystem = project.projectFile?.parent?.parent!!
        file = Paths.get(projectFileSystem.path).resolve(NoteFile)
        if (!file.exists()) {
            file.createFile()
        }
        Files.readAllLines(file).forEach {
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
        if (System.currentTimeMillis() - lastTimestamp >= 5 * 1000) {
            val toByteArray =
                cache.map { "${it.key}=${it.value}" }.joinToString(separator = "\n").toByteArray(Charsets.UTF_8)
            Files.write(file, toByteArray)
            this.lastTimestamp = System.currentTimeMillis()
        }
    }
}