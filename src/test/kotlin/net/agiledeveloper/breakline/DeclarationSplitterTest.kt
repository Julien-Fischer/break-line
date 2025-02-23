package net.agiledeveloper.breakline

import net.agiledeveloper.breakline.splitters.DeclarationSplitter
import net.agiledeveloper.breakline.splitters.Pair
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class DeclarationSplitterTest {

    private val testPairs = listOf(
        Pair('(', ')'),
        Pair('{', '}'),
        Pair('[', ']')
    )

    private val splitter = DeclarationSplitter(testPairs)

    @Test
    fun list_initialization_is_split_across_multiple_lines() {
        val input = "List.of(a, b, c)"
        val expected = """List.of(
    a,
    b,
    c
)"""
        val output: String = splitter.split(input)

        assertThat(output)
            .isEqualTo(expected)
    }

    @Test
    fun array_initialization_is_split_across_multiple_lines() {
        val input = "new String[] {a, b, c}"
        val expected = """new String[] {
    a,
    b,
    c
}"""

        val output: String = splitter.split(input)

        assertThat(output)
            .isEqualTo(expected)
    }

    @Test
    fun empty_method_signature_is_split_across_multiple_lines() {
        val input = "private void f() {"
        val expected = """private void f(

) {"""

        val output: String = splitter.split(input)

        assertThat(output)
            .isEqualTo(expected)
    }

    @Test
    fun method_signature_is_split_across_multiple_lines() {
        val input = "private void f(String a, byte b, Object c) throws IOException {"
        val expected = """private void f(
    String a,
    byte b,
    Object c
) throws IOException {"""

        val output: String = splitter.split(input)

        assertThat(output)
            .isEqualTo(expected)
    }

    @Test
    fun constructor_signature_is_split_across_multiple_lines() {
        val input = "public Lifecycle(Inventory inventory, Instrumentation instrumentation, Clock clock) {"
        val expected = """public Lifecycle(
    Inventory inventory,
    Instrumentation instrumentation,
    Clock clock
) {""".trimMargin()

        val output: String = splitter.split(input)

        assertThat(output)
            .isEqualTo(expected)
    }

    @Test
    fun empty_parentheses_are_split_across_multiple_lines() {
        val input = "List.of()"
        val expected = """List.of(

)""".trimIndent()

        val output: String = splitter.split(input)

        assertThat(output)
            .isEqualTo(expected)
    }

    @Test
    fun empty_braces_are_split_across_multiple_lines() {
        val input = "new int[] {}"
        val expected = """new int[] {

}"""

        val output: String = splitter.split(input)

        assertThat(output)
            .isEqualTo(expected)
    }

    @Test
    fun single_element_list_is_split_across_multiple_lines() {
        val input = "List.of(a)"
        val expected = """List.of(
    a
)"""

        val output: String = splitter.split(input)

        assertThat(output)
            .isEqualTo(expected)
    }

    @Test
    fun testSingleElementWithBraces() {
        val input = "new int[] {a}"
        val expected = """new int[] {
    a
}""".trimIndent()

        val output: String = splitter.split(input)

        assertThat(output)
            .isEqualTo(expected)
    }

    @Test
    fun indentation_is_preserved() {
        val input = "    List.of(a, b, c)"
        val expected = """    List.of(
        a,
        b,
        c
    )"""

        val output: String = splitter.split(input)

        assertThat(output)
            .isEqualTo(expected)
    }

    @Test
    fun input_when_missing_closing_brace_remains_unchanged() {
        val input = "new int[] {a, b"

        assertThat(input)
            .isEqualTo(input)
    }

    @Test
    fun input_when_missing_closing_parenthesis_remains_unchanged() {
        val input = "List.of(a, b"

        assertThat(input)
            .isEqualTo(input)
    }

    @Test
    fun input_when_empty_string_remains_unchanged() {
        val input = ""

        assertThat("")
            .isEqualTo(input)
    }

    @Test
    fun input_when_no_enclosing_characters_remains_unchanged() {
        val input = "a, b, c"

        assertThat("a, b, c")
            .isEqualTo(input)
    }

    @Test
    fun nested_lists_are_split_across_multiple_lines() {
        val input = "List.of(List.of(a, b), c)"
        val expected = """List.of(
    List.of(a, b),
    c
)"""

        val output: String = splitter.split(input)

        assertThat(output)
            .isEqualTo(expected)
    }

    @Test
    fun nested_arrays_are_split_across_multiple_lines() {
        val input = "new Object[] {new int[] {1, 2}, 3}"
        val expected = """new Object[] {
    new int[] {1, 2},
    3
}"""

        val output: String = splitter.split(input)

        assertThat(output)
            .isEqualTo(expected)
    }

    @Test
    fun nested_sets_are_split_across_multiple_lines() {
        val input = """var nestedSets = Set.of(Set.of("foo", "bar", "baz"), Set.of("foo", "bar", "baz"), Set.of("foo", "bar", "baz"));"""
        val expected = """var nestedSets = Set.of(
    Set.of("foo", "bar", "baz"),
    Set.of("foo", "bar", "baz"),
    Set.of("foo", "bar", "baz")
);"""

        val output: String = splitter.split(input)

        assertThat(output)
            .isEqualTo(expected)
    }

    @Test
    fun method_calls_are_split_across_multiple_lines() {
        val input = "var channel = new Channel(id, device.macAddress(), type, zone.path());"
        val expected = """var channel = new Channel(
    id,
    device.macAddress(),
    type,
    zone.path()
);""".trimMargin()

        val output: String = splitter.split(input)

        assertThat(output)
            .isEqualTo(expected)
    }

}
