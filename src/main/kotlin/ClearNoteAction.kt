import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.ServiceManager


class ClearNoteAction : AnAction("Clear Note") {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project!!
        val location = event.getData(CommonDataKeys.VIRTUAL_FILE)!!.toLocation()
        val line = event.getData(CommonDataKeys.EDITOR)!!.caretModel.primaryCaret.logicalPosition.line
        ServiceManager.getService(project, NotesService::class.java).clear(location, line)
    }
}