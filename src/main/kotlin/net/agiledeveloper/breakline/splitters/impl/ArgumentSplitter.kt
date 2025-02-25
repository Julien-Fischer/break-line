// This file is licensed under the MIT License.
// See the LICENSE file in the project root for more information:
// https://github.com/Julien-Fischer/break-line/blob/main/LICENSE

package net.agiledeveloper.breakline.splitters.impl

import net.agiledeveloper.breakline.splitters.Splitter
import net.agiledeveloper.breakline.splitters.SplitterSyntax
import net.agiledeveloper.breakline.splitters.constants.Characters.NEW_LINE
import net.agiledeveloper.breakline.splitters.constants.Characters.SINGLE_SPACE
import net.agiledeveloper.breakline.splitters.data.Pair
import net.agiledeveloper.breakline.splitters.data.SplitRequest
import net.agiledeveloper.breakline.splitters.utils.TextUtils

class ArgumentSplitter(
    private val syntax: SplitterSyntax
) : Splitter {

    constructor(pairs: List<Pair>) : this(SplitterSyntax(pairs))

    override fun split(request: SplitRequest): String {
        val caretContext = request.context
        val line = caretContext.line
        val pair: Pair? = syntax.detectPair(line)

        val leadingWhitespace = TextUtils.getLeadingWhitespace(line)

        if (pair != null) {
            val openParenIndex = line.indexOf(pair.opening)
            val closeParenIndex = findMatchingCharacter(line, openParenIndex, pair)

            if (isMalformedInput(closeParenIndex)) {
                return line
            }

            val before = line.substring(0, openParenIndex + 1).trim { it <= SINGLE_SPACE }
            val inside = line.substring(openParenIndex + 1, closeParenIndex).trim { it <= SINGLE_SPACE }
            val after  = line.substring(closeParenIndex).trim { it <= SINGLE_SPACE }

            val parts = splitPartsWithNesting(inside)
            val result = StringBuilder()

            result
                .append(leadingWhitespace)
                .append(before)
                .append(NEW_LINE)

            for (part in parts) {
                result
                    .append(leadingWhitespace)
                    .append(request.userIndentation)
                    .append(part.trim { it <= SINGLE_SPACE })
                    .append(",$NEW_LINE")
            }

            removeLastTwoCharactersAfterLastPart(result, parts)

            result
                .append(NEW_LINE)
                .append(leadingWhitespace)
                .append(after)

            return result.toString()
        }

        return line
    }

    private fun splitPartsWithNesting(text: String): List<String> {
        val parts = mutableListOf<String>()
        var currentPart = StringBuilder()
        var depth = 0
        var inQuotes = false

        for (char in text) {
            when {
                char == '"' -> inQuotes = !inQuotes
                !inQuotes && syntax.isOpeningChar(char) -> depth++
                !inQuotes && syntax.isClosingChar(char) -> depth--
                !inQuotes && char == ',' && depth == 0 -> {
                    parts.add(currentPart.toString().trim())
                    currentPart = StringBuilder()
                    continue
                }
            }
            currentPart.append(char)
        }

        if (currentPart.isNotEmpty()) {
            parts.add(currentPart.toString().trim())
        }

        return parts
    }

    private fun removeLastTwoCharactersAfterLastPart(result: StringBuilder, parts: List<String>) {
        if (result.isNotEmpty() && parts.isNotEmpty()) {
            result.setLength(result.length - 2)
        }
    }

    private fun isMalformedInput(closeParenIndex: Int): Boolean {
        return closeParenIndex == -1
    }

    private fun findMatchingCharacter(text: String, openIndex: Int, pair: Pair): Int {
        var depth = 0
        for (i in openIndex..<text.length) {
            val c = text[i]
            if (c == pair.opening) depth++
            if (c == pair.closing) depth--
            if (depth == 0) return i
        }
        return -1
    }

}
