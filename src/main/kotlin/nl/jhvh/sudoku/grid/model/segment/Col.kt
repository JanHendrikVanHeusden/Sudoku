package nl.jhvh.sudoku.grid.model.segment

import nl.jhvh.sudoku.format.Formattable
import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.cell.CellRef.CellRefCalculation.indexToColRef
import nl.jhvh.sudoku.util.incrementFromZero

/**
 * A [Col] (column) represents a collection of [Cell]s within a Sudoku that are at the same horizontal axis (see [colIndex]).
 *
 * When solved, the [Cell]s within the [Col] must contain all defined values of the Sudoku.
 */
class Col(grid: Grid, val colIndex: Int) : GridSegment(grid), Formattable {

    val colRef: String = indexToColRef(colIndex)

    override val cells: LinkedHashSet<Cell> = LinkedHashSet(incrementFromZero(grid.gridSize).map { grid.findCell(colIndex = colIndex, rowIndex = it) })

    /** Technical [toString] value; for a functional representation, see [format]  */
    override fun toString(): String = "${this.javaClass.simpleName}: [colIndex=$colIndex] [colRef=$colRef]"

    override fun format(formatter: SudokuFormatter): FormattableList = formatter.format(this)

    init {
        subscribeToSegmentSetValueEvents()
    }

}
