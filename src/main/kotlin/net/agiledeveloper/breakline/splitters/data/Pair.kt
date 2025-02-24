// This file is licensed under the MIT License.
// See the LICENSE file in the project root for more information:
// https://github.com/Julien-Fischer/break-line/blob/main/LICENSE

package net.agiledeveloper.breakline.splitters.data

@JvmRecord
data class Pair(val opening: Char, val closing: Char) {
    override fun toString(): String {
        return "$opening $closing"
    }
}
