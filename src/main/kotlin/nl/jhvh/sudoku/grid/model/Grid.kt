package nl.jhvh.sudoku.grid.model

import nl.jhvh.sudoku.base.DEFAULT_BLOCK_SIZE
import nl.jhvh.sudoku.base.incrementFromZero
import nl.jhvh.sudoku.format.Formattable
import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.defaultGridToStringFormatter
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.cell.CellRef
import nl.jhvh.sudoku.grid.model.segment.Block
import nl.jhvh.sudoku.grid.model.segment.Col
import nl.jhvh.sudoku.grid.model.segment.Row
import nl.jhvh.sudoku.grid.solve.GridSolvable
import nl.jhvh.sudoku.grid.solve.GridSolver
import nl.jhvh.sudoku.util.log
import java.util.Collections.unmodifiableMap

/**
 * Class to represent a Sudoku grid.
 *  * Grids of different sizes can be represented, indicated by their [blockSize]
 *  * Square ones only ([Grid]s of `4*4` ([blockSize] = 2), `9*9` ([blockSize] = 3), `16*16` ([blockSize] = 4), etc.,
 *    but not `4*6`, `9*16` etc.)
 */
class Grid private constructor (val blockSize: Int = 3, val fixedValues: Map<CellRef, Int>, val gridSolver: GridSolver = GridSolver())
    : Formattable, GridSolvable by gridSolver {

    /** The length of each side = [blockSize] * [blockSize]  */
    val gridSize: Int = blockSize * blockSize
    /** Maximum value to be entered in a cell = [blockSize] * [blockSize]  */

    val maxValue: Int = blockSize * blockSize

    private fun colCalc (cellListIndex: Int, gridSize: Int): Int = cellListIndex % gridSize
    private fun rowCalc (cellListIndex: Int, gridSize: Int): Int = cellListIndex / gridSize
    private fun cellRefCalc (cellListIndex: Int, gridSize: Int): CellRef =
            CellRef(colCalc(cellListIndex, gridSize), rowCalc(cellListIndex, gridSize))

    val cellList: List<Cell> = incrementFromZero(gridSize * gridSize)
            .map { Cell(this, colCalc(it, gridSize), rowCalc(it, gridSize), fixedValue = fixedValues[cellRefCalc(it, gridSize)]) }

    val rowList: List<Row> = incrementFromZero(gridSize).map { Row(this, rowIndex = it) }
    val colList: List<Col> = incrementFromZero(gridSize).map { Col(this, colIndex = it) }
    val blockList: List<Block> = incrementFromZero(gridSize)
            .map { Block(this, leftColIndex = ((it * blockSize) % gridSize), topRowIndex = (it / blockSize) * blockSize) }

    init {
        // For all fixed values, publish the set value event to remove the candidates that can be eliminated already
        this.cellList.filter { it.isFixed }.map { it.cellValue }.forEach { it.publish(SetCellValueEvent(it, it.value!!)) }
    }

    @Throws(IllegalArgumentException::class)
    fun findCell(cellRef: String): Cell {
        return with(CellRef(cellRef)) { findCell(x, y) }
    }

    @Throws(IllegalArgumentException::class)
    fun findCell(cellRef: CellRef): Cell {
        return findCell(cellRef.x, cellRef.y)
    }

    @Throws(IllegalArgumentException::class)
    fun findCell(colIndex: Int, rowIndex: Int): Cell {
        validateCellCoordinates(colIndex, rowIndex, gridSize)
        return cellList[colIndex + rowIndex * gridSize]
    }

    override val maxValueLength: Int = this.maxValue.toString().length

    /** Technical [toString] method; for a functional representation, see [format]  */
    override fun toString(): String {
        return "${this.javaClass.simpleName}: (blockSize=$blockSize, gridSize=$gridSize)" +
                // if not too big, we also add the formatted grid
                if (gridSize <= 16) ", grid = \n" + format(defaultGridToStringFormatter) else ""
    }

    override fun format(formatter: SudokuFormatter): FormattableList = formatter.format(this)

    /**
     * Construct a [Grid] and allow the caller to fix cells etc., than have the constructed [Grid] returned by the [build] method.
     *  * Stateful. The [GridBuilder] instance holds a reference to the [Grid] being built, until [build] is called.
     *  * No reuse. Trying to use the same [GridBuilder] instance twice results in [IllegalStateException]
     *  * Not thread safe (should not be used across threads anyway)
     */
    class GridBuilder(val blockSize: Int = DEFAULT_BLOCK_SIZE) {

        private val fixedValueMap: MutableMap<CellRef, Int> = mutableMapOf()

        /**
         * Flag to indicate whether the [Grid] was completed;
         *  * If so, no structural changes possible anymore (e.g. not possible to fix [Cell]s anymore)
         */
        var isBuilt: Boolean = false
            private set

        /**
         * @return The [Grid] as specified by its blockSize and by it's fixed values
         * @throws IllegalStateException when the [Grid] was built already
         */
        @Throws(IllegalStateException::class)
        fun build(): Grid {
            check(!isBuilt) { "${this.javaClass.simpleName} can be used only once - " +
                    "create a new ${this.javaClass.simpleName} instance to build a new ${Grid::class.simpleName}!" }
            val grid = Grid(blockSize, unmodifiableMap(fixedValueMap))
            isBuilt = true
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
            validateCellCoordinates(colIndex = cellRef.x, rowIndex = cellRef.y, gridSize = blockSize*blockSize)

            val oldValue = fixedValueMap[cellRef]
            if (oldValue != null && value != oldValue) {
                log().warn { "About to override fixed value for $cellRef: old value = $oldValue, new value =$value" }
            }
            fixedValueMap[cellRef] = value
            return this
        }
    }

}

private fun validateCellCoordinates(colIndex: Int, rowIndex: Int, gridSize: Int) {
    if (colIndex < 0 ||colIndex >= gridSize || rowIndex < 0 || rowIndex >= gridSize) {
        throw IllegalArgumentException("The indicated ${Cell::class.simpleName} coordinates are outside" +
                " the ${Grid::class.simpleName} (gridSize = ${gridSize}: cellRef = ${CellRef(x = colIndex, y = rowIndex)}," +
                " colIndex=$colIndex, rowIndex=$rowIndex")
    }
}
