package nl.jhvh.sudoku.grid.model.cell

import io.mockk.CapturingSlot
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.slot
import io.mockk.spyk
import io.mockk.unmockkConstructor
import io.mockk.verify
import nl.jhvh.sudoku.grid.event.GridEventType.SET_CELL_VALUE
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.model.GridElement
import nl.jhvh.sudoku.grid.model.cell.CellValue.NonFixedValue
import nl.jhvh.sudoku.grid.model.segment.GridSegment
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
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
    fun `setValue should accept values within range and reject values out of range`() {
        for (newValue in 1..gridSize) {
            subject.setValue(newValue)
            assertThat(subject.value).isEqualTo(newValue)
        }
        var newValue = gridSize+1
        with (assertFailsWith<IllegalArgumentException>{ subject.setValue(newValue) }) {
            assertThat(message).isEqualTo("A cell value must be at most $gridSize but is $newValue")
        }
        newValue = 0
        with (assertFailsWith<IllegalArgumentException>{ subject.setValue(newValue) }) {
            assertThat(message).isEqualTo("A cell value must be 1 or higher but is $newValue")
        }
    }

    @Test
    @Disabled("Not working currently, mocking / verification does not work anymore somehow")
    fun `setValue should publish an event on change of value`() {
        TODO("Not working currently, mocking / verification does not work anymore somehow")
        var publishCount = 0
        try {
            mockkConstructor(GridElement::class)
            every { anyConstructed<GridElement>().publish(any()) } coAnswers {
                publishCount++
                Unit
            }

            val subject = spyk(NonFixedValue(cellMock))
//            every {subject.publish(any())} answers {
//                published = true
//                Unit
//            }
            // given
//            mockkConstructor(GridEvent::class)
//            every {anyConstructed<GridEvent>().type} returns SET_CELL_VALUE
//            mockkConstructor(SetCellValueEvent::class)
//            every {anyConstructed<SetCellValueEvent>().type} returns SET_CELL_VALUE
//
        val gridSegmentMock: GridSegment = mockk(relaxed = true)
        every { gridSegmentMock.onEvent(any()) } returns Unit
        subject.subscribe(gridSegmentMock, SET_CELL_VALUE)
            // This test is brittle! The GridSegment should not be needed, but without it,
            // the test fails with  io.mockk.MockKException: Failed matching mocking signature for left matchers: [any()]
            // mockk seems to have quite some bugs / flaws causing that exception (mockk version: 1.10.0)?

            assertThat(subject.value).isNull()
            var newValue = 5
            // when
            println("voor: published = ${publishCount}")
            subject.setValue(newValue)
            println("na  : published = ${publishCount}")
            // then
            // due to (probably) asynchronous behaviour of Delegates.observable, the publish event maybe delayed, so first verify with timeout
println(System.currentTimeMillis())
            try {
                verify(atLeast = 0, atMost = 1, timeout = 1000) { subject.publish(any<SetCellValueEvent>()) }
            } catch (t: Throwable) {
                //
            }
println(System.currentTimeMillis())
            println("na  : published = ${publishCount}")
            val setCellValueEventCapturer1: CapturingSlot<SetCellValueEvent> = slot()
            verify(exactly = 1, timeout = 1000) { subject.publish(capture(setCellValueEventCapturer1)) }
            assertThat(setCellValueEventCapturer1.isCaptured).isTrue()
            val publishedEvent1 = setCellValueEventCapturer1.captured
            assertThat(publishedEvent1.newValue).isEqualTo(newValue)
            assertThat(publishedEvent1.eventSource).isEqualTo(subject)

            // clear previously recorded events
            clearAllMocks(answers = false, recordedCalls = true)
            // Need to exclude this here, otherwise mockk will fail saying that onEvent happened while not verified
            // Actually, we never wanted / asked to verify that, so seems to be a bug too?? (mockk version: 1.10.0)
//        excludeRecords { gridSegmentMock.onEvent(any())}

            // when
            subject.setValue(newValue)
            // then - verify that setting same value again does not fire an event
            val setCellValueEventCapturer2: CapturingSlot<SetCellValueEvent> = slot()
            verify(exactly = 0, timeout = 100) { subject.publish(capture(setCellValueEventCapturer2)) }
            assertThat(setCellValueEventCapturer2.isCaptured).isFalse()

            // clear previously recorded events
            clearAllMocks(answers = false, recordedCalls = true)

            // given - different value now
            newValue = 6

            // when
            subject.setValue(newValue)

            // then - verify that setting it to another value publishes a new event
            verify(timeout = 1000) { subject.publish(any()) }
            val setCellValueEventCapturer3: CapturingSlot<SetCellValueEvent> = slot()
            verify(exactly = 1) { subject.publish(capture(setCellValueEventCapturer3)) }
            assertThat(setCellValueEventCapturer3.isCaptured).isTrue()
            val publishedEvent3 = setCellValueEventCapturer3.captured
            assertThat(publishedEvent3.newValue).isEqualTo(newValue)
            assertThat(publishedEvent3.eventSource).isEqualTo(subject)
        } finally {
//            unmockkConstructor(GridEvent::class, SetCellValueEvent::class)
            unmockkConstructor(GridElement::class)
        }
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
