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
import nl.jhvh.sudoku.grid.model.cell.CellRef.CellRefCalculation.indexToRowRef
import nl.jhvh.sudoku.util.incrementFromZero

/**
 * A [Row]represents a collection of [Cell]s within a Sudoku that are at the same vertical axis (see [rowIndex]).
 *
 * When solved, the [Cell]s within the [Row] must contain all defined values of the Sudoku.
 */
class Row(grid: Grid, val rowIndex: Int) : LinearSegment(grid), Formattable {

    val rowRef: String = indexToRowRef(rowIndex)

    override val cells: Set<Cell> = LinkedHashSet(incrementFromZero(grid.gridSize)
            .map { grid.findCell(colIndex = it, rowIndex = rowIndex) })

    /** Technical [toString] value; for a functional representation, see [format]  */
    override fun toString(): String = "${this.javaClass.simpleName}: [rowIndex=$rowIndex] [rowRef=$rowRef]"

    override fun format(formatter: SudokuFormatter): FormattableList = formatter.format(this)

}
