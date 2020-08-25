package nl.jhvh.sudoku.grid.model.segment

import nl.jhvh.sudoku.base.incrementFromZero
import nl.jhvh.sudoku.format.Formattable
import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.cell.Cell

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
class Block(grid: Grid, val leftColIndex: Int, val topRowIndex: Int) : GridSegment(grid), Formattable {
    /** The right (x-axis) coordinate of the [Block] within the [Grid]  */
    val rightColIndex: Int = leftColIndex + grid.blockSize - 1
    /** The bottom (y-axis) coordinate of the [Block] within the [Grid]  */
    val bottomRowIndex: Int = topRowIndex + grid.blockSize - 1

    override val cells: LinkedHashSet<Cell> = LinkedHashSet(incrementFromZero(grid.gridSize)
            .map { grid.findCell(colIndex = it % grid.blockSize + leftColIndex, rowIndex = it/grid.blockSize + topRowIndex) }
    )

    /** Technical [toString] method; for a functional representation, see [format]  */
    override fun toString(): String = "${this.javaClass.simpleName}: [leftColIndex=$leftColIndex], [rightColIndex=$rightColIndex], [upperRowIndex=$topRowIndex], [bottomRowIndex=$bottomRowIndex]"

    override fun format(formatter: SudokuFormatter): FormattableList = formatter.format(this)

    init {
        subscribeToSetValueEvents()
    }

}
