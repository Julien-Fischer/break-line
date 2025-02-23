package net.agiledeveloper.breakline

@JvmRecord
data class Pair(val opening: Char, val closing: Char) {
    override fun toString(): String {
        return "$opening $closing"
    }
}
