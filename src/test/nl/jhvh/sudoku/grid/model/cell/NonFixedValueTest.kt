package nl.jhvh.sudoku.grid.model.cell

import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import nl.jhvh.sudoku.grid.event.cellvalue.CellSetValueEvent
import nl.jhvh.sudoku.grid.model.cell.CellValue.NonFixedValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

internal class NonFixedValueTest {

    private lateinit var subject: NonFixedValue
    private lateinit var cellMock: Cell
    private val blockSize = 3
    private val gridSize = blockSize * blockSize

    @BeforeEach
    fun setUp() {
        cellMock = mockk(relaxed = true)
        every {cellMock.grid.blockSize} returns blockSize
        every {cellMock.grid.gridSize} returns gridSize
        every {cellMock.grid.maxValue} returns gridSize

        subject = NonFixedValue(cellMock)
    }

    @Test
    fun setValue() {
        for (newValue in 1..gridSize) {
            subject.setValue(newValue)
            assertThat(subject.value).isEqualTo(newValue)
        }
        var newValue = gridSize+1
        with (assertFailsWith<IllegalArgumentException>{ subject.setValue(newValue) }) {
            assertThat(message).isEqualTo("A cell value must be at most $gridSize but is ${newValue}")
        }
        newValue = 0
        with (assertFailsWith<IllegalArgumentException>{ subject.setValue(newValue) }) {
            assertThat(message).isEqualTo("A cell value must be 1 or higher but is $newValue")
        }
    }

    @Test
    fun `test that setValue publishes an event`() {
        // given
        val newValue = 5
        val cellSetValueEventCapturer: CapturingSlot<CellSetValueEvent> = slot()
        every {cellMock.publish(capture(cellSetValueEventCapturer))} returns Unit
        // when
        subject.setValue(newValue)
        // then
        val publishedEvent = cellSetValueEventCapturer.captured
        verify {cellMock.publish(publishedEvent)}
        assertThat(publishedEvent.newValue).isEqualTo(newValue)
        assertThat(publishedEvent.eventSource).isEqualTo(cellMock)
    }

    @Test
    fun isFixed() {
        assertThat(subject.isFixed).isFalse()
    }

    @Test
    fun hasValue() {
        assertThat(subject.value).isNull()
        assertThat(subject.hasValue()).isFalse()
        subject.setValue(5)
        assertThat(subject.value).isEqualTo(5)
        assertThat(subject.hasValue()).isTrue()
        subject.setValue(3)
        assertThat(subject.value).isEqualTo(3)
        assertThat(subject.hasValue()).isTrue()
    }

    @Test
    fun testToString() {
        assertThat(subject.value).isNull()
        assertThat(subject.isFixed).isFalse()
        assertThat(subject.toString()).contains("NonFixedValue ", "value=null", "isFixed()=false")
        subject.setValue(8)
        assertThat(subject.toString()).contains("NonFixedValue ", "value=8", "isFixed()=false")
    }

    @Test
    fun getCell() {
        assertThat(subject.cell).isEqualTo(cellMock)
    }
}
