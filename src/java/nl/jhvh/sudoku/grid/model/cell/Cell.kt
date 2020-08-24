package nl.jhvh.sudoku.grid.model.cell

import nl.jhvh.sudoku.base.CELL_MIN_VALUE
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
 *  * On construction, the [Cell] can either be empty, or filled with a single numeric value; see also [CellValue]
 *  * The [Cell] is aware of it's position within the [Grid], indicated by [colIndex] (X-axis) and [rowIndex] (Y-axis)
 */
class Cell(grid: Grid, val colIndex: Int, val rowIndex: Int, val fixedValue: Int? = null): GridElement(grid), CellSetValueSource, Formattable {

    // lazy so no unnecessary initialization in case it's a fixed values
    private val valueCandidatesInit: MutableSet<Int> by lazy { synchronizedSet(IntRange(start = CELL_MIN_VALUE, endInclusive = grid.maxValue).toSet()) }

    val isFixed: Boolean = fixedValue != null

    /** @see [GridEventListener] */
    override val eventListeners: MutableSet<GridEventListener<Cell, CellSetValueEvent>> = mutableSetOf()

    val cellValue: CellValue = if (isFixed) FixedValue(this, fixedValue!!) else NonFixedValue(this)

    /** Thread safe [MutableSet] of possible candidate values for the [Cell.cellValue]  */
    val valueCandidates: MutableSet<Int> = if (isFixed) mutableSetOf() else synchronizedSet(HashSet(valueCandidatesInit))

    /** Technical [toString] method; for a functional representation, see [format]  */
    override fun toString(): String = "${this.javaClass.simpleName}: colIndex=$colIndex, rowIndex=$rowIndex, cellValue=[$cellValue], valueCandidates=$valueCandidates"

    override fun format(formatter: SudokuFormatter): FormattableList = formatter.format(this)
}


