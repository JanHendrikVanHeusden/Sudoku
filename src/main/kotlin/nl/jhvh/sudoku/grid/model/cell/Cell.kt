package nl.jhvh.sudoku.grid.model.cell

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

    val isSet: Boolean = cellValue.isSet

    /** Technical [toString] method; for a functional representation, see [format]  */
    override fun toString(): String = "${this.javaClass.simpleName}: colIndex=$colIndex, rowIndex=$rowIndex, cellValue=[$cellValue]"

    override fun format(formatter: SudokuFormatter): FormattableList = formatter.format(this)
}

