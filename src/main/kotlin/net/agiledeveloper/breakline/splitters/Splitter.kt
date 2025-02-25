// This file is licensed under the MIT License.
// See the LICENSE file in the project root for more information:
// https://github.com/Julien-Fischer/break-line/blob/main/LICENSE

package net.agiledeveloper.breakline.splitters

import net.agiledeveloper.breakline.splitters.constants.Characters.FOUR_SPACES
import net.agiledeveloper.breakline.splitters.data.CaretContext
import net.agiledeveloper.breakline.splitters.data.SplitRequest

interface Splitter {

    fun split(request: SplitRequest): String

    fun split(line: String): String {
        return split(CaretContext(line, 0))
    }

    fun split(context: CaretContext): String {
        return split(SplitRequest(context, FOUR_SPACES))
    }

}
