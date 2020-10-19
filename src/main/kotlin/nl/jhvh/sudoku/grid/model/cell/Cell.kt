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

import nl.jhvh.sudoku.format.Formattable
import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.GridElement
import nl.jhvh.sudoku.grid.model.cell.CellValue.FixedValue
import nl.jhvh.sudoku.grid.model.cell.CellValue.NonFixedValue

/**
 * A [Cell] in a Sudoku represents a single square in a [Grid]
 *  * On construction, the [Cell] can either be empty, or filled with a single numeric value; see also [CellValue]
 *  * The [Cell] is aware of it's position within the [Grid], indicated by [colIndex] (X-axis) and [rowIndex] (Y-axis)
 */
class Cell(grid: Grid, val colIndex: Int, val rowIndex: Int, val fixedValue: Int? = null): GridElement(grid), Formattable {

    val isFixed: Boolean = fixedValue != null

    val cellValue: CellValue = if (isFixed) FixedValue(this, fixedValue!!) else NonFixedValue(this)

    val isSet: Boolean
        get() = cellValue.isSet

    /** Technical [toString] method; for a functional representation, see [format]  */
    override fun toString(): String = "${this.javaClass.simpleName}: colIndex=$colIndex, rowIndex=$rowIndex, cellValue=[$cellValue]"

    override fun format(formatter: SudokuFormatter): FormattableList = formatter.format(this)
}

