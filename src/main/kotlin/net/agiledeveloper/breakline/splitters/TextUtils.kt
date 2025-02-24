package net.agiledeveloper.breakline.splitters

object TextUtils {

    fun getLeadingWhitespace(line: String): String {
        return line.replace(Regex("^(\\s*).*"), "$1")
    }

}
