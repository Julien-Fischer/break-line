package net.agiledeveloper.breakline

import net.agiledeveloper.breakline.Characters.FOUR_SPACES

interface Splitter {

    fun split(lineText: String, indentation: String): String

    fun split(lineText: String): String {
        return split(lineText, FOUR_SPACES)
    }

}
