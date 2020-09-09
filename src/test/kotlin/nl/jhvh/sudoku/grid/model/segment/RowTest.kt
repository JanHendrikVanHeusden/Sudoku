package nl.jhvh.sudoku.grid.model.segment

import io.mockk.CapturingSlot
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.spyk
import io.mockk.unmockkObject
import io.mockk.verify
import nl.jhvh.sudoku.format.Formattable
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.GridWithCellsTestBase
import nl.jhvh.sudoku.grid.model.cell.CellRef.CellRefCalculation
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.random.Random

/** grid mock and cell mocks initialized in [GridWithCellsTestBase.gridSetUp] */
internal class RowTest: GridWithCellsTestBase(blockSize = 3) {

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

    @Test
    fun format() {
        // given
        val spiedSubject = spyk(Row(gridMock, 4))
        val formatterMock: SudokuFormatter = mockk(relaxed = true)
        val expected = Formattable.FormattableList(listOf("formatted Row"))
        every { formatterMock.format(spiedSubject) } returns expected
        // when, then
        assertThat(spiedSubject.format(formatterMock)).isEqualTo(expected)
        verify (exactly = 1) { spiedSubject.format(formatterMock) }
        verify (exactly = 1) { formatterMock.format(spiedSubject) }
        // confirm that no further calls were made
        confirmVerified(spiedSubject, formatterMock)
    }

}
