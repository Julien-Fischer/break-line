// This file is licensed under the MIT License.
// See the LICENSE file in the project root for more information:
// https://github.com/Julien-Fischer/break-line/blob/main/LICENSE

package net.agiledeveloper.breakline

import net.agiledeveloper.breakline.splitters.impl.ChainSplitter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class ChainSplitterTest {

    private val splitter = ChainSplitter()

    @Test
    fun no_function_calls_are_not_split() {
        assertThat(splitter.split("a"))
            .isEqualTo("a")
        assertThat(splitter.split("object"))
            .isEqualTo("object")
    }

    @Test
    fun simple_chains_are_split_across_multiple_lines() {
        val input = "a.b().c().d()"
        val expected = """
            a
                    .b()
                    .c()
                    .d()
        """.trimIndent()

        assertThat(splitter.split(input))
            .isEqualTo(expected)
    }

    @Test
    fun nested_function_calls_are_split_across_multiple_lines() {
        val input = "a.b(x.y().z()).c().d()"
        val expected = """
            a
                    .b(x.y().z())
                    .c()
                    .d()
        """.trimIndent()

        assertThat(splitter.split(input))
            .isEqualTo(expected)
    }

    @Test
    fun single_function_calls_are_split_across_multiple_lines() {
        val input = "a.b()"
        val expected = """
            a
                    .b()
        """.trimIndent()

        assertThat(splitter.split(input))
            .isEqualTo(expected)
    }

    @Test
    fun empty_strings_are_not_split() {
        assertThat(splitter.split(""))
            .isEqualTo("")
    }

    @Test
    fun trailing_whitespaces_are_trimmed() {
        val input = "  a  .b(   x   )   .  c( ) "
        val expected = """
            a
                    .b(   x   )
                    .  c( )
        """.trimIndent()

        assertThat(splitter.split(input.trim { it <= ' ' }))
            .isEqualTo(expected)
    }

    @Test
    fun deeply_nested_function_calls_are_split_gracefully() {
        val input = "a.b(c(d(e(f())))).g().h()"
        val expected = """
            a
                    .b(c(d(e(f()))))
                    .g()
                    .h()
        """.trimIndent()

        assertThat(splitter.split(input))
            .isEqualTo(expected)
    }

    @Test
    fun trailing_dot_is_handled_gracefully() {
        val input = "a.b().c()."
        val expected = """
            a
                    .b()
                    .c()
                    .
        """.trimIndent()

        assertThat(splitter.split(input))
            .isEqualTo(expected)
    }

    @Test
    fun unbalanced_parentheses_is_handled_gracefully() {
        val input = "a.b(c.d("
        val expected = """
            a
                    .b(c.d(
        """.trimIndent()

        assertThat(splitter.split(input))
            .isEqualTo(expected)
    }

    @Test
    fun incomplete_syntax_is_handled_gracefully() {
        val input = "a.b(c.d))"
        val expected = """
            a
                    .b(c.d))
        """.trimIndent()

        assertThat(splitter.split(input))
            .isEqualTo(expected)
    }

    @Test
    fun dot_inside_parameters_do_not_cause_a_split() {
        val input = "a.b(x.y.z).c().d()"
        val expected = """
            a
                    .b(x.y.z)
                    .c()
                    .d()
        """.trimIndent()

        assertThat(splitter.split(input))
            .isEqualTo(expected)
    }

    @Test
    fun property_chaining_is_split_to_multiple_lines() {
        val input = "a.b.c.d.e"
        val expected = """
            a
                    .b
                    .c
                    .d
                    .e
        """.trimIndent()

        assertThat(splitter.split(input))
            .isEqualTo(expected)
    }

    @Test
    fun method_call_chaining_is_split_to_multiple_lines() {
        val input = "new Obj().method1(param1.method2()).method3(param2).method4().method5(param3.method6(param4.method7()))"
        val expected = """new Obj()
        .method1(param1.method2())
        .method3(param2)
        .method4()
        .method5(param3.method6(param4.method7()))"""

        assertThat(splitter.split(input))
            .isEqualTo(expected)
    }

    @Test
    fun indentation_is_preserved() {
        val input = "    List.of(a, b, c)"
        val expected = """    List
            .of(a, b, c)"""

        val output: String = splitter.split(input)

        assertThat(output)
            .isEqualTo(expected)
    }

    @Test
    fun assignments_are_split_to_multiple_lines() {
        val input = "var list =   List.of(a, b, c)"
        val expected = """var list =   List
        .of(a, b, c)"""

        val output: String = splitter.split(input)

        assertThat(output)
            .isEqualTo(expected)
    }

    @Test
    fun lengthy_method_chains_are_split_to_multiple_lines() {
        val input = """List<String> result = List.of("a", "b", "c").stream() .filter(s -> !s.isEmpty()) .map(String::toUpperCase) .flatMap(s -> s.chars().mapToObj(ch -> String.valueOf((char) ch))) .distinct() .sorted() .skip(1) .limit(2) .map(s -> s.repeat(2)) .collect(Collectors.toList());"""
        val expected = """List<String> result = List
        .of("a", "b", "c")
        .stream()
        .filter(s -> !s.isEmpty())
        .map(String::toUpperCase)
        .flatMap(s -> s.chars().mapToObj(ch -> String.valueOf((char) ch)))
        .distinct()
        .sorted()
        .skip(1)
        .limit(2)
        .map(s -> s.repeat(2))
        .collect(Collectors.toList());"""

        val output: String = splitter.split(input)

        assertThat(output)
            .isEqualTo(expected)
    }

}
