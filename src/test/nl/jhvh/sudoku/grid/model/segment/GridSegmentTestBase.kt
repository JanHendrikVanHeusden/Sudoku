package nl.jhvh.sudoku.grid.model.segment

import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.cell.Cell
import org.junit.jupiter.api.BeforeEach

abstract class GridSegmentTestBase {

    protected abstract var gridMock: Grid
    protected abstract val blockSize: Int
    protected abstract val gridSize: Int

    @BeforeEach
    fun gridSetUp() {
        gridMock = mockk()
        every {gridMock.blockSize} returns blockSize
        every {gridMock.gridSize} returns gridSize
        every {gridMock.maxValue} returns gridSize

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
        for (x in 0..gridSize-1) {
            for (y in 0.. gridSize-1) {
                gridCells.add(gridMock.findCell(x, y))
            }
        }
        every {gridMock.cellList} returns gridCells
    }

}
