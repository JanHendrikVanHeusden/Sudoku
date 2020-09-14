package nl.jhvh.sudoku.grid.event

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import nl.jhvh.sudoku.grid.event.ValueEventType.CELL_REMOVE_CANDIDATES
import nl.jhvh.sudoku.grid.event.ValueEventType.SET_CELL_VALUE
import nl.jhvh.sudoku.grid.model.segment.GridSegment
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.ConcurrentHashMap

class ValueEventSourceTest {

    lateinit var subject: ValueEventSource

    @BeforeEach
    fun setUp() {
        subject = object : ValueEventSource {
            // For this test, eventListeners and its contents need not be thread safe actually,
            // but just copied the real implementation here (which as a bonus is thread safe)
            override val eventListeners: ConcurrentHashMap<ValueEventType, MutableSet<ValueEventListener>> = ConcurrentHashMap()
            override fun initEventListeners() {
                ValueEventType.values().forEach {
                    eventListeners.putIfAbsent(it, ConcurrentHashMap.newKeySet())
                }
            }
            override fun toString(): String = "GridEventSource for unit test"
            init {
                initEventListeners()
            }
        }
    }

    @Test
    fun subscribe() {
        // given
        ValueEventType.values().forEach {
            assertThat(subject.eventListeners[it]).isEmpty()
        }
        assertThat(subject.eventListeners).hasSize(2) // 1 for each event type
        val gridSegmentMock1: GridSegment = mockk()
        val gridSegmentMock2: GridSegment = mockk()

        // when
        subject.subscribe(gridSegmentMock1, CELL_REMOVE_CANDIDATES)

        // then
        assertThat(subject.eventListeners[CELL_REMOVE_CANDIDATES]).hasSize(1)
        assertThat(subject.eventListeners[CELL_REMOVE_CANDIDATES]?.first() === gridSegmentMock1).isTrue()
        // assert that no alien value was added :-)
        assertThat(subject.eventListeners[SET_CELL_VALUE]).isNullOrEmpty()

        // when
        subject.subscribe(gridSegmentMock1, SET_CELL_VALUE)
        // then - previous subscription should still be there
        assertThat(subject.eventListeners[CELL_REMOVE_CANDIDATES]).hasSize(1)
        assertThat(subject.eventListeners[CELL_REMOVE_CANDIDATES]?.first() === gridSegmentMock1).isTrue()
        // then - newly added subscription
        assertThat(subject.eventListeners[SET_CELL_VALUE]).hasSize(1)
        assertThat(subject.eventListeners[SET_CELL_VALUE]?.first() === gridSegmentMock1).isTrue()
        // when - subscribing same thing twice, should make no difference
        subject.subscribe(gridSegmentMock1, SET_CELL_VALUE)
        // then
        assertThat(subject.eventListeners[SET_CELL_VALUE]).hasSize(1)
        assertThat(subject.eventListeners[SET_CELL_VALUE]?.first() === gridSegmentMock1).isTrue()

        // when - adding another listener
        subject.subscribe(gridSegmentMock2, SET_CELL_VALUE)
        // then
        assertThat(subject.eventListeners[SET_CELL_VALUE]).hasSize(2)
        assertThat(subject.eventListeners[SET_CELL_VALUE]).hasSameElementsAs(listOf(gridSegmentMock1, gridSegmentMock2))
    }

    @Test
    fun unSubscribe() {
        // given
        ValueEventType.values().forEach {
            assertThat(subject.eventListeners[it]).isEmpty()
        }
        val gridSegmentMock1: GridSegment = mockk()
        val gridSegmentMock2: GridSegment = mockk()
        assertThat(subject.eventListeners).hasSize(2) // 1 for each event type

        subject.subscribe(gridSegmentMock1, CELL_REMOVE_CANDIDATES)
        assertThat(subject.eventListeners[CELL_REMOVE_CANDIDATES]).hasSize(1)
        assertThat(subject.eventListeners[CELL_REMOVE_CANDIDATES]?.first() === gridSegmentMock1).isTrue()

        // when - unsubscribe something that did not subscribe at all, no problem
        subject.unsubscribe(gridSegmentMock2, CELL_REMOVE_CANDIDATES)
        // then
        assertThat(subject.eventListeners[CELL_REMOVE_CANDIDATES]).hasSize(1)
        assertThat(subject.eventListeners[CELL_REMOVE_CANDIDATES]?.first() === gridSegmentMock1).isTrue()
        // when
        subject.unsubscribe(gridSegmentMock1, SET_CELL_VALUE)
        // then
        assertThat(subject.eventListeners[CELL_REMOVE_CANDIDATES]).hasSize(1)
        assertThat(subject.eventListeners[CELL_REMOVE_CANDIDATES]?.first() === gridSegmentMock1).isTrue()

        // when - unsubscribe something that actually subscribed
        subject.unsubscribe(gridSegmentMock1, CELL_REMOVE_CANDIDATES)
        // then
        assertThat(subject.eventListeners[CELL_REMOVE_CANDIDATES]).isNullOrEmpty()
        // event types should still be there
        assertThat(subject.eventListeners).hasSize(2) // 1 for each event type
    }

    @Test
    fun publish() {
        // given
        val gridSegmentMock1: GridSegment = mockk()
        every {gridSegmentMock1.onEvent(any())} returns Unit
        val valueEvent1: ValueEvent = mockk(relaxed = true)
        every {valueEvent1.type} returns SET_CELL_VALUE

        val gridSegmentMock2: GridSegment = mockk()
        every {gridSegmentMock2.onEvent(any())} returns Unit
        val valueEvent2: ValueEvent = mockk(relaxed = true)
        every {valueEvent2.type} returns CELL_REMOVE_CANDIDATES

        // when - no relevant subscriptions yet, nothing should be published
        subject.subscribe(gridSegmentMock2, valueEvent2.type)
        subject.publish(valueEvent1)
        // then
        verify (exactly = 0, timeout = 100) { gridSegmentMock1.onEvent(any()) }
        verify (exactly = 0, timeout = 100) { gridSegmentMock2.onEvent(any()) }

        // when - subscribed event
        subject.publish(valueEvent2)
        // then
        verify (exactly = 0, timeout = 100) { gridSegmentMock2.onEvent(valueEvent1) }
        verify (exactly = 0, timeout = 100) { gridSegmentMock1.onEvent(valueEvent1) }
        verify (exactly = 1) { gridSegmentMock2.onEvent(valueEvent2) }

    }

}