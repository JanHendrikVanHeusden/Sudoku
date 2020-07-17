package nl.jhvh.sudoku.grid.model.segment

import nl.jhvh.sudoku.base.incrementFromZero
import nl.jhvh.sudoku.format.Formattable
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.event.cellvalue.CellSetValueEvent
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.cell.indexToColRef

/**
 * A [Col] (column) represents a collection of [Cell]s within a Sudoku that are at the same horizontal axis (see [.colIndex]).
 *
 * When solved, the [Cell]s within the [Col] must contain all defined values of the Sudoku.
 */
class Col(grid: Grid, val colIndex: Int) : GridSegment(grid), Formattable {

    override fun onEvent(gridEvent: CellSetValueEvent) {
        TODO("Not yet implemented")
    }

    val colRef: String = indexToColRef(colIndex)

    override val cellList: List<Cell> = incrementFromZero(grid.gridSize) .map { grid.findCell(x = colIndex, y = it) }

    /** Technical [toString] value; for a functional representation, see [.format]  */
    override fun toString(): String = "${this.javaClass.simpleName} [colIndex=$colIndex] [colRef=$colRef]"

    override fun format(formatter: SudokuFormatter): List<String> = formatter.format(this)

}
