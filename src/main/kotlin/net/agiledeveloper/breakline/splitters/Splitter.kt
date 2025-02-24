// This file is licensed under the MIT License.
// See the LICENSE file in the project root for more information:
// https://github.com/Julien-Fischer/break-line/blob/main/LICENSE

package net.agiledeveloper.breakline.splitters

import net.agiledeveloper.breakline.splitters.Characters.FOUR_SPACES

interface Splitter {

    fun split(line: String, indentation: String): String

    fun split(line: String): String {
        return split(line, FOUR_SPACES)
    }

}
