package nl.jhvh.sudoku.grid.model.segment

import io.mockk.*
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.cell.CellRef.CellRefCalculation
import nl.jhvh.sudoku.grid.model.cell.CellRef.CellRefCalculation.indexToColRef
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class ColTest: GridSegmentTestBase() {

    /** grid mock and cell mocks initialized in [GridSegmentTestBase.gridSetUp] */
    override lateinit var gridMock: Grid
    override val blockSize = 3
    override val gridSize = 9

    @Test
    fun getColRef() {
        val colIndexCapturer: CapturingSlot<Int> = slot()
        // given
        for (colIndex in 0 until gridSize)
        try {
            mockkObject(CellRefCalculation)
            every { CellRefCalculation.indexToColRef(capture(colIndexCapturer)) } answers { (colIndexCapturer.captured+1).toString() }
            val subject = Col(gridMock, colIndex)
            // when
            val colRef = subject.colRef
            // then
            assertThat(colRef).isEqualTo((colIndex+1).toString())
            verify(exactly = 1) { CellRefCalculation.indexToColRef(colIndex) }
            confirmVerified(CellRefCalculation)
        } finally {
            unmockkObject(CellRefCalculation)
        }
    }

    @Test
    fun getCells() {
        for (colIndex in 0 until gridSize) {
            val subject = Col(gridMock, colIndex)
            val cellList = subject.cells
            assertThat(cellList).hasSize(gridSize)
            cellList.forEachIndexed { index, cell ->
                assertThat(cellList.toList()[index].colIndex).isEqualTo(colIndex)
                assertThat(cellList.toList()[index].rowIndex).isEqualTo(index)
            }
        }
    }

    @Test
    fun testToString() {
        val colIndex = Random.nextInt(0, gridSize)
        val subject = Col(gridMock, colIndex)
        assertThat(subject.toString()).contains("${Col::class.simpleName}: ", "[colIndex=$colIndex]", "[colRef=${indexToColRef(colIndex)}]")
    }

}
