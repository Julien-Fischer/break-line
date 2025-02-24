// This file is licensed under the MIT License.
// See the LICENSE file in the project root for more information:
// https://github.com/Julien-Fischer/break-line/blob/main/LICENSE

package net.agiledeveloper.breakline

import net.agiledeveloper.breakline.splitters.SplitterFactory
import net.agiledeveloper.breakline.splitters.impl.ArgumentSplitter
import net.agiledeveloper.breakline.splitters.impl.ChainSplitter
import net.agiledeveloper.breakline.splitters.impl.IdentitySplitter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class SplitterFactoryTest {

    companion object {
        val CHAIN_SPLITTER    = ChainSplitter::class.java
        val ARGUMENT_SPLITTER = ArgumentSplitter::class.java
        val IDENTITY_SPLITTER = IdentitySplitter::class.java
    }

    private val splitterFactory: SplitterFactory = SplitterFactory()

    @Test
    fun of_returns_chain_splitter_when_caret_within_property_chain() {
        //                                       |
        val input = "String lineText = document.text.substring(lineStart, lineEnd)"

        val splitter = splitterFactory.of(input, caretOffset = 28)

        assertThat(splitter).isInstanceOf(CHAIN_SPLITTER)
    }

    @Test
    fun of_returns_argument_splitter_when_caret_within_method_call() {
        //                                                        |
        val input = "String lineText = document.text.substring(lineStart, lineEnd)"

        val splitter = splitterFactory.of(input, caretOffset = 46)

        assertThat(splitter).isInstanceOf(ARGUMENT_SPLITTER)
    }

    @Test
    fun of_returns_identity_splitter_when_caret_within_variable_type() {
        //                |
        val input = "Byte[] array = {0, 1, 2}"

        val splitter = splitterFactory.of(input, caretOffset = 5)

        assertThat(splitter).isInstanceOf(IDENTITY_SPLITTER)
    }

    @Test
    fun of_returns_argument_splitter_when_caret_within_array_initialization() {
        //                                |
        val input = "Byte[] array = {0, 1, 2}"

        val splitter = splitterFactory.of(input, caretOffset = 22)

        assertThat(splitter).isInstanceOf(ARGUMENT_SPLITTER)
    }

    @Test
    fun of_returns_argument_splitter_when_caret_within_explicit_array_initialization() {
        //                                    |
        val input = "var array = new Byte[] {0, 1, 2}"

        val splitter = splitterFactory.of(input, caretOffset = 25)

        assertThat(splitter).isInstanceOf(ARGUMENT_SPLITTER)
    }

    @Test
    fun of_returns_identity_splitter_when_caret_within_variable_name() {
        //                    |
        val input = "Byte[] array = {0, 1, 2}"

        val splitter = splitterFactory.of(input, caretOffset = 9)

        assertThat(splitter).isInstanceOf(IDENTITY_SPLITTER)
    }

    @Test
    fun of_returns_identity_splitter_when_caret_within_whitespaces() {
        //              |
        val input = "var array = new Byte[] {0, 1, 2}"

        val splitter = splitterFactory.of(input, caretOffset = 3)

        assertThat(splitter).isInstanceOf(IDENTITY_SPLITTER)
    }

    @Test
    fun of_returns_chain_splitter_when_caret_within_method_chain() {
        //                |
        val input = "a.b().c().d()"

        val splitter = splitterFactory.of(input, caretOffset = 4)

        assertThat(splitter).isInstanceOf(CHAIN_SPLITTER)
    }

    @Test
    fun of_returns_argument_splitter_when_caret_within_empty_parentheses() {
        //               |
        val input = "a.b().c().d()"

        val splitter = splitterFactory.of(input, caretOffset = 3)

        assertThat(splitter).isInstanceOf(ARGUMENT_SPLITTER)
    }

    @Test
    fun of_returns_chain_splitter_when_caret_within_empty_parentheses() {
        //            |
        val input = "a.b().c().d()"

        val splitter = splitterFactory.of(input, caretOffset = 1)

        assertThat(splitter).isInstanceOf(CHAIN_SPLITTER)
    }

    @Test
    fun of_returns_identity_splitter_when_caret_within_generic_type() {
        //                    |
        val input = """List<String> result = List.of("a", "b", "c")"""

        val splitter = splitterFactory.of(input, caretOffset = 1)

        assertThat(splitter).isInstanceOf(IDENTITY_SPLITTER)
    }

}
