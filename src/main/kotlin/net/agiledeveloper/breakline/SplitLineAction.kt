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

class SplitLineAction : AnAction() {

    companion object {
        private const val FOUR_SPACES  = "    "
        private const val SINGLE_SPACE = ' '
        private const val NEW_LINE     = '\n'
        private const val TAB          = '\t'
    }

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

        val formatted = splitLineByComma(lineText, userIndentation)

        ApplicationManager.getApplication().runWriteAction {
            document.replaceString(lineStart, lineEnd, formatted)
        }
    }

    fun splitLineByComma(lineText: String): String {
        return splitLineByComma(lineText, FOUR_SPACES)
    }

    fun splitLineByComma(lineText: String, indentation: String): String {
        val pair: Pair? = detectPair(lineText)

        println("using pair %s".format(pair))
        val leadingWhitespace = lineText.replace(Regex("^(\\s*).*"), "$1")

        if (pair != null) {
            val openParenIndex = lineText.indexOf(pair.opening)
            val closeParenIndex = findMatchingParenthesis(lineText, openParenIndex, pair)

            if (isMalformedInput(closeParenIndex)) {
                return lineText
            }

            val beforeParenthesis = lineText.substring(0, openParenIndex + 1).trim { it <= SINGLE_SPACE }
            val insideParenthesis = lineText.substring(openParenIndex + 1, closeParenIndex).trim { it <= SINGLE_SPACE }
            val afterParenthesis  = lineText.substring(closeParenIndex).trim { it <= SINGLE_SPACE }

            val parts = insideParenthesis.split(", ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val result = StringBuilder()

            result
                .append(leadingWhitespace)
                .append(beforeParenthesis)
                .append(NEW_LINE)

            for (part in parts) {
                result
                    .append(leadingWhitespace)
                    .append(indentation)
                    .append(part.trim { it <= SINGLE_SPACE })
                    .append(",$NEW_LINE")
            }

            removeLastTwoCharactersAfterLastPart(result, parts)

            result
                .append(NEW_LINE)
                .append(leadingWhitespace)
                .append(afterParenthesis)

            return result.toString()
        }

        return lineText
    }

    private fun removeLastTwoCharactersAfterLastPart(result: StringBuilder, parts: Array<String>) {
        if (result.isNotEmpty() && parts.isNotEmpty()) {
            result.setLength(result.length - 2)
        }
    }

    private fun isMalformedInput(closeParenIndex: Int): Boolean {
        return closeParenIndex == -1
    }

    private fun findMatchingParenthesis(text: String, openIndex: Int, pair: Pair): Int {
        var depth = 0
        for (i in openIndex..<text.length) {
            val c = text[i]
            if (c == pair.opening) depth++
            if (c == pair.closing) depth--
            if (depth == 0) return i
        }
        return -1
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

    private fun detectPair(text: String): Pair? {
        val pairs = listOf(
            Pair('(', ')'),
            Pair('{', '}'),
            Pair('[', ']')
        )

        for (pair in pairs) {
            if (text.contains(pair.opening) && text.contains(pair.closing)) {
                return pair
            }
        }

        return null
    }

    @JvmRecord
    data class Pair(val opening: Char, val closing: Char) {
        override fun toString(): String {
            return "$opening $closing"
        }
    }

}

