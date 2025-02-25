// This file is licensed under the MIT License.
// See the LICENSE file in the project root for more information:
// https://github.com/Julien-Fischer/break-line/blob/main/LICENSE

package net.agiledeveloper.breakline.splitters.impl

import net.agiledeveloper.breakline.splitters.Splitter
import net.agiledeveloper.breakline.splitters.data.SplitRequest

class IdentitySplitter : Splitter {

    override fun split(request: SplitRequest): String {
        return request.context.line
    }

}
