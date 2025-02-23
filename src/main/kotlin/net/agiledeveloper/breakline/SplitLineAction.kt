package net.agiledeveloper.breakline

import com.intellij.application.options.CodeStyle
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import net.agiledeveloper.breakline.splitters.Characters.FOUR_SPACES
import net.agiledeveloper.breakline.splitters.Characters.TAB
import net.agiledeveloper.breakline.splitters.DeclarationSplitter
import net.agiledeveloper.breakline.splitters.Pair
import net.agiledeveloper.breakline.splitters.Splitter

class SplitLineAction : AnAction() {

    private val supportedPairs = listOf(
        Pair('(', ')'),
        Pair('{', '}'),
        Pair('[', ']')
    )

    private val splitter: Splitter = DeclarationSplitter(supportedPairs)

    override fun actionPerformed(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        val project = e.project

        if (editor == null || project == null) return

        val document = editor.document
        val caretOffset = editor.caretModel.offset
        val lineNumber = document.getLineNumber(caretOffset)

        val lineStart = document.getLineStartOffset(lineNumber)
        val lineEnd = document.getLineEndOffset(lineNumber)

        val lineText = document.text.substring(lineStart, lineEnd)

        val userIndentation: String = getUserPreferredIndentation(editor, project)

        val formatted = splitter.split(lineText, userIndentation)

        ApplicationManager.getApplication().runWriteAction {
            document.replaceString(lineStart, lineEnd, formatted)
        }
    }

    private fun getUserPreferredIndentation(editor: Editor, project: Project): String {
        val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.document)
            ?:
            return FOUR_SPACES

        val codeStyleSettings: CommonCodeStyleSettings = CodeStyle.getLanguageSettings(psiFile)
        val useTabCharacter: Boolean = codeStyleSettings.indentOptions?.USE_TAB_CHARACTER ?: false
        val indentSize = codeStyleSettings.indentOptions?.INDENT_SIZE ?: 4

        return if (useTabCharacter) "$TAB" else " ".repeat(indentSize)
    }

}
