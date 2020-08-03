package nl.jhvh.sudoku.grid.model.segment

import io.mockk.clearMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import nl.jhvh.sudoku.grid.model.Grid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class ColTest {

    @Test
    fun getMaxValueLength() {
        // given
        val gridMock: Grid = mockk(relaxed = true)
        every {gridMock.gridSize} returns 12
        every {gridMock.findCell(any(), any())} returns mockk()
        for (maxValueLength in 1..5) {
            every {gridMock.maxValueLength} returns maxValueLength
            val subject = Col(gridMock, Random.nextInt(0, 13))
            clearMocks(gridMock, answers = false, recordedCalls = true, verificationMarks = true)
            // when, then
            assertThat(subject.maxValueLength).isEqualTo(maxValueLength)
            verify (exactly = 1) {gridMock.maxValueLength}
            confirmVerified(gridMock)
        }
    }

    @Test
    fun getGrid() {
        TODO("Not implemented yet")
    }

    @Test
    fun getColRef() {
        TODO("Not implemented yet")
    }

    @Test
    fun getCellList() {
        TODO("Not implemented yet")
    }

    @Test
    fun testToString() {
        TODO("Not implemented yet")
    }

    @Test
    fun format() {
        TODO("Not implemented yet")
    }

    @Test
    fun getColIndex() {
        TODO("Not implemented yet")
    }
}
