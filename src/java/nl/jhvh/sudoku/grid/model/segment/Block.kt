package nl.jhvh.sudoku.grid.model.segment

import nl.jhvh.sudoku.base.incrementFromZero
import nl.jhvh.sudoku.format.Formattable
import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.event.cellvalue.CellSetValueEvent
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.cell.Cell

/**
 * A [Block] represents a collection of [Cell]s within a Sudoku that are form a square
 * with each side having the square root number of [Cell]s as the [Grid] the [Block].
 * <br></br>E.g. for a 9x9 grid, each [Block] is a 2x3 square of [Cell]s.
 *
 * When solved, the [Cell]s within the [Block] must contain all defined values of the Sudoku.
 *
 * Functional synonyms for [Block] are **Box** or **Region**.
 *
 * @constructor Construct a [Block], to be positioned at the given left horizontal
 * and the upper vertical coordinates within the [Grid].
 */
class Block(grid: Grid, val leftXIndex: Int, val topYIndex: Int) : GridSegment(grid), Formattable {
    /** The right (x-axis) coordinate of the [Block] within the [Grid]  */
    val rightXIndex: Int = leftXIndex + grid.blockSize - 1
    /** The bottom (y-axis) coordinate of the [Block] within the [Grid]  */
    val bottomYIndex: Int = topYIndex + grid.blockSize - 1

    override val cellList: List<Cell> = incrementFromZero(grid.gridSize)
            .map { grid.findCell(x = it % grid.blockSize + leftXIndex, y = it/grid.blockSize + topYIndex) }

    fun containsCell(cell: Cell): Boolean = grid === cell.grid && containsCellCoordinates(cell.colIndex, cell.rowIndex)

    fun containsCellCoordinates(x: Int, y: Int): Boolean = x in leftXIndex..rightXIndex && y in topYIndex..bottomYIndex

    override fun onEvent(gridEvent: CellSetValueEvent) {
        TODO("Not yet implemented")
    }

    /** Technical [toString] method; for a functional representation, see [format]  */
    override fun toString(): String = "Block(leftXIndex=$leftXIndex, rightXIndex=$rightXIndex, upperYIndex=$topYIndex, bottomYIndex=$bottomYIndex)"

    override fun format(formatter: SudokuFormatter): FormattableList = formatter.format(this)

}
