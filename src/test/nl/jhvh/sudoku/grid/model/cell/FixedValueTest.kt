package nl.jhvh.sudoku.grid.model.cell

import io.mockk.every
import io.mockk.mockk
import nl.jhvh.sudoku.grid.model.cell.CellValue.FixedValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

internal class FixedValueTest {

    private lateinit var subject: FixedValue
    private lateinit var cellMock: Cell
    private val blockSize = 3
    private val gridSize = blockSize * blockSize
    private val newValue = 1

    @BeforeEach
    fun setUp() {
        cellMock = mockk(relaxed = true)
        every {cellMock.grid.blockSize} returns blockSize
        every {cellMock.grid.gridSize} returns gridSize
        every {cellMock.grid.maxValue} returns gridSize

        subject = FixedValue(cellMock, newValue)
    }

    @Test
    fun setValue() {
        // assert that setting to the same value as before is allowed (and ignored)
        subject.setValue(newValue) // unchanged, succeeds
        var anotherValue = newValue +1
        with (assertFailsWith<IllegalArgumentException> { subject.setValue(newValue+1) }) {
            assertThat(message).isEqualTo("Not allowed to change a fixed value! (value = $anotherValue)")
        }
        anotherValue = -10
        with (assertFailsWith<IllegalArgumentException> { subject.setValue(-10) }) {
            assertThat(message).isEqualTo("Not allowed to change a fixed value! (value = $anotherValue)")
        }
        anotherValue = 500
        with (assertFailsWith<IllegalArgumentException> { subject.setValue(500) }) {
            assertThat(message).isEqualTo("Not allowed to change a fixed value! (value = $anotherValue)")
        }
    }

    @Test
    fun `setValue should not publish an event`() {
        // assert that setting to the same value as before does not publish an event
    }

    @Test
    fun validateRange() {
        TODO("Not implemented yet")
    }

    @Test
    fun isFixed() {
        TODO("Not implemented yet")
    }

    @Test
    fun hasValue() {
        TODO("Not implemented yet")
    }

    @Test
    fun testSetValue() {
        TODO("Not implemented yet")
    }

    @Test
    fun testToString() {
        TODO("Not implemented yet")
    }

    @Test
    fun getCell() {
        TODO("Not implemented yet")
    }
}
