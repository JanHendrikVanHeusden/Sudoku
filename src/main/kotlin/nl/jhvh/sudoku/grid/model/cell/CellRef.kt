@file:Suppress("KDocUnresolvedReference")

package nl.jhvh.sudoku.grid.model.cell

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

import nl.jhvh.sudoku.base.MAX_BLOCK_SIZE
import nl.jhvh.sudoku.grid.model.cell.CellRef.CellRefCalculation.equals
import nl.jhvh.sudoku.grid.model.cell.CellRef.CellRefCalculation.hashCode
import nl.jhvh.sudoku.util.requireAndLog
import org.slf4j.event.Level

/**
 * [CellRef] is intended to keep and calculate between alphanumeric references and numeric references only.
 * It has no reference to any [Cell] or [Grid].
 *  * So 2 [CellRef] objects are considered equal when they have the same coordinates, regardless
 *    of any context (`Cell`, `Grid`, etc.) they are used in.
 *
 * @constructor Construct a [CellRef] by its internal technical coordinates
 * @param x The internal, technical representation of the [Col]'s sequence number; zero based.
 *          So the 2nd column (indicated by "`2`") would correspond with [x]-value **`1`**.
 * @param y The internal, technical representation of the [Row]'s sequence number; zero based.
 *          So the 5th row (indicated by "`E`") would correspond with [y]-value **`4`**.
 */
data class CellRef(val x: Int, val y: Int) {
    val colRef: String = indexToColRef(x)
    val rowRef: String = indexToRowRef(y)
    val cellRef: String = rowRef + colRef

    /**
     * Construct a [CellRef] by a combination of:
     *  * digits (usually a single digit) for the row number of a [Cell],
     *  * letters (usually a single letter) for the column indicator of a [Cell].
     *
     * E.g. `C2` would indicate a [Cell] on the intercept of the 3rd row ([Row]) with the 2nd column ([Col])
     * @param cellRef The combination of letter(s) and digit(s) to indicate the cell position
     */
    constructor(cellRef: String) : this(getRowRefFromCellRef(cellRef), getColRefFromCellRef(cellRef))

    /**
     * Construct a [CellRef] by a combination of:
     *  * digits (usually a single digit) for the row number of a [Cell],
     *  * letters (usually a single letter) for the column indicator of a [Cell].
     *
     * E.g. (`C`, `2`) would indicate a [Cell] on the intercept of the 3rd row ([Row]) with the 2nd column ([Col])
     * @param rowRef the letter(s) to indicate the cell's row
     * @param colRef the digit(s) to indicate the cell's column
     */
    constructor(rowRef: String, colRef: String) : this(colRefToIndex(colRef.trim { it <= ' ' }.toUpperCase()),
            rowRefToIndex(rowRef.trim { it <= ' ' }.toUpperCase()))

