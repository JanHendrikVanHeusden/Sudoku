package nl.jhvh.sudoku.grid.model.cell

/* Copyright 2020 Jan-Hendrik van Heusden

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

import io.mockk.CapturingSlot
import io.mockk.clearMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.excludeRecords
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import nl.jhvh.sudoku.grid.event.ValueEventType.SET_CELL_VALUE
import nl.jhvh.sudoku.grid.event.cellvalue.CellRemoveCandidatesEvent
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.model.cell.CellValue.NonFixedValue
import nl.jhvh.sudoku.grid.model.segment.GridSegment
import nl.jhvh.sudoku.grid.solve.GridNotSolvableException
import nl.jhvh.sudoku.util.log
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random
import kotlin.streams.toList
import kotlin.test.assertFailsWith


internal class NonFixedValueTest {

    private lateinit var subject: NonFixedValue
    private lateinit var cellMock: Cell
    private val blockSize = 3
    private val gridSize = blockSize * blockSize
    private val maxValue = gridSize

    @BeforeEach
    fun setUp() {
        cellMock = mockk(relaxed = true)
        every {cellMock.grid.blockSize} returns blockSize
        every {cellMock.grid.gridSize} returns gridSize
        every {cellMock.grid.maxValue} returns maxValue

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
        val valueRange = (1..maxValue).toList()
        val removedCandidate = 4
        val valuesWithoutRemovedCandidate = valueRange.stream().filter { it != removedCandidate }.toList().toSet()
        val spiedSubject = spyk(NonFixedValue(cellMock))
        every { spiedSubject.getValueCandidates() } returns valuesWithoutRemovedCandidate

        // assert that values within candidate set can be assigned
        for (value in valuesWithoutRemovedCandidate) {
            // when
            spiedSubject.setValue(value)
            // then
            assertThat(spiedSubject.value).isEqualTo(value)
        }
        // when, then: assert that it throws the exception when assigning value that is not in candidate set
        assertFailsWith<GridNotSolvableException> { spiedSubject.setValue(removedCandidate)}
    }

    @Disabled("Does not work; see replacement test in this class")
    @Test
    fun `setValue should publish an event on change of value - not working`() {
        // Verification of subject.publish(...) does not work anymore
        // Maybe because the publish is not called by the subject itself, but delegated to a Delegate.observable
        // (but it has worked, until eventType was added to ValueEventSource methods).
        // Unclear why this happens ???
        //
        // Note that the event IS published, and before the change it actually could be verified
        // (albeit with some vague workarounds).
        // Anyhow verifying behaviour on async / observable stuff seems not very reliable with mockk
        // (also based on experience with other projects)...
        //
        // given
        val subject = NonFixedValue(cellMock)
        val spiedSubject = spyk(subject)
        assertThat(spiedSubject.value).isNull()
        val newValue = 5
        // when
        spiedSubject.setValue(newValue)
        // then
        // Fails here ! AssertionError: Verification failed: call 1 of 1: NonFixedValue(#4).publish(any())) was not called
        verify (timeout = 1000) {spiedSubject.publish(any())}
        // ...
    }

    @Test
    fun `setValue should publish an event on change of value`() {
        // We can not actually verify the publish call, see @Disabled test.
        // Instead we test the onEvent result; onEvent is called by the publish method...
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
    fun removeValueCandidate() {
        // given
        val spiedSubject = spyk(subject)
        every { spiedSubject.publish(any()) } returns Unit

        assertThat(spiedSubject.getValueCandidates()).hasSameElementsAs(1..gridSize)
        var toRemove = setOf(2, 3)

        // when
        var removedIntArray = toRemove.toIntArray()
        spiedSubject.removeValueCandidate(*removedIntArray)

        // then
        var eventCapturer: CapturingSlot<CellRemoveCandidatesEvent> = slot()
        verify { spiedSubject.removeValueCandidate(*removedIntArray) }
        verify (exactly = 1) { spiedSubject.publish(capture(eventCapturer)) }
        var publishedEvent = eventCapturer.captured
        assertThat(publishedEvent.eventSource).isEqualTo(spiedSubject)
        assertThat(publishedEvent.removedValues).isEqualTo(toRemove)
        assertThat(spiedSubject.getValueCandidates()).hasSameElementsAs((1..gridSize)-toRemove)

        clearMocks(spiedSubject, answers = false, recordedCalls = true, verificationMarks = true)

        // given - should also accept values that are removed already or out of range
        toRemove = setOf(7, gridSize+2, 2)

        // when
        removedIntArray = toRemove.toIntArray()
        spiedSubject.removeValueCandidate(*removedIntArray)

        // then
        assertThat(spiedSubject.getValueCandidates()).hasSameElementsAs((1..gridSize)- setOf(2, 3, 7))

        eventCapturer = slot()
        verify { spiedSubject.removeValueCandidate(*removedIntArray) }
        verify (exactly = 1) { spiedSubject.publish(capture(eventCapturer)) }
        publishedEvent = eventCapturer.captured
        assertThat(publishedEvent.eventSource).isEqualTo(spiedSubject)
        assertThat(publishedEvent.removedValues).isEqualTo(setOf(7))

        verify { spiedSubject.getValueCandidates() }
        confirmVerified(spiedSubject)

        clearMocks(spiedSubject, answers = false, recordedCalls = true, verificationMarks = true)

        // given - ignore empty input
        toRemove = setOf()

        // when
        removedIntArray = toRemove.toIntArray()
        spiedSubject.removeValueCandidate(*removedIntArray)

        // then
        verify { spiedSubject.removeValueCandidate(*removedIntArray) }
        confirmVerified(spiedSubject) // nothing else called
        // unchanged
        assertThat(spiedSubject.getValueCandidates()).hasSameElementsAs((1..gridSize)- setOf(2, 3, 7))

        clearMocks(spiedSubject, answers = false, recordedCalls = true, verificationMarks = true)

        // given - everything out of range
        toRemove = setOf(gridSize+1, gridSize+10, -3)

        // when
        removedIntArray = toRemove.toIntArray()
        spiedSubject.removeValueCandidate(*removedIntArray)

        // then
        verify { spiedSubject.removeValueCandidate(*removedIntArray) }
        verify { spiedSubject.getValueCandidates() }
        verify (exactly = 0) { spiedSubject.publish(any()) }
        confirmVerified(spiedSubject)
        // unchanged
        assertThat(spiedSubject.getValueCandidates()).hasSameElementsAs((1..gridSize)- setOf(2, 3, 7))
    }

    @Test
    fun getValueCandidates() {
        assertThat(subject.getValueCandidates()).hasSameElementsAs(1..gridSize)
    }

    @Test
    fun `clearValueCandidatesOnValueSet - value was set`() {
        // given
        val spiedSubject = spyk(subject)
        assertThat(spiedSubject.getValueCandidates()).hasSameElementsAs(1..gridSize)
        every { spiedSubject.publish(any()) } returns Unit
        excludeRecords { spiedSubject.value }
        excludeRecords { spiedSubject.getValueCandidates() }

        val value = 3
        every { spiedSubject.value } returns value

        // when
        spiedSubject.clearValueCandidatesOnValueSet()

        // then
        val eventCapturer: CapturingSlot<CellRemoveCandidatesEvent> = slot()
        verify { spiedSubject.clearValueCandidatesOnValueSet() }
        verify (exactly = 1) { spiedSubject.publish(capture(eventCapturer)) }
        confirmVerified(spiedSubject)

        val publishedEvent = eventCapturer.captured
        assertThat(publishedEvent.eventSource).isEqualTo(spiedSubject)
        assertThat(publishedEvent.removedValues).isEqualTo(((1..gridSize)-value).toSet())
        assertThat(spiedSubject.getValueCandidates()).isEqualTo(setOf(value))

    }

    @Test
    fun `clearValueCandidatesOnValueSet - no value was set`() {
        // given
        val spiedSubject = spyk(subject)
        every { spiedSubject.publish(any()) } returns Unit
        excludeRecords { spiedSubject.value }
        excludeRecords { spiedSubject.getValueCandidates() }

        assertThat(spiedSubject.getValueCandidates()).hasSameElementsAs(1..gridSize)

        // when
        spiedSubject.clearValueCandidatesOnValueSet() // no value set

        // then
        val eventCapturer: CapturingSlot<CellRemoveCandidatesEvent> = slot()
        verify { spiedSubject.clearValueCandidatesOnValueSet() }
        verify (exactly = 1) { spiedSubject.publish(capture(eventCapturer)) }
        confirmVerified(spiedSubject)

        val publishedEvent = eventCapturer.captured
        assertThat(publishedEvent.eventSource).isEqualTo(spiedSubject)
        assertThat(publishedEvent.removedValues).isEqualTo((1..gridSize).toSet())
        assertThat(spiedSubject.getValueCandidates()).isEmpty()

    }

    @Test
    fun isSet() {
        // given
        assertThat(subject.value).isNull()
        assertThat(subject.value === VALUE_UNKNOWN).isTrue()
        // when, then
        assertThat(subject.isSet).isFalse()
        // given
        val newValue = Random.nextInt(1, gridSize)
        subject.setValue(newValue)
        assertThat(subject.value).isEqualTo(newValue)
        assertThat(subject.isSet)
    }

    @Test
    fun `valueCandidates should be thread safe`() {
        assertThat(NonFixedValue(cellMock).getValueCandidates().javaClass.name)
                .isEqualTo("java.util.concurrent.ConcurrentHashMap\$KeySetView")
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
