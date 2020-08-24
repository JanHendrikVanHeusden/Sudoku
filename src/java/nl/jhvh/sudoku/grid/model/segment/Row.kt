package nl.jhvh.sudoku.grid.model.segment

import nl.jhvh.sudoku.base.incrementFromZero
import nl.jhvh.sudoku.format.Formattable
import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.cell.CellRef.CellRefCalculation.indexToRowRef

/**
 * A [Row]represents a collection of [Cell]s within a Sudoku that are at the same vertical axis (see [rowIndex]).
 *
 * When solved, the [Cell]s within the [Row] must contain all defined values of the Sudoku.
 */
class Row(grid: Grid, val rowIndex: Int) : GridSegment(grid), Formattable {

    val rowRef: String = indexToRowRef(rowIndex)

    override val cells: LinkedHashSet<Cell> = LinkedHashSet(incrementFromZero(grid.gridSize).map { grid.findCell(colIndex = it, rowIndex = rowIndex) })

    /** Technical [toString] value; for a functional representation, see [format]  */
    override fun toString(): String = "${this.javaClass.simpleName}: [rowIndex=$rowIndex] [rowRef=$rowRef]"

    override fun format(formatter: SudokuFormatter): FormattableList = formatter.format(this)

    init {
        subscribeToSetValueEvents()
    }

}
