package nl.jhvh.sudoku.grid.model

import nl.jhvh.sudoku.base.DEFAULT_BLOCK_SIZE
import nl.jhvh.sudoku.base.incrementFromZero
import nl.jhvh.sudoku.format.Formattable
import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.cell.CellRef
import nl.jhvh.sudoku.grid.model.segment.Block
import nl.jhvh.sudoku.grid.model.segment.Col
import nl.jhvh.sudoku.grid.model.segment.Row

/**
 * Class to represent a Sudoku grid.
 *  * Grids of different sizes can be represented, indicated by their [blockSize]
 *  * Square ones only ([Grid]s of `4*4` ([blockSize] = 2), `9*9` ([blockSize] = 3), `16*16` ([blockSize] = 4), etc.,
 *    but not `4*6`, `9*16` etc.)
 */
class Grid protected constructor (val blockSize: Int = 3) : Formattable {

    /** The length of each side = [blockSize] * [blockSize]  */
    val gridSize: Int = blockSize * blockSize
    /** Maximum value to be entered in a cell = [blockSize] * [blockSize]  */
    val maxValue: Int = blockSize * blockSize

    val cellList: List<Cell> = incrementFromZero(gridSize * gridSize)
            .map { Cell(this, colIndex = it % gridSize, rowIndex = it / gridSize) }

    val rowList: List<Row> = incrementFromZero(gridSize).map { Row(this, rowIndex = it) }
    val colList: List<Col> = incrementFromZero(gridSize).map { Col(this, colIndex = it) }
    val blockList: List<Block> = incrementFromZero(gridSize)
            .map { Block(this, leftColIndex = ((it * blockSize) % gridSize), topRowIndex = (it / blockSize) * blockSize) }

    fun findCell(cellRef: String): Cell {
        return with(CellRef(cellRef)) { findCell(x, y) }
    }

    fun findCell(cellRef: CellRef): Cell {
        return findCell(cellRef.x, cellRef.y)
    }

    fun findCell(x: Int, y: Int): Cell {
        return cellList[x + y * gridSize]
    }

    fun fixCell(cell: Cell, value: Int) {
        cell.fixValue(value)
    }

    override val maxValueLength: Int by lazy { this.maxValue.toString().length }

    /** Technical [toString] method; for a functional representation, see [format]  */
    override fun toString(): String {
        return "${this.javaClass.simpleName}: (blockSize=$blockSize, gridSize=$gridSize)"
    }

    override fun format(formatter: SudokuFormatter): FormattableList = formatter.format(this)

    /**
     * Construct a [Grid] and allow the caller to fix cells etc., than have the constructed [Grid] returned by the [build] method.
     *  * Stateful. The [GridBuilder] instance holds a reference to the [Grid] being built, until [build] is called.
     *  * No reuse. Trying to use the same [GridBuilder] instance twice results in [IllegalStateException]
     *  * Not thread safe (should not be used across threads anyway)
     */
    class GridBuilder(val blockSize: Int = DEFAULT_BLOCK_SIZE) {

        /** The [Grid] to build  */
        private var gridInstance: Grid? = Grid(blockSize)

        /** @throws IllegalStateException when the [Grid] was built already */
        private fun grid(): Grid {
            check(!isBuilt) { "${this.javaClass.simpleName} can be used only once - " +
                    "create a new ${this.javaClass.simpleName} instance to build a new ${Grid::class.simpleName}!" }
            return gridInstance!!
        }

        /**
         * Flag to indicate whether the [Grid] was completed;
         *  * If so, no structural changes possible anymore (e.g. not possible to fix [Cell]s anymore)
         */
        private var isBuilt: Boolean = false

        /**
         * @return The [Grid] as specified by its blockSize and by it's fixed values
         * @throws IllegalStateException when the [Grid] was built already
         */
        @Throws(IllegalStateException::class)
        fun build(): Grid {
            val grid = grid()
            isBuilt = true
            gridInstance = null // to allow garbage collection when grid is not used anymore
            return grid
        }

        /**
         * Fix a [Cell] given by it's [CellRef.cellRef] to the given value
         * @param cellRef The [CellRef.cellRef] of the [Cell] whose value is to be fixed
         * @param value The value to fix the [Cell] to
         * @return "this", to allow fluent builder syntax
         * @throws IllegalStateException when the [Grid] was built already
         */
        fun fix(cellRef: String, value: Int): GridBuilder {
            return fix(CellRef(cellRef), value)
        }

        /**
         * Fix a [Cell] given by it's [CellRef] to the given value
         * @param cellRef The [CellRef] of the [Cell] whose value is to be fixed
         * @param value The value to fix the [Cell] to
         * @return "this", to allow fluent builder syntax
         * @throws IllegalStateException when the [Grid] was built already
         */
        fun fix(cellRef: CellRef, value: Int): GridBuilder {
            return fix(grid().findCell(cellRef), value)
        }

        /**
         * Fix a given [Cell] to the given value
         * @param cell The [Cell] whose value is to be fixed
         * @param value The value to fix the [Cell] to
         * @return "this", to allow fluent builder syntax
         * @throws IllegalStateException when the [Grid] was built already
         */
        @Throws(IllegalStateException::class)
        private fun fix(cell: Cell, value: Int): GridBuilder {
            check(!isBuilt) { "Can not fix a cell after the grid has been built!" }
            grid().fixCell(cell, value)
            return this
        }
    }

}
