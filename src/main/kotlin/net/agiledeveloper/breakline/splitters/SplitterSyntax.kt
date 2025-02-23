package net.agiledeveloper.breakline.splitters

class SplitterSyntax(
    private val supportedPairs: List<Pair>
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
