package net.agiledeveloper.breakline.splitters

import net.agiledeveloper.breakline.splitters.Characters.DOT
import net.agiledeveloper.breakline.splitters.Characters.NEW_LINE
import net.agiledeveloper.breakline.splitters.Characters.SINGLE_SPACE
import java.util.*

class MethodSplitter : Splitter {

    private val pair = Pair('(', ')')

    override fun split(line: String, indentation: String): String {
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
            result.append(currentLine.toString().trim { it <= SINGLE_SPACE })
        }

        return result.toString()
    }

    private fun startNewLine(currentChar: Char, currentLine: StringBuilder, result: StringBuilder) {
            if (currentLine.isNotEmpty()) {
                result
                    .append(currentLine.toString().trim { it <= SINGLE_SPACE })
                    .append(NEW_LINE)
            }
            currentLine.setLength(0)
            currentLine.append(currentChar)
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
