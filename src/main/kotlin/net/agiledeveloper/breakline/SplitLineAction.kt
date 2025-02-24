// This file is licensed under the MIT License.
// See the LICENSE file in the project root for more information:
// https://github.com/Julien-Fischer/break-line/blob/main/LICENSE

package net.agiledeveloper.breakline

import com.intellij.application.options.CodeStyle
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import net.agiledeveloper.breakline.splitters.SplitterFactory
import net.agiledeveloper.breakline.splitters.constants.Characters.FOUR_SPACES
import net.agiledeveloper.breakline.splitters.constants.Characters.TAB
import net.agiledeveloper.breakline.splitters.data.Context

class SplitLineAction : AnAction() {

    private val splitterFactory: SplitterFactory = SplitterFactory()

    override fun actionPerformed(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        val project = e.project

        if (editor == null || project == null) return

        val document = editor.document
        val caretOffsetAbsolute = editor.caretModel.offset
        val lineNumber = document.getLineNumber(caretOffsetAbsolute)
        val lineStartOffset = editor.document.getLineStartOffset(lineNumber)
        val caretOffsetRelative = caretOffsetAbsolute - lineStartOffset

        val lineStart = document.getLineStartOffset(lineNumber)
        val lineEnd = document.getLineEndOffset(lineNumber)


        val lineText = document.text.substring(lineStart, lineEnd)

        val splitter = splitterFactory.of(Context(lineText, caretOffsetRelative))

        val userIndentation: String = getUserPreferredIndentation(editor, project)

        val formatted = splitter.split(lineText, userIndentation)

        WriteCommandAction.runWriteCommandAction(project) {
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
