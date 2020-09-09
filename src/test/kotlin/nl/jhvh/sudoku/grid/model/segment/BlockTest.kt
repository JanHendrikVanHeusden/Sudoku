package nl.jhvh.sudoku.grid.model.segment

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import nl.jhvh.sudoku.format.Formattable
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.GridWithCellsTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.random.Random

/** grid mock and cell mocks initialized in [GridWithCellsTestBase.gridSetUp] */
internal class BlockTest: GridWithCellsTestBase(blockSize = 3) {

    @Test
    fun getRightColIndex() {
        for (leftColIndex in listOf(0, 3, 6)) {
            assertThat(Block(gridMock, leftColIndex, Random.nextInt(0, gridSize)).rightColIndex)
                    .isEqualTo(leftColIndex + blockSize - 1)
        }
    }

    @Test
    fun getBottomRowIndex() {
        for (topRowIndex in listOf(0, 3, 6)) {
            assertThat(Block(gridMock, Random.nextInt(0, gridSize), topRowIndex).bottomRowIndex)
                    .isEqualTo(topRowIndex + blockSize - 1)
        }
    }

    @Test
    fun getCells() {
        for (leftColIndex in listOf(0, 3, 6)) {
            for (topRowIndex in listOf(0, 3, 6)) {
                val subject = Block(gridMock, leftColIndex, topRowIndex)
                subject.cells.forEachIndexed { index, cell ->
                    assertThat(index)
                            .`as`("index should be $index for: leftColIndex=$leftColIndex, topRowIndex=$topRowIndex, cell.colIndex=${cell.colIndex}, cell.rowIndex=${cell.rowIndex}")
                            .isEqualTo((cell.colIndex-leftColIndex)%blockSize + (cell.rowIndex-topRowIndex)*blockSize)
                }
            }
        }
    }

    @Test
    fun testToString() {
        val subject = Block(gridMock, 6, 3)
        assertThat(subject.toString()).contains("${Block::class.simpleName}:", "[leftColIndex=${subject.leftColIndex}]", "[rightColIndex=${subject.rightColIndex}]", "[upperRowIndex=${subject.topRowIndex}], [bottomRowIndex=${subject.bottomRowIndex}]")
    }

    @Test
    fun format() {
        // given
        val spiedSubject = spyk(Block(gridMock, 2, 5))
        val formatterMock: SudokuFormatter = mockk(relaxed = true)
        val expected = Formattable.FormattableList(listOf("formatted Block"))
        every { formatterMock.format(spiedSubject) } returns expected
        // when, then
        assertThat(spiedSubject.format(formatterMock)).isEqualTo(expected)
        verify (exactly = 1) { spiedSubject.format(formatterMock) }
        verify (exactly = 1) { formatterMock.format(spiedSubject) }
        // confirm that no further calls were made
        confirmVerified(spiedSubject, formatterMock)
    }

}
