import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys


class EditNoteAction : AnAction("Edit Note") {
    override fun actionPerformed(event: AnActionEvent) {
        val virtualFile = event.getData(CommonDataKeys.VIRTUAL_FILE)
        val location = virtualFile!!.toLocation()
        val line = event.getData(CommonDataKeys.EDITOR)!!.caretModel.primaryCaret.logicalPosition.line
        val sampleDialogWrapper = NoteDialog(location, line, event.project!!)
        sampleDialogWrapper.show()
    }
}
