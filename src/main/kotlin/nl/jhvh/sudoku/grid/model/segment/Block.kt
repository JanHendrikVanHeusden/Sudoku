package nl.jhvh.sudoku.grid.model.segment

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

import nl.jhvh.sudoku.format.Formattable
import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.util.incrementFromZero

/**
 * A [Block] represents a collection of [Cell]s within a Sudoku that form a square
 * with each side having the square root number of [Cell]s as the [Grid] the [Block].
 *  * E.g. for a 9x9 grid, each [Block] is a 3x3 square of [Cell]s.
 *  * Functional synonyms for [Block] are **Box** or **Region**.
 *
 * When solved, the [Cell]s within the [Block] must contain all defined values of the Sudoku.
 *
 * @constructor Construct a [Block], to be positioned at the given left horizontal
 * and the upper vertical coordinates within the [Grid].
 * @param grid
 * @param leftColIndex The left (x-axis) coordinate of the [Block] within the [Grid]
 * @param topRowIndex The top (y-axis) coordinate of the [Block] within the [Grid]
 */
class Block(grid: Grid, val leftColIndex: Int, val topRowIndex: Int) : SquareSegment(grid), Formattable {

    /** The right (x-axis) coordinate of the [Block] within the [Grid]  */
    val rightColIndex: Int = leftColIndex + grid.blockSize - 1
    /** The bottom (y-axis) coordinate of the [Block] within the [Grid]  */
    val bottomRowIndex: Int = topRowIndex + grid.blockSize - 1

    override val cells: Set<Cell> = LinkedHashSet(incrementFromZero(grid.gridSize)
            .map { grid.findCell(colIndex = it % grid.blockSize + leftColIndex, rowIndex = it/grid.blockSize + topRowIndex) }
    )

    /** Technical [toString] method; for a functional representation, see [format]  */
    override fun toString(): String = "${this.javaClass.simpleName}: [leftColIndex=$leftColIndex], [rightColIndex=$rightColIndex], [upperRowIndex=$topRowIndex], [bottomRowIndex=$bottomRowIndex]"

    override fun format(formatter: SudokuFormatter): FormattableList = formatter.format(this)

}
