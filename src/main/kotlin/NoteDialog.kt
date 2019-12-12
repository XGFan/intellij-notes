import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.editor.impl.DocumentImpl
import com.intellij.openapi.fileTypes.FileTypes
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.EditorTextField
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JPanel

class NoteDialog(val location: String, val line: Int, project: Project) : DialogWrapper(project, true) {

    private val editorTextField: EditorTextField
    private val service = ServiceManager.getService(project, NotesService::class.java)

    init {
        title = "$location:$line"
        editorTextField = EditorTextField(
            DocumentImpl(service.get(location, line) ?: ""),
            project,
            FileTypes.PLAIN_TEXT,
            false,
            true
        )
        // component.getInputMap().put(aKeyStroke, aCommand);
        // component.getActionMap().put(aCommmand, anAction);
//        editorTextField.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "SAVE")
//        editorTextField.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("x"), "SAVE")
//        editorTextField.actionMap.put("SAVE", object : AbstractAction() {
//            override fun actionPerformed(e: ActionEvent?) {
//                println("hello")
//            }
//        })
        editorTextField.preferredSize = Dimension(450, 100)
        editorTextField.addSettingsProvider {
            it.settings.isUseSoftWraps = true
            it.setVerticalScrollbarVisible(false)
        }
        init()
    }

    override fun createCenterPanel(): JComponent? {
        val dialogPanel = JPanel(BorderLayout())
        dialogPanel.add(editorTextField, BorderLayout.CENTER)
        return dialogPanel
    }

    override fun doOKAction() {
        if (editorTextField.text.isNotBlank()) {
            service.set(location, line, editorTextField.text)
        }
        super.doOKAction()
    }


    override fun getPreferredFocusedComponent(): JComponent? {
        return editorTextField
    }
}