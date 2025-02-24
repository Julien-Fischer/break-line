// This file is licensed under the MIT License.
// See the LICENSE file in the project root for more information:
// https://github.com/Julien-Fischer/break-line/blob/main/LICENSE

package net.agiledeveloper.breakline.splitters

import java.util.*

class SplitterFactory {

    private val supportedPairs = listOf(
        Pair('(', ')'),
        Pair('{', '}'),
        Pair('[', ']')
    )

    private val syntax = SplitterSyntax(supportedPairs)

    fun of(input: String, caretOffset: Int): Splitter {
        return of(Context(input, caretOffset))
    }

    fun of(context: Context): Splitter {
        if (shouldSplitArguments(context)) {
            return ArgumentSplitter(supportedPairs)
        }
        if (shouldSplitChains(context)) {
            return ChainSplitter()
        }
        return IdentitySplitter()
    }

    private fun shouldSplitArguments(context: Context): Boolean {
        val stack = Stack<Char>()
        println("${context.caretOffset}, ${context.line.length}, ${context.line}")
        for (i in 0..context.caretOffset) {
            val currentChar = context.line[i]

            if (syntax.isOpeningChar(currentChar)) {
                stack.push(currentChar)
            } else if (syntax.isClosingChar(currentChar)) {
                stack.pop()
            }
        }
        return stack.isNotEmpty()
    }

    private fun shouldSplitChains(context: Context): Boolean {
        var inString = false
        var inNumber = false
        var inVarargs = false

        for (i in 0..context.caretOffset) {
            val currentChar = context.line[i]

            when {
                currentChar == '"' -> inString = !inString
                currentChar == '.' && !inString && !inNumber && !inVarargs -> return true
                currentChar.isDigit() -> inNumber = true
                currentChar == '.' && inNumber -> {}
                currentChar.isLetter() || currentChar == '_' -> inNumber = false
                currentChar == '.' && i > 0 && context.line[i-1] == '.' && context.line[i-2] == '.' -> inVarargs = true
                !currentChar.isWhitespace() -> inVarargs = false
            }
        }
        return false
    }

}
