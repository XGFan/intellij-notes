import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.fileEditor.impl.LoadTextUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.util.concurrent.ConcurrentHashMap

class NotesService(val project: Project) {
    companion object {
        fun getInstance(project: Project): NotesService {
            return ServiceManager.getService(project, NotesService::class.java)
        }
    }

    var lastTimestamp = System.currentTimeMillis()
    private val cache = ConcurrentHashMap<String, String>()
    private val localFile: VirtualFile

    init {
        val projectFileSystem = project.projectFile?.parent!!
        this.localFile = projectFileSystem.findChild("notes.properties")
            ?: ApplicationManager.getApplication().runWriteAction<VirtualFile> {
                projectFileSystem.createChildData(
                    "NotesService",
                    "notes.properties"
                )
            }
        val loadText = LoadTextUtil.loadText(this.localFile)
        loadText.split("\n").forEach {
            val index = it.indexOf("=")
            if(index!= -1){
                cache[it.substring(0, index)] = it.substring(index + 1)
            }
        }
        //read file
    }

    fun get(location: String, line: Int): String? {
        return cache["$location:$line"]
    }

    fun set(location: String, line: Int, doc: String) {
        cache["$location:$line"] = doc
        saveFile()
    }

    fun clear(location: String, line: Int) {
        cache.remove("$location:$line")
        saveFile()
    }

    private fun saveFile() {
        if (System.currentTimeMillis() - lastTimestamp >= 30 * 1000) {
            val toByteArray =
                cache.map { "${it.key}=${it.value}" }.joinToString(separator = "\n").toByteArray(Charsets.UTF_8)
            ApplicationManager.getApplication().runWriteAction {
                localFile.setBinaryContent(toByteArray)
            }
            this.lastTimestamp = System.currentTimeMillis()
        }
    }
}