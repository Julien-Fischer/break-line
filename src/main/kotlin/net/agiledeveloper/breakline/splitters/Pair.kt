package net.agiledeveloper.breakline.splitters

@JvmRecord
data class Pair(val opening: Char, val closing: Char) {
    override fun toString(): String {
        return "$opening $closing"
    }
}
