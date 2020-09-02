package nl.jhvh.sudoku.grid

import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.cell.Cell
import org.junit.jupiter.api.BeforeEach

/**
 * Base class for tests that require a [Grid] mock populated with [Cell] mocks.
 *  * NB: it does NOT provide [Row]s, [Col]s or [Block]s !!
 *  * The [gridMock] is newly constructed before each test, see [gridSetUp]
 *  * The [Cell]s can be retrieved by one of these methods:
 *     * [Grid.cellList].
 *       This always returns the same cells, so repeatedly calling [cellist]`[3]` always returns the same [Cell] mock.
 *     * [Grid.findCell]`(x, y)`.
 *       Every call of [Grid.findCell]`(x, y)` constructs a new [Cell], even with the same input params.
 */
abstract class GridTestBase {

    protected abstract var gridMock: Grid
    protected abstract val blockSize: Int
    protected abstract val gridSize: Int

    @BeforeEach
    fun gridSetUp() {
        gridMock = mockk()
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