    companion object CellRefCalculation {

        private const val maxBlockSizeStringLength = MAX_BLOCK_SIZE.toString().length
        private const val A: Char = 'A'
        private const val AInt: Int = 'A'.toInt()
        private const val rowGroupInCellPattern: Int = 1
        private const val colGroupInCellPattern: Int = 2

        private fun String.trimSpacesAndControlChars() = this.trim{ it <= ' '}

        private const val ROW_REF_PATTERN_STRING: String = "[A-Z]+"
        private val rowRefRegex = Regex("^$ROW_REF_PATTERN_STRING$")

        private const val COL_REF_PATTERN_STRING: String = "[1-9][0-9]*"
        private val colRefRegex = Regex("^$COL_REF_PATTERN_STRING$")

        private val cellRefRegex = Regex("^($ROW_REF_PATTERN_STRING)\\s*($COL_REF_PATTERN_STRING)$")

        private fun parseCellRef(cellRef: String): List<String> {
            val tidiedCellRef = cellRef.trim { it <= ' ' }.toUpperCase()
            val groupValues = cellRefRegex.find(tidiedCellRef)?.groupValues
            // if matching then groupValues != null. groupValues[0] then contains the whole matching String,
            // groupValues[1], [2], ... etc. contain the matches of the groups in the pattern
            requireAndLog(groupValues != null && groupValues[0] == tidiedCellRef, Level.WARN) {"""Invalid format, can not parse cell reference [$cellRef]. Format must be "A1", "J12", "AC23" etc."""}
            return groupValues!!
        }

        fun getRowRefFromCellRef(cellRef: String): String {
            return parseCellRef(cellRef)[rowGroupInCellPattern]
        }

        fun getColRefFromCellRef(cellRef: String): String {
            return parseCellRef(cellRef)[colGroupInCellPattern]
        }

        fun rowRefToIndex(rowRef: String): Int {
            val rowRefUpper = rowRef.trimSpacesAndControlChars().toUpperCase()
            requireAndLog(rowRefUpper.isNotBlank()) {"""Row reference must not be blank. Format must be "A", "J", "AC" etc."""}
            requireAndLog(rowRefRegex.matches(rowRefUpper)) {"""Invalid format, can not parse row reference [$rowRef]. Format must be "A", "J", "AC" etc."""}
            var value = 0
            for (x in 0 until rowRefUpper.length) {
                // +1 because the references have no zero A = 1, Z = 26, AA = 27 (no A0!) -> no zeros!
                val charWeight = rowRefUpper[x] - A + 1
                value = value * 26 + charWeight
                // If we reach this value, we better break, we can not handle Sudoku's that that large
                // (would cause Int overflow)
                requireAndLog(value <= MAX_BLOCK_SIZE) {
                    "Too high value [$rowRef] for row reference! Block size asked for is [${MAX_BLOCK_SIZE+1}] or higher but must be between 1 and $MAX_BLOCK_SIZE"
                }
            }
            return value - 1 // Make it zero-base
        }

        fun indexToRowRef(rowIndex: Int): String {
            requireAndLog(rowIndex >= 0) {"Negative row index [$rowIndex] is not allowed"}
            requireAndLog(rowIndex < MAX_BLOCK_SIZE) {"Too high value [$rowIndex] for row index! Block size asked for is higher than $MAX_BLOCK_SIZE"}
            val sb = StringBuilder()
            var num = rowIndex
            while (num >= 0) {
                val asciiNum = num % 26 + AInt
                sb.append(asciiNum.toChar())
                // -1 because the row references have no zero: A = 1, Z = 26, AA = 27 (no A0!) -> no zeros!
                num = num / 26 - 1
            }
            return sb.reverse().toString()
        }

        fun colRefToIndex(colRef: String): Int {
            // trim all spaces and control characters, and make uppercase
            val colRefUpper = colRef.trimSpacesAndControlChars().toUpperCase()
            requireAndLog(colRefUpper.isNotBlank()) {"""Column reference must not be blank. Format must be "A", "J", "AC" etc."""}
            requireAndLog(colRefRegex.matches(colRefUpper)) {"""Invalid format, can not parse column reference [$colRef]. Format must be "1", "8", "11" etc."""}
            requireAndLog(colRefUpper.length <= maxBlockSizeStringLength && colRefUpper.toInt() <= MAX_BLOCK_SIZE) {
                "Too high value [$colRef] for column reference! Block size asked for is [${MAX_BLOCK_SIZE+1}] or higher but must be between 1 and $MAX_BLOCK_SIZE"
            }
            return colRefUpper.toInt() - 1 // -1 to make it zero-based
        }

        fun indexToColRef(colIndex: Int): String {
            requireAndLog(colIndex >= 0) {"Negative column index [$colIndex] is not allowed"}
            requireAndLog(colIndex < MAX_BLOCK_SIZE) {
                "Too high value [$colIndex] for column index! Block size asked for is higher than $MAX_BLOCK_SIZE"
            }
            return (colIndex + 1).toString() // +1 to make it one-based
        }
    }

    private val theHashCode = (31 + x + y) * 31

    /** [hashCode] based on [x] and [y] only  */
    override fun hashCode(): Int = theHashCode

    /** [equals] based on [x] and [y] only */
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null) {
            return false
        }
        if (javaClass != other.javaClass) {
            return false
        }
        val cellRef = other as CellRef
        if (x != cellRef.x) {
            return false
        }
        return (y == cellRef.y)
    }

    override fun toString(): String {
        return "${this.javaClass.simpleName} (x=$x, y=$y, rowRef='$rowRef', colRef='$colRef', cellRef='$cellRef')"
    }

}
