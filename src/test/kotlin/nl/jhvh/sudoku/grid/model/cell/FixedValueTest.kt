package nl.jhvh.sudoku.grid.model.cell

import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import nl.jhvh.sudoku.grid.model.cell.CellValue.FixedValue
import nl.jhvh.sudoku.util.log
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

internal class FixedValueTest {

    private lateinit var subject: FixedValue
    private lateinit var cellMock: Cell
    private val blockSize = 3
    private val gridSize = blockSize * blockSize

    @BeforeEach
    fun setUp() {
        cellMock = mockk(relaxed = true)
        every {cellMock.grid.blockSize} returns blockSize
        every {cellMock.grid.gridSize} returns gridSize
        every {cellMock.grid.maxValue} returns gridSize
    }

    @Test
    fun `constructor should accept values within range and reject values out of range`() {
        for (newValue in 1..gridSize) {
            subject = FixedValue(cellMock, newValue)
            assertThat(subject.value).isEqualTo(newValue)
        }
        var newValue = gridSize+1
        with (assertFailsWith<IllegalArgumentException>{ FixedValue(cellMock, newValue) }) {
            assertThat(message).isEqualTo("A cell value must be at most $gridSize but is $newValue")
        }
        newValue = 0
        with (assertFailsWith<IllegalArgumentException>{ FixedValue(cellMock, newValue) }) {
            assertThat(message).isEqualTo("A cell value must be 1 or higher but is $newValue")
        }
    }

    @Test
    fun `setValue should not accept change of value`() {
        val newValue = 1
        subject = FixedValue(cellMock, newValue)
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
    fun `setValue should publish an event when setting to a different value`() {
        // given
        val newValue = 5
        subject = FixedValue(cellMock, newValue)
        // clear recorded events
        clearMocks(cellMock, answers = false, recordedCalls = true, verificationMarks = true)

        every {cellMock.publish(any())} returns Unit
        // when - setting to same value
        subject.setValue(newValue)
        // then - verify that no event is published
        verify (exactly = 0) {cellMock.publish(any())}

        // when - trying to set a different value
        try {
            subject.setValue(newValue + 1)
        } catch (e: Exception) {
            log().info { "${e.javaClass.simpleName} thrown (as expected when trying to set a ${FixedValue::class.simpleName} to another value)" }
        }
        // then - verify that unsuccessful setting does not fire an event
        verify (exactly = 0) {cellMock.publish(any())}
    }

    @Test
    fun isFixed() {
        subject = FixedValue(cellMock, 2)
        assertThat(subject.isFixed).isTrue()
    }

    @Test
    fun hasValue() {
        subject = FixedValue(cellMock, 8)
        assertThat(subject.hasValue()).isTrue()
    }

    @Test
    fun testToString() {
        subject = FixedValue(cellMock, 6)
        assertThat(subject.toString()).contains("FixedValue ", "value=6", "isFixed()=true")
    }

    @Test
    fun getCell() {
        subject = FixedValue(cellMock, 9)
        assertThat(subject.cell).isEqualTo(cellMock)
    }
}
