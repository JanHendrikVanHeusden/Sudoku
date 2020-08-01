package nl.jhvh.sudoku.grid.model.cell

import nl.jhvh.sudoku.format.Formattable
import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.event.GridEventListener
import nl.jhvh.sudoku.grid.event.cellvalue.CellSetValueEvent
import nl.jhvh.sudoku.grid.event.cellvalue.CellSetValueSource
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.GridElement
import nl.jhvh.sudoku.grid.model.cell.CellValue.FixedValue
import nl.jhvh.sudoku.grid.model.cell.CellValue.NonFixedValue
import java.util.Collections.synchronizedSet

/**
 * A [Cell] in a Sudoku represents a single square in a [Grid]
 *  * The [Cell] can either be empty, or filled with a single numeric value; see also [CellValue]
 *  * The [Cell] is aware of it's position within the [Grid], indicated by [colIndex] (X-axis) and [rowIndex] (Y-axis)
 */
class Cell(grid: Grid, val colIndex: Int, val rowIndex: Int, val fixedValue: Int? = null): GridElement(grid), CellSetValueSource, Formattable {

    /** Thread safe [MutableSet] of possible candidate values for the [Cell.cellValue]  */
    val valueCandidates: MutableSet<Int> = synchronizedSet(mutableSetOf())

    /** @see [GridEventListener] */
    override val eventListeners: MutableSet<GridEventListener<Cell, CellSetValueEvent>> = mutableSetOf()

    var cellValue: CellValue = if (fixedValue == null) NonFixedValue(this) else FixedValue(this, fixedValue)
        @Synchronized
        get
        @Synchronized
        private set

    /** Technical [toString] method; for a functional representation, see [format]  */
    override fun toString(): String = "${this.javaClass.simpleName}: colIndex=$colIndex, rowIndex=$rowIndex, cellValue=[$cellValue], valueCandidates=$valueCandidates"

    override fun format(formatter: SudokuFormatter): FormattableList = formatter.format(this)
}


