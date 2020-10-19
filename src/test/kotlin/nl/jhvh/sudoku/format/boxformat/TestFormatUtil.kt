package nl.jhvh.sudoku.format.boxformat

/* Copyright 2020 Jan-Hendrik van Heusden

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

import nl.jhvh.sudoku.format.lineSeparator
import kotlin.math.max

private val lineEndPattern: Regex = Regex("""[\r\n]+""")

fun String.tidy(): String = this.trimIndent().replace(lineEndPattern, lineSeparator)

fun cellContentLength(maxValue: Int): Int = max(maxValue.toString().length, 3)
/**
 * To be used in test classes where we do not really test exact cell content.
 *  * For extensive tests on cell content formatting see [CellBoxFormatterTest].
 * This regular expression can be used to check that content should consist of space, digit and/or fixedValueMarker, and with correct length
 */
val cellContentPattern: (maxValue: Int) -> String = { gridSize ->  """(►|\d| ){${cellContentLength(gridSize)}}""" }
