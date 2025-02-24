package net.agiledeveloper.breakline

import junit.framework.TestCase.assertEquals
import net.agiledeveloper.breakline.splitters.MethodSplitter
import org.junit.Test

internal class MethodSplitterTest {

    private val splitter = MethodSplitter()

    @Test
    fun testSimpleChain() {
        val input = "a.b().c().d()"
        val expected = "a\n.b()\n.c()\n.d()"
        assertEquals(expected, splitter.split(input))
    }

    @Test
    fun testNestedFunctionCalls() {
        val input = "a.b(x.y().z()).c().d()"
        val expected = "a\n.b(x.y().z())\n.c()\n.d()"
        assertEquals(expected, splitter.split(input))
    }

    @Test
    fun testSingleFunctionCall() {
        val input = "a.b()"
        val expected = "a\n.b()"
        assertEquals(expected, splitter.split(input))
    }

    @Test
    fun testNoFunctionCalls() {
        val input = "a"
        val expected = "a"
        assertEquals(expected, splitter.split(input))
    }

    @Test
    fun testEmptyInput() {
        val input = ""
        val expected = ""
        assertEquals(expected, splitter.split(input))
    }

    @Test
    fun testWhitespaceHandling() {
        val input = "  a  .b(   x   )   .c( ) "
        val expected = "a\n.b(   x   )\n.c( )"
        assertEquals(expected, splitter.split(input.trim { it <= ' ' }))
    }

    @Test
    fun testDeeplyNestedFunctionCalls() {
        val input = "a.b(c(d(e(f())))).g().h()"
        val expected = "a\n.b(c(d(e(f()))))\n.g()\n.h()"
        assertEquals(expected, splitter.split(input))
    }

    @Test
    fun testMultipleDotsWithoutParentheses() {
        val input = "a.b.c.d"
        val expected = "a\n.b\n.c\n.d"
        assertEquals(expected, splitter.split(input))
    }

    @Test
    fun testTrailingDot() {
        val input = "a.b().c()."
        val expected = "a\n.b()\n.c()\n."
        assertEquals(expected, splitter.split(input))
    }

    @Test
    fun testUnbalancedParentheses_Open() {
        val input = "a.b(c.d("
        // This is invalid syntax, but the function should handle it gracefully
        val expected = "a\n.b(c.d("
        assertEquals(expected, splitter.split(input))
    }

    @Test
    fun testUnbalancedParentheses_Close() {
        val input = "a.b(c.d))"
        // This is invalid syntax, but the function should handle it gracefully
        val expected = "a\n.b(c.d))"
        assertEquals(expected, splitter.split(input))
    }

    @Test
    fun testDotInsideParameters() {
        val input = "a.b(x.y.z).c().d()"
        // The dot inside parameters should not cause a split
        val expected = "a\n.b(x.y.z)\n.c()\n.d()"
        assertEquals(expected, splitter.split(input))
    }

    @Test
    fun testFunctionWithNoParametersAndNoParentheses() {
        val input = "a.b.c.d.e"
        // No parentheses at all; just split on dots
        val expected = "a\n.b\n.c\n.d\n.e"
        assertEquals(expected, splitter.split(input))
    }

    @Test
    fun testComplexMixedCase() {
        val input = "new Obj().method1(param1.method2()).method3(param2).method4().method5(param3.method6(param4.method7()))"
        // Handles nested calls and proper splitting of the chain
        val expected =
            """
            new Obj()
            .method1(param1.method2())
            .method3(param2)
            .method4()
            .method5(param3.method6(param4.method7()))
            """.trimIndent()

        assertEquals(expected, splitter.split(input))
    }
}
