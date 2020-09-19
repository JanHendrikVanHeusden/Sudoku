package nl.jhvh.sudoku.grid.model.segment

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.GridWithCellsTestBase
import nl.jhvh.sudoku.grid.event.ValueEvent
import nl.jhvh.sudoku.grid.event.ValueEventType.CELL_REMOVE_CANDIDATES
import nl.jhvh.sudoku.grid.event.cellvalue.CellRemoveCandidatesEvent
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.model.cell.CellValue.NonFixedValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.random.Random

/** grid mock and cell mocks initialized in [GridWithCellsTestBase.gridSetUp] */
internal class GridSegmentTest : GridWithCellsTestBase(blockSize = 3) {

    private lateinit var subject: GridSegment

    @BeforeEach
    fun setUp() {
        subject = object : GridSegment(gridMock) {
            // Just grab some of the (mocked) cells to populate our mocked GridSegment
            override val cells = LinkedHashSet(gridMock.cellList.filter { it.colIndex == it.rowIndex })
            override fun format(formatter: SudokuFormatter): FormattableList = FormattableList(emptyList())
            override fun toString() = "GridSegment for testing"
        }
        every { gridMock.handleRemoveCandidatesEvent(any(), any()) } returns Unit
        every { gridMock.handleSetCellValueEvent(any(), any()) } returns Unit
    }

    @Test
    fun `onEvent - SetCellValueEvent`() {
        // given
        val spiedSubject = spyk(subject)
        val newValue = Random.nextInt(1, gridSize+1)
        // grab a random cell of the GridSegment's cells
        val eventCellMock = spiedSubject.cells.toList()[Random.nextInt(0, gridSize)]
        val setCellValueEvent = SetCellValueEvent(eventCellMock.cellValue, newValue)
        // when
        spiedSubject.onEvent(setCellValueEvent)
        // then
        verify (exactly = 1) {gridMock.handleSetCellValueEvent(setCellValueEvent, spiedSubject)}
    }

    @Test
    fun `onEvent - CellRemoveCandidatesEvent`() {
        // given
        val spiedSubject = spyk(subject)
        every { spiedSubject.grid } returns subject.grid
        // grab a random cell of the GridSegment's cells
        val eventCellMock = spiedSubject.cells.toList()[Random.nextInt(0, gridSize)]
        val nonFixedValueMock: NonFixedValue = mockk()

        every {eventCellMock.cellValue} returns nonFixedValueMock
        every {nonFixedValueMock.cell} returns eventCellMock

        val cellRemoveCandidatesEvent = CellRemoveCandidatesEvent(nonFixedValueMock, setOf(1, 6))
        // when
        spiedSubject.onEvent(cellRemoveCandidatesEvent)
        // then - event cell is part of the GridSegment (spiedSubject), so should handle it
        verify (exactly = 1) {gridMock.handleRemoveCandidatesEvent(cellRemoveCandidatesEvent, spiedSubject)}
    }

    @Test
    fun `onEvent should ignore other event types`() {
        // given
        val spiedSubject = spyk(subject)
        every { spiedSubject.grid } returns subject.grid
        // grab a random cell of the GridSegment's cells
        val eventCellMock = spiedSubject.cells.toList()[Random.nextInt(0, gridSize)]
        val nonFixedValueMock: NonFixedValue = mockk()

        every {eventCellMock.cellValue} returns nonFixedValueMock
        every {nonFixedValueMock.cell} returns eventCellMock

        val someValueEvent: ValueEvent = spyk(object : ValueEvent {
            override val eventSource = nonFixedValueMock
            override val type = CELL_REMOVE_CANDIDATES // just any type; irrelevant for the test
            override fun toString() = "Some event type for test"
        })
        // when
        spiedSubject.onEvent(someValueEvent)

        // then - event type the handler is not interested in, so only some general stuff is called
        verify { spiedSubject.onEvent(someValueEvent) }
        verify { spiedSubject.cells } // to check if the eventSource is part of the segment
        verify { spiedSubject.toString() } // in the log message
        verify { someValueEvent.toString() } // in the log message
        verify { someValueEvent.eventSource } // to check if the eventSource is part of the segment

        // verify that no further calls were made: event is ignored
        confirmVerified(spiedSubject)
        confirmVerified(someValueEvent)
    }

    private fun `onEvent should ignore eventSource that is not part of the Segment`(valueEvent: ValueEvent) {
        // given
        val spiedSubject = spyk(subject)
        every { spiedSubject.grid } returns subject.grid

        // grab a random cell that is not one of the GridSegment's cells
        val nonSegmentCells = gridMock.cellList - spiedSubject.cells
        val eventCellMock = nonSegmentCells[Random.nextInt(0, nonSegmentCells.size)]
        val nonFixedValueMock: NonFixedValue = mockk()

        every {eventCellMock.cellValue} returns nonFixedValueMock
        every {nonFixedValueMock.cell} returns eventCellMock

        val eventSpyk = spyk(valueEvent)
        every { eventSpyk.eventSource } returns nonFixedValueMock

        // when
        spiedSubject.onEvent(eventSpyk)

        // then - event cell / cellValue is not part of the GridSegment (spiedSubject), so only some general stuff is called
        verify { spiedSubject.onEvent(eventSpyk) }
        verify { spiedSubject.cells } // to check if the eventSource is part of the segment
        verify { spiedSubject.toString() } // in the log message
        verify { eventSpyk.toString() } // in the log message
        verify { eventSpyk.eventSource } // to check if the eventSource is part of the segment

        // verify that no further calls were made: event is ignored
        confirmVerified(spiedSubject)
        confirmVerified(eventSpyk)
    }

    @Test
    fun `onEvent should ignore CellRemoveCandidatesEvent where eventSource is not part of the segment`() {
        val valueEvent = CellRemoveCandidatesEvent(mockk(), setOf(1, 6))
        `onEvent should ignore eventSource that is not part of the Segment`(valueEvent)
    }

    @Test
    fun `onEvent should ignore SetCellValueEvent where eventSource is not part of the segment`() {
        val valueEvent = spyk(SetCellValueEvent(mockk(), 5))
        `onEvent should ignore eventSource that is not part of the Segment`(valueEvent)
    }

}