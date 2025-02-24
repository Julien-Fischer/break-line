// This file is licensed under the MIT License.
// See the LICENSE file in the project root for more information:
// https://github.com/Julien-Fischer/break-line/blob/main/LICENSE

package net.agiledeveloper.breakline.splitters

import net.agiledeveloper.breakline.splitters.Characters.DOT
import net.agiledeveloper.breakline.splitters.Characters.FOUR_SPACES
import net.agiledeveloper.breakline.splitters.Characters.NEW_LINE
import net.agiledeveloper.breakline.splitters.Characters.SINGLE_SPACE
import java.util.*

class ChainSplitter : Splitter {

    private val pair = Pair('(', ')')

    private var firstLine = true
    private var leadingWhitespace: String = ""
    private var lineIndentation: String = FOUR_SPACES

    override fun split(line: String, indentation: String): String {
        firstLine = true
        leadingWhitespace = TextUtils.getLeadingWhitespace(line)
        lineIndentation = indentation

        val result = StringBuilder()
        val currentLine = StringBuilder()
        val stack = Stack<Char>()

        for (currentChar in line) {
            updateStack(currentChar, stack)

            if (shouldStartNewLine(currentChar, stack)) {
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

    private fun updateStack(currentChar: Char, stack: Stack<Char>) {
        if (currentChar == pair.opening) {
            stack.push(currentChar)
        } else if (currentChar == pair.closing && stack.isNotEmpty()) {
            stack.pop()
        }
    }

    private fun shouldStartNewLine(currentChar: Char, stack: Stack<Char>): Boolean {
        return currentChar == DOT && stack.isEmpty()
    }
}
