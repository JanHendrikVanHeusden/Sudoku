package nl.jhvh.sudoku.grid.model.cell

import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import nl.jhvh.sudoku.base.intRangeSet
import nl.jhvh.sudoku.grid.event.GridEventType.SET_CELL_VALUE
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.model.cell.CellValue.NonFixedValue
import nl.jhvh.sudoku.grid.model.segment.GridSegment
import nl.jhvh.sudoku.grid.solve.GridNotSolvableException
import nl.jhvh.sudoku.util.log
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.streams.toList
import kotlin.test.assertFailsWith


internal class NonFixedValueTest {

    private lateinit var subject: NonFixedValue
    private lateinit var cellMock: Cell
    private val blockSize = 3
    private val gridSize = blockSize * blockSize
    private val maxValue = gridSize

    private val valueCandidates = intRangeSet(1, maxValue)

    @BeforeEach
    fun setUp() {
        cellMock = mockk(relaxed = true)
        every {cellMock.grid.blockSize} returns blockSize
        every {cellMock.grid.gridSize} returns gridSize
        every {cellMock.grid.maxValue} returns gridSize
        every { cellMock.getValueCandidates() } returns valueCandidates
        every { cellMock.clearValueCandidates() } returns Unit

        subject = NonFixedValue(cellMock)
    }

    @Test
    fun `setValue should accept values within range and reject values out of range`() {
        for (newValue in 1..gridSize) {
            subject.setValue(newValue)
            assertThat(subject.value).isEqualTo(newValue)
        }
        var newValue = gridSize+1
        with(assertFailsWith<IllegalArgumentException> { subject.setValue(newValue) }) {
            assertThat(message).isEqualTo("A cell value must be at most $gridSize but is $newValue")
        }
        newValue = 0
        with(assertFailsWith<IllegalArgumentException> { subject.setValue(newValue) }) {
            assertThat(message).isEqualTo("A cell value must be 1 or higher but is $newValue")
        }
    }

    @Test
    fun `setValue should fail with 'not solvable' when trying to set a value while no such value candidate is present`() {
        // given
        val valueRange = intRangeSet(1, maxValue)
        val removedCandidate = 4
        val valuesWithoutRemovedCandidate = valueRange.stream().filter { it != removedCandidate }.toList().toSet()
        every { cellMock.getValueCandidates() } returns valuesWithoutRemovedCandidate

        // assert that values within candidate set can be assigned
        for (value in valuesWithoutRemovedCandidate) {
            // when
            subject.setValue(value)
            // then
            assertThat(subject.value).isEqualTo(value)
        }
        // when, then: assert that it throws the exception when assigning value that is not in candidate set
        assertFailsWith<GridNotSolvableException> { subject.setValue(removedCandidate)}
    }

    @Test
    fun `setValue should publish an event on change of value`() {
        // Verification of subject.publish(...) does not work anymore since eventType was added
        // to GridEventSource methods.
        // Unclear why this happens ??? Note that the event IS published, and before the change
        // it actually could be verified (albeit with some vague workarounds).
        // Anyhow verifying behaviour on async / observable stuff seems not very reliable with mockk
        // (also based on experience with other projects)...
        //
        // So we can not actually verify the publish call. Instead we test the onEvent result; onEvent is called by
        // the publish method...
        // So now it is an integration test rather than a unit test :-(
        // ... but at least we can reliably verify the behaviour this way :-)

        // given
        val subject = NonFixedValue(cellMock)
        val gridSegmentMock: GridSegment = mockk(relaxed = true)
        val eventCounter = AtomicInteger(0)
        lateinit var eventCapturer: CapturingSlot<SetCellValueEvent>

        fun initMock() {
            eventCapturer = slot()
            every { gridSegmentMock.onEvent(capture(eventCapturer)) } answers {
                eventCounter.incrementAndGet()
                log().warn {"IGNORE THIS! Just for testing: mocked '$this' received event, eventCounter = $eventCounter"}
                Unit
            }
        }

        initMock()
        subject.subscribe(gridSegmentMock, SET_CELL_VALUE)

        assertThat(subject.value).isNull()
        var newValue = 5
        // when
        subject.setValue(newValue)

        // then
        await()
                .timeout(1, TimeUnit.SECONDS)
                .pollInterval(10, TimeUnit.MILLISECONDS)
                .untilAtomic(eventCounter, equalTo(1))

        // assert that onEvent has happened 1 time
        assertThat(eventCounter.get()).isEqualTo(1)
        assertThat(eventCapturer.isCaptured).isTrue()

        // assert correct event
        assertThat(eventCapturer.captured.type).isEqualTo(SET_CELL_VALUE)
        assertThat(eventCapturer.captured.newValue).isEqualTo(newValue)
        assertThat(eventCapturer.captured.eventSource).isEqualTo(subject)

        initMock()

        // when
        subject.setValue(newValue)
        // then - assert that setting same value again does not fire an event
        await()
                .timeout(100, TimeUnit.MILLISECONDS)
                .pollInterval(10, TimeUnit.MILLISECONDS)
                .untilAtomic(eventCounter, equalTo(1))
        assertThat(eventCapturer.isCaptured).isFalse()
        assertThat(eventCounter.get()).isEqualTo(1) // not incremented

        // given - different value now
        newValue = 6
        initMock()

        // when
        subject.setValue(newValue)
        await()
                .timeout(1, TimeUnit.SECONDS)
                .pollInterval(10, TimeUnit.MILLISECONDS)
                .untilAtomic(eventCounter, equalTo(2))

        // then - verify that setting it to another value publishes a new event
        // assert that onEvent has happened due to new value
        assertThat(eventCounter.get()).isEqualTo(2) // incremented
        assertThat(eventCapturer.isCaptured).isTrue()
        // assert correct event
        assertThat(eventCapturer.captured.type).isEqualTo(SET_CELL_VALUE)
        assertThat(eventCapturer.captured.newValue).isEqualTo(newValue)
        assertThat(eventCapturer.captured.eventSource).isEqualTo(subject)
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
