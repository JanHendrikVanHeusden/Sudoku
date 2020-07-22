package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.lineSeparator
import kotlin.math.max

fun String.tidy(): String = this.trimIndent().replace(Regex("""([\r\n])+"""), lineSeparator)

fun cellContentLength(gridSize: Int): Int = max(gridSize.toString().length, 3)
/**
 * To be used in test classes where we do not really test exact cell content.
 *  * For extensive tests on cell content formatting see [CellBoxFormatterTest].
 * This regular expression can be used to check that content should consist of space, digit and/or fixedValueMarker, and with correct length
 */
val cellContentPattern: (gridSize: Int) -> String = { gridSize ->  """(►|\d| ){${cellContentLength(gridSize)}}""" }
