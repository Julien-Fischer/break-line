// This file is licensed under the MIT License.
// See the LICENSE file in the project root for more information:
// https://github.com/Julien-Fischer/break-line/blob/main/LICENSE

package net.agiledeveloper.breakline.splitters

class IdentitySplitter : Splitter {

    override fun split(line: String, indentation: String): String {
        return line
    }

}
