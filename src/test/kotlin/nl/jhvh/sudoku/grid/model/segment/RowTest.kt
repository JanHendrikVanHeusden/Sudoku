package nl.jhvh.sudoku.grid.model.segment

import io.mockk.CapturingSlot
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkObject
import io.mockk.verify
import nl.jhvh.sudoku.grid.GridTestBase
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.cell.CellRef.CellRefCalculation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class RowTest: GridTestBase() {

    /** grid mock and cell mocks initialized in [GridTestBase.gridSetUp] */
    override lateinit var gridMock: Grid
    override val blockSize = 3
    override val gridSize = 9

    @Test
    fun getRowRef() {
        val rowIndexCapturer: CapturingSlot<Int> = slot()
        // given
        for (rowIndex in 0 until gridSize)
            try {
                mockkObject(CellRefCalculation)
                every { CellRefCalculation.indexToRowRef(capture(rowIndexCapturer)) } answers { ('A' + rowIndexCapturer.captured).toString() }
                val subject = Row(gridMock, rowIndex)
                // when
                val rowRef = subject.rowRef
                // then
                assertThat(rowRef).isEqualTo(('A' + rowIndex).toString())
                verify(exactly = 1) { CellRefCalculation.indexToRowRef(rowIndex) }
                confirmVerified(CellRefCalculation)
            } finally {
                unmockkObject(CellRefCalculation)
            }
    }

    @Test
    fun getCells() {
        for (rowIndex in 0 until gridSize) {
            val subject = Row(gridMock, rowIndex)
            val cellSet = subject.cells
            assertThat(cellSet).hasSize(gridSize)
            cellSet.forEachIndexed { index, cell ->
                assertThat(cell.rowIndex).isEqualTo(rowIndex)
                assertThat(cell.colIndex).isEqualTo(index)
            }
        }
    }

    @Test
    fun testToString() {
        val rowIndex = Random.nextInt(0, gridSize)
        val subject = Row(gridMock, rowIndex)
        assertThat(subject.toString()).contains("${Row::class.simpleName}: ", "[rowIndex=$rowIndex]", "[rowRef=${CellRefCalculation.indexToRowRef(rowIndex)}]")
    }

}
