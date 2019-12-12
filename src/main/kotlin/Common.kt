import com.intellij.openapi.vfs.VirtualFile

fun VirtualFile.toLocation(): String {
    val path = this.path ?: return ""
    return if (path.contains("!")) {
        path.substring(path.indexOf("!") + 1, path.length)
    } else {
        path
    }
}