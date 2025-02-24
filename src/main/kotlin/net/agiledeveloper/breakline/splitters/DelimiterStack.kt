package net.agiledeveloper.breakline.splitters

import java.util.*

class DelimiterStack(private val syntax: SplitterSyntax) {

    private val stack = Stack<Char>()

    constructor(supportedPairs: List<Pair>) : this(SplitterSyntax(supportedPairs))

    fun offer(char: Char) {
        if (syntax.isOpeningChar(char)) {
            stack.push(char)
        } else if (syntax.isClosingChar(char) && isNotEmpty()) {
            stack.pop()
        }
    }

    fun isEmpty(): Boolean = stack.isEmpty()

    fun isNotEmpty(): Boolean = !isEmpty()

    fun size(): Int = stack.size

}
