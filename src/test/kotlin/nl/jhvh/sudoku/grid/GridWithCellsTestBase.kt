package nl.jhvh.sudoku.grid

import io.mockk.CapturingSlot
import io.mockk.clearAllMocks
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.excludeRecords
import io.mockk.mockk
import io.mockk.slot
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.cell.CellValue
import nl.jhvh.sudoku.grid.model.cell.CellValue.NonFixedValue
import nl.jhvh.sudoku.grid.model.cell.VALUE_UNKNOWN
import org.junit.jupiter.api.BeforeEach

/**
 * Base class for tests that require a [Grid] mock populated with [Cell] and [CellValue] mocks.
 *  * It does NOT provide [Row]s, [Col]s or [Block]s !!
 *     * If [Row]s, [Col]s or [Block]s are needed, see [GridWithCellsAndSegmentsTestBase]
 *  * The [gridMock] and all related mocks are newly constructed before each test, see [gridSetUp]
 *  * All [CellValue]s are mocked [NonFixedValue]s.
 *    Individual tests or subclasses can replace these by [FixedValue]s if needed.
 *  * The [Cell]s can be retrieved by one of these methods:
 *     * [Grid.cellList].
 *       This always returns the same cells, so repeatedly calling [cellist]`[3]` always returns the same [Cell] mock.
 *     * [Grid.findCell]`(x, y)`.
 *       Every call of [Grid.findCell]`(x, y)` constructs a new [Cell], even with the same input params.
 */
internal abstract class GridWithCellsTestBase(protected val blockSize: Int) {

    protected lateinit var gridMock: Grid
    protected val gridSize = blockSize * blockSize

    @BeforeEach
    fun gridSetUp() {
        gridMock = mockk(relaxed = true)
        every { gridMock.blockSize } returns blockSize
        every { gridMock.gridSize } returns gridSize
        every { gridMock.maxValue } returns gridSize

        val cellColIndexCapturer: CapturingSlot<Int> = slot()
        val cellRowIndexCapturer: CapturingSlot<Int> = slot()
        every {gridMock.findCell(capture(cellColIndexCapturer), capture(cellRowIndexCapturer))} answers {
            val cellMock: Cell = mockk(relaxed = true)
            every { cellMock.grid } returns gridMock
            every { cellMock.colIndex } returns cellColIndexCapturer.captured
            every { cellMock.rowIndex } returns cellRowIndexCapturer.captured

            val nonFixedValueMock: NonFixedValue = mockk(relaxed = true)
            every { cellMock.cellValue } returns nonFixedValueMock
            every { cellMock.isFixed } returns false
            every { nonFixedValueMock.isFixed } returns false
            every { nonFixedValueMock.cell.isFixed } returns false

            every { nonFixedValueMock.cell } returns cellMock
            every { nonFixedValueMock.value } returns VALUE_UNKNOWN
            every { nonFixedValueMock.isSet } returns false
            every { nonFixedValueMock.toString() } returns "NonFixedValue mock;" +
                    " rowIndex=${nonFixedValueMock.cell.rowIndex}, colIndex=${nonFixedValueMock.cell.colIndex}, value=${nonFixedValueMock.value}"

            excludeRecords { nonFixedValueMock.cell }
            excludeRecords { nonFixedValueMock.value }
            excludeRecords { nonFixedValueMock.isFixed }
            excludeRecords { cellMock.isFixed }
            excludeRecords { cellMock.cellValue.isFixed }

            cellMock
        }

        val gridCells = mutableListOf<Cell>()
        for (x in 0 until gridSize) {
            for (y in 0 until gridSize) {
                val cell = gridMock.findCell(x, y)
                every { cell.toString() } returns "Cell mock; rowIndex=${y}, colIndex=${x}, value=${cell.cellValue.value}"
                gridCells.add(cell)
            }
        }
        every {gridMock.cellList} returns gridCells
    }

    /** Used by [clearAllGridMocks] */
    protected open val allGridMocks: Array<Any> by lazy {
        val mockkList = mutableListOf<Any>()
        mockkList.add(gridMock)
        mockkList.addAll(gridMock.cellList)
        mockkList.addAll(gridMock.cellList.map { it.cellValue })
        mockkList.toTypedArray()
    }

    /**
     * Clears [gridMock] and all mocks it consists of (cells, cellValues, rows, cols, blocks, etc., insofar specified).
     * This is used for cases where [clearAllMocks] can not be used (you can't specify `exclusionRules` there)
     *  * To specify different mocks from your subclass, override [allGridMocks] to specify your test class's mocks.
     *  * Cleared are:
     *     * All recorded calls, including those of childMocks
     *     * All verification marks, including those of childMocks
     *  * NOT cleared are:
     *     * answers / returns
     *     * exclusion rules
     */
    protected fun clearAllGridMocks() {
        clearMocks(
                firstMock = gridMock,
                mocks = allGridMocks,
                answers = false,
                recordedCalls = true,
                childMocks = true,
                verificationMarks = true,
                exclusionRules = false
        )
    }

}
