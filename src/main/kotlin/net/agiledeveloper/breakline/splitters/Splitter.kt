// This file is licensed under the MIT License.
// See the LICENSE file in the project root for more information:
// https://github.com/Julien-Fischer/break-line/blob/main/LICENSE

package net.agiledeveloper.breakline.splitters

import net.agiledeveloper.breakline.splitters.Characters.FOUR_SPACES

interface Splitter {

    fun split(lineText: String, indentation: String): String

    fun split(lineText: String): String {
        return split(lineText, FOUR_SPACES)
    }

}
