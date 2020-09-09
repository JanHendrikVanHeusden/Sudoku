package nl.jhvh.sudoku.grid

import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.cell.CellValue
import nl.jhvh.sudoku.grid.model.cell.CellValue.NonFixedValue
import org.junit.jupiter.api.BeforeEach

/**
 * Base class for tests that require a [Grid] mock populated with [Cell] and [CellValue] mocks.
 *  * NB: it does NOT provide [Row]s, [Col]s or [Block]s !!
 *  * The [gridMock] is newly constructed before each test, see [gridSetUp]
 *  * All [CellValue]s are mocked [NonFixedValue]s
 *  * The [Cell]s can be retrieved by one of these methods:
 *     * [Grid.cellList].
 *       This always returns the same cells, so repeatedly calling [cellist]`[3]` always returns the same [Cell] mock.
 *     * [Grid.findCell]`(x, y)`.
 *       Every call of [Grid.findCell]`(x, y)` constructs a new [Cell], even with the same input params.
 */
abstract class GridWithCellsTestBase(protected val blockSize: Int) {

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
            every {nonFixedValueMock.cell} returns cellMock
            cellMock
        }

        val gridCells = mutableListOf<Cell>()
        for (x in 0 until gridSize) {
            for (y in 0 until gridSize) {
                gridCells.add(gridMock.findCell(x, y))
            }
        }
        every {gridMock.cellList} returns gridCells
    }

}
