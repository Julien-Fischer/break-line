package net.agiledeveloper.breakline.splitters

import net.agiledeveloper.breakline.splitters.data.Pair
import java.util.*

class DelimiterStack(private val syntax: DelimiterSyntax) {

    private val stack = Stack<Char>()

    constructor(supportedPairs: Set<Pair>) : this(DelimiterSyntax(supportedPairs))

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
