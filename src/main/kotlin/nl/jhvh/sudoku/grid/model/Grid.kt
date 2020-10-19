package nl.jhvh.sudoku.grid.model

/* Copyright 2020 Jan-Hendrik van Heusden

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

import nl.jhvh.sudoku.base.CELL_MIN_VALUE
import nl.jhvh.sudoku.base.DEFAULT_BLOCK_SIZE
import nl.jhvh.sudoku.base.gridSize
import nl.jhvh.sudoku.base.maxValue
import nl.jhvh.sudoku.base.validateBlockSize
import nl.jhvh.sudoku.format.Formattable
import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.defaultGridFormatter
import nl.jhvh.sudoku.grid.gridWithCandidatesBoxFormatter
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
private constructor (val blockSize: Int = 3, val fixedValues: Map<CellRef, Int>, val gridSolver: GridSolver):
        Formattable, GridStructural,
        SegmentValueEventHandlable by SegmentValueEventHandler(),
        ValueEventHandlable by gridSolver,
        GridSolvable by gridSolver {

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
        val formatter = if (isSolving) gridWithCandidatesBoxFormatter else defaultGridFormatter
        return toStringCompact() + "\ngrid = \n" + format(formatter)
    }

    fun toStringCompact(): String {
        return "${this.javaClass.simpleName}: (blockSize=$blockSize, gridSize=$gridSize), isSolving=$isSolving, isSolved=$isSolved, unSolvable=$unSolvable." +
                " ${this.javaClass.simpleName} id = ${System.identityHashCode(this)}"
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
