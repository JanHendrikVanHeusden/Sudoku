package nl.jhvh.sudoku.grid.model

import nl.jhvh.sudoku.base.DEFAULT_BASE_DIM
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.cell.CellRef

/**
 * Construct a [Grid] and allow the caller to fix cells etc., than have the constructed [Grid] returned by the [build] method.
 * TODO: verify the minimum number of fixed cells? See https://en.wikipedia.org/wiki/Mathematics_of_Sudoku#Minimum_number_of_givens
 */
class GridBuilder(blockSize: Int = DEFAULT_BASE_DIM) {

    /** The [Grid] to build  */
    private val grid: Grid = Grid(blockSize)

    /**
     * Flag to indicate whether the [Grid] was completed;
     *  * If so, no structural changes possible anymore (e.g. not possible to fix [Cell]s anymore)
     */
    private var isBuilt: Boolean = false

    /** @return The [Grid] as specified by size ([blockSize]) and by it's fixed values
     */
    fun build(): Grid {
        isBuilt = true
        return grid
    }

    /**
     * Fix a [Cell] given by it's [CellRef.cellRef] to the given value
     * @param cellRef The [CellRef.cellRef] of the [Cell] whose value is to be fixed
     * @param value The value to fix the [Cell] to
     * @return "this", to allow fluent builder syntax
     */
    fun fix(cellRef: String, value: Int): GridBuilder {
        return fix(CellRef(cellRef), value)
    }

    /**
     * Fix a [Cell] given by it's [CellRef] to the given value
     * @param cellRef The [CellRef] of the [Cell] whose value is to be fixed
     * @param value The value to fix the [Cell] to
     * @return "this", to allow fluent builder syntax
     */
    fun fix(cellRef: CellRef, value: Int): GridBuilder {
        return fix(grid.findCell(cellRef), value)
    }

    /**
     * Fix a given [Cell] to the given value
     * @param cell The [Cell] whose value is to be fixed
     * @param value The value to fix the [Cell] to
     * @return "this", to allow fluent builder syntax
     */
    private fun fix(cell: Cell, value: Int): GridBuilder {
        check(!isBuilt) { "Can not fix a cell after the grid has been built!" }
        grid.fixCell(cell, value)
        return this
    }


}

fun main(args: Array<String>) {

}
