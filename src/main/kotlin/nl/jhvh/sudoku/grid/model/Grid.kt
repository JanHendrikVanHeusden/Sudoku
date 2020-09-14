package nl.jhvh.sudoku.grid.model

import nl.jhvh.sudoku.base.CELL_MIN_VALUE
import nl.jhvh.sudoku.base.DEFAULT_BLOCK_SIZE
import nl.jhvh.sudoku.base.gridSize
import nl.jhvh.sudoku.base.maxValue
import nl.jhvh.sudoku.base.validateBlockSize
import nl.jhvh.sudoku.format.Formattable
import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.defaultGridToStringFormatter
import nl.jhvh.sudoku.grid.model.Grid.GridBuilder
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.cell.CellRef
import nl.jhvh.sudoku.grid.model.segment.Block
import nl.jhvh.sudoku.grid.model.segment.Col
import nl.jhvh.sudoku.grid.model.segment.Row
import nl.jhvh.sudoku.grid.solve.GridSolvable
import nl.jhvh.sudoku.grid.solve.GridSolver
import nl.jhvh.sudoku.grid.solve.SegmentValueEventHandlable
import nl.jhvh.sudoku.grid.solve.SegmentValueEventHandler
import nl.jhvh.sudoku.grid.solve.ValueEventHandlable
import nl.jhvh.sudoku.util.incrementFromZero
import nl.jhvh.sudoku.util.log
import java.util.Collections.unmodifiableMap

/**
 * Class to represent a Sudoku grid.
 *  * Grids of different sizes can be represented, indicated by their [blockSize]
 *  * Square ones only ([Grid]s of `4*4` ([blockSize] = 2), `9*9` ([blockSize] = 3), `16*16` ([blockSize] = 4), etc.,
 *    but not `4*6`, `9*16` etc.)
 *  @constructor Throws [IllegalArgumentException] on invalid [blockSize], e.g. negative or too high
 */
class Grid

@Throws(IllegalArgumentException::class)
private constructor (val blockSize: Int = 3, val fixedValues: Map<CellRef, Int>, val gridSolver: GridSolver) :
        Formattable, SegmentValueEventHandlable by SegmentValueEventHandler(), ValueEventHandlable by gridSolver, GridSolvable by gridSolver {

    // early validation
    init {
        try {
            validateBlockSize(blockSize)
        } catch (e: IllegalArgumentException) {
            log().warn {e.message}
            throw e
        }
    }

    /** The length of each side = [blockSize] * [blockSize]  */
    val gridSize: Int = gridSize(blockSize)
    /** Maximum value to be entered in a cell = [blockSize] * [blockSize]  */
    val maxValue: Int = maxValue(blockSize)

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

    // late initialization: after all properties have been set
    init {
        gridSolver.gridToSolve = this
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
        try {
            validateCellCoordinates(colIndex, rowIndex, gridSize)
        } catch (e: IllegalArgumentException) {
            log().warn {e.message}
            throw e
        }
        return cellList[colIndex + rowIndex * gridSize]
    }

    override val maxValueLength: Int = this.maxValue.toString().length

    fun toStringFull(): String {
        return toStringCompact() + "\ngrid = \n" + format(defaultGridToStringFormatter)
    }

    fun toStringCompact(): String {
        return "${this.javaClass.simpleName}: (blockSize=$blockSize, gridSize=$gridSize). ${this.javaClass.simpleName}" +
                " id = ${System.identityHashCode(this)}"
    }

    /** Technical [toString] method; for a functional representation, see [format]  */
    override fun toString(): String {
        return if (gridSize <= 16) toStringFull() else toStringCompact()
    }

    override fun format(formatter: SudokuFormatter): FormattableList = formatter.format(this)

    /**
     * Construct a [Grid] and allow the caller to fix cells etc., than have the constructed [Grid] returned by the [build] method.
     *  * Stateful. The [GridBuilder] instance holds a reference to the [Grid] being built, until [build] is called.
     *  * No reuse. Trying to use the same [GridBuilder] instance twice results in [IllegalStateException]
     *  * Not thread safe (should not be used across threads anyway)
     */
    class GridBuilder(val blockSize: Int = DEFAULT_BLOCK_SIZE, val gridSolver: GridSolver = GridSolver()) {

        init {
            validateBlockSize(blockSize)
        }

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
            val grid = Grid(blockSize, unmodifiableMap(fixedValueMap), gridSolver)
            isBuilt = true
            return grid
        }

        /**
         * Fix a [Cell] given by it's [CellRef.cellRef] to the given value
         * @param cellRef The [CellRef.cellRef] of the [Cell] whose value is to be fixed
         * @param value The value to fix the [Cell] to
         * @return "this", to allow fluent builder syntax
         * @throws IllegalArgumentException when the [cellRef] coordinates are outside the intended [Grid]
         */
        fun fix(cellRef: String, value: Int): GridBuilder {
            return fix(CellRef(cellRef), value)
        }

        /**
         * Fix a [Cell] given by it's [CellRef] to the given value
         * @param cellRef The [CellRef] of the [Cell] whose value is to be fixed
         * @param value The value to fix the [Cell] to
         * @return "this", to allow fluent builder syntax
         * @throws IllegalArgumentException when the [cellRef] coordinates are outside the intended [Grid]
         */
        fun fix(cellRef: CellRef, value: Int): GridBuilder {
            check(!isBuilt, { "Can not fix a cell when the ${Grid::class.simpleName} was built already" })
            try {
                validateCellCoordinates(colIndex = cellRef.x, rowIndex = cellRef.y, gridSize(this.blockSize))
                validateValueRange(value, maxValue(this.blockSize))
            } catch (e: IllegalArgumentException) {
                log().warn {e.message}
                throw e
            }

            val oldValue = fixedValueMap[cellRef]
            if (oldValue != null && value != oldValue) {
                log().warn { "About to override fixed value for $cellRef: old value = $oldValue, new value =$value" }
            }
            fixedValueMap[cellRef] = value
            return this
        }
    }

}

