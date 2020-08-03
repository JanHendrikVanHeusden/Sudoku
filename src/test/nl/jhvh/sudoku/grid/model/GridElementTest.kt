package nl.jhvh.sudoku.grid.model

import io.mockk.clearMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.SudokuFormatter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

val gridMock: Grid = mockk()

internal class GridElementTest {

    @Test
    fun getMaxValueLength() {
        for (valueLength in 1..10) {
            // given
            every { gridMock.maxValueLength } returns valueLength
            val gridElement = object: GridElement(gridMock) {
                override fun format(formatter: SudokuFormatter): FormattableList {
                    return mockk()
                }
            }
            clearMocks(gridMock, answers = false, recordedCalls = true, verificationMarks = true)
            // when, then
            assertThat(gridElement.maxValueLength).isEqualTo(valueLength)
            verify (exactly = 1) {gridMock.maxValueLength}
            confirmVerified(gridMock)
        }
    }
}
