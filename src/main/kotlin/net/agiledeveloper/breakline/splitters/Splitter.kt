package net.agiledeveloper.breakline.splitters

import net.agiledeveloper.breakline.splitters.Characters.FOUR_SPACES

interface Splitter {

    fun split(lineText: String, indentation: String): String

    fun split(lineText: String): String {
        return split(lineText, FOUR_SPACES)
    }

}
