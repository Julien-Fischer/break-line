// This file is licensed under the MIT License.
// See the LICENSE file in the project root for more information:
// https://github.com/Julien-Fischer/break-line/blob/main/LICENSE

package net.agiledeveloper.breakline.splitters

import net.agiledeveloper.breakline.splitters.Characters.DOT
import net.agiledeveloper.breakline.splitters.Characters.HEIGHT_SPACES
import net.agiledeveloper.breakline.splitters.Characters.NEW_LINE
import net.agiledeveloper.breakline.splitters.Characters.SINGLE_SPACE

class ChainSplitter : Splitter {

    private val stack = DelimiterStack(listOf(
        Pair('(', ')')
    ))

    private var firstLine = true
    private var leadingWhitespace: String = ""
    private var lineIndentation: String = HEIGHT_SPACES

    override fun split(line: String, indentation: String): String {
        firstLine = true
        leadingWhitespace = TextUtils.getLeadingWhitespace(line)
        lineIndentation = indentation.repeat(2)

        val result = StringBuilder()
        val currentLine = StringBuilder()

        for (currentChar in line) {
            stack.offer(currentChar)
            if (shouldStartNewLine(currentChar)) {
                startNewLine(currentChar, currentLine, result)
            } else {
                currentLine.append(currentChar)
            }
        }

        if (currentLine.isNotEmpty()) {
            result
                .append(getIndentation())
                .append(currentLine.toString().trim { it <= SINGLE_SPACE })
        }

        return result.toString().trimEnd()
    }

    private fun startNewLine(currentChar: Char, currentLine: StringBuilder, result: StringBuilder) {
        if (currentLine.isNotEmpty()) {
            result
                .append(getIndentation())
                .append(currentLine.toString().trim { it <= SINGLE_SPACE })
                .append(NEW_LINE)
        }
        currentLine.setLength(0)
        currentLine.append(currentChar)
        firstLine = false
    }

    private fun getIndentation(): String {
        return leadingWhitespace + if (firstLine) "" else lineIndentation
    }

    private fun shouldStartNewLine(currentChar: Char): Boolean {
        return currentChar == DOT && stack.isEmpty()
    }

}
