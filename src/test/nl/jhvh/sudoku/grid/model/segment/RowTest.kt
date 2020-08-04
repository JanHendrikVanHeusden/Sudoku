package nl.jhvh.sudoku.grid.model.segment

import io.mockk.CapturingSlot
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkObject
import io.mockk.verify
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.cell.CellRef.CellRefCalculation
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class RowTest: AbstractGridSegmentTest() {

    override lateinit var gridMock: Grid // initialized in setUp() of AbstractGridSegmentTest
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
                Assertions.assertThat(rowRef).isEqualTo(('A' + rowIndex).toString())
                verify(exactly = 1) { CellRefCalculation.indexToRowRef(rowIndex) }
                confirmVerified(CellRefCalculation)
            } finally {
                unmockkObject(CellRefCalculation)
            }
    }

    @Test
    fun getCellList() {
        for (rowIndex in 0 until gridSize) {
            val subject = Row(gridMock, rowIndex)
            val cellList = subject.cellList
            Assertions.assertThat(cellList).hasSize(gridSize)
            cellList.forEachIndexed { index, cell ->
                Assertions.assertThat(cellList[index].rowIndex).isEqualTo(rowIndex)
                Assertions.assertThat(cellList[index].colIndex).isEqualTo(index)
            }
        }
    }

    @Test
    fun testToString() {
        val rowIndex = Random.nextInt(0, gridSize)
        val subject = Row(gridMock, rowIndex)
        Assertions.assertThat(subject.toString()).contains("${Row::class.simpleName}: ", "[rowIndex=$rowIndex]", "[rowRef=${CellRefCalculation.indexToRowRef(rowIndex)}]")
    }

}
