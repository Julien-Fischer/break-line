// This file is licensed under the MIT License.
// See the LICENSE file in the project root for more information:
// https://github.com/Julien-Fischer/break-line/blob/main/LICENSE

package net.agiledeveloper.breakline.splitters

import net.agiledeveloper.breakline.splitters.data.Pair

class DelimiterSyntax(
    private val supportedPairs: Set<Pair>
) {

    fun isOpeningChar(char: Char): Boolean {
        return supportedPairs.any { it.opening == char }
    }

    fun isClosingChar(char: Char): Boolean {
        return supportedPairs.any { it.closing == char }
    }

    fun detectPair(text: String): Pair? {
        for (pair in supportedPairs) {
            if (text.contains(pair.opening) && text.contains(pair.closing)) {
                return pair
            }
        }
        return null
    }

}