@Throws(IllegalArgumentException::class)
private fun validateCellCoordinates(colIndex: Int, rowIndex: Int, gridSize: Int) {
    require(colIndex in 0 until gridSize && rowIndex in 0 until gridSize) {
        "The indicated ${Cell::class.simpleName} coordinates are outside the ${Grid::class.simpleName}" +
                " (gridSize = ${gridSize}: cellRef = ${CellRef(x = colIndex, y = rowIndex)}, colIndex=$colIndex, rowIndex=$rowIndex)"
    }
}

/**
 * Cell values are numbers from [CELL_MIN_VALUE] = 1 up to and including [Grid.maxValue]
 * @param value The value to validate
 * @throws IllegalArgumentException If the given value is not in the proper range for the [Grid].
 */
@Throws(IllegalArgumentException::class)
fun validateValueRange(value: Int, maxValue: Int) {
    require(value >= CELL_MIN_VALUE) { "A cell value must be $CELL_MIN_VALUE or higher but is $value" }
    require(value <= maxValue) { "A cell value must be at most $maxValue but is $value" }
}


fun main() {
    val gridBuilder = GridBuilder()
    // build an easy to solve grid
    val grid = gridBuilder
            .fix("A1", 5)
            .fix("A2", 8)
            .fix("A3", 4)
            .fix("A4", 7)
            .fix("A5", 2)
            .fix("A6", 1)
            .fix("A7", 3)
            .fix("A8", 6)
            .fix("A9", 9)
            .fix("B1", 2)
            .fix("B2", 3)
            .fix("B5", 6)
            .fix("B6", 4)
            .fix("C3", 9)
            .fix("C5", 3)
            .fix("C6", 8)
            .fix("C7", 4)
            .fix("C8", 1)
            .fix("D1", 8)
            .fix("D2", 9)
            .fix("D3", 6)
            .fix("D5", 1)
            .fix("D6", 7)
            .fix("D8", 4)
            .fix("E3", 5)
            .fix("E5", 4)
            .fix("E6", 9)
            .fix("E8", 8)
            .fix("E9", 7)
            .fix("F1", 7)
            .fix("F2", 4)
            .fix("F3", 3)
            .fix("F5", 5)
            .fix("F6", 6)
            .fix("F8", 9)
            .fix("G3", 8)
            .fix("G5", 7)
            .fix("G6", 3)
            .fix("G7", 1)
            .fix("G8", 2)
            .fix("H1", 4)
            .fix("H2", 6)
            .fix("H5", 9)
            .fix("H6", 2)
            .fix("I1", 3)
            .fix("I2", 1)
            .fix("I3", 2)
            .fix("I4", 6)
            .fix("I5", 8)
            .fix("I6", 5)
            .fix("I7", 9)
            .fix("I8", 7)
            .fix("I9", 4)
            .build()

    // print the formatted grid
    println(grid)
    grid.solveGrid()
    println(grid)

}