package nl.jhvh.sudoku.grid.model.segment

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.GridTestBase
import nl.jhvh.sudoku.grid.event.GridEvent
import nl.jhvh.sudoku.grid.event.GridEventType.CELL_REMOVE_CANDIDATES
import nl.jhvh.sudoku.grid.event.candidate.CellRemoveCandidatesEvent
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.model.Grid
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.HashSet
import kotlin.random.Random

internal class GridSegmentTest : GridTestBase() {

    /** grid mock and cell mocks initialized in [GridTestBase.gridSetUp] */
    override lateinit var gridMock: Grid
    override val blockSize = 3
    override val gridSize = 9

    private lateinit var subject: GridSegment

    @BeforeEach
    fun setUp() {
        subject = object : GridSegment(gridMock) {
            // Just grab some of the (mocked) cells to populate our mocked GridSegment
            override val cells = LinkedHashSet(gridMock.cellList.filter { it.colIndex == it.rowIndex })
            override fun format(formatter: SudokuFormatter): FormattableList = FormattableList(emptyList())
            override fun toString() = "GridElement for testing"
        }
        subject.cells.forEach {cellMock ->
            every { cellMock.getValueCandidates() } returns HashSet(IntRange(1, gridSize).toList())
        }
        every { gridMock.handleCellRemoveCandidatesEvent(any(), any()) } returns Unit
        every { gridMock.handleCellSetValueEvent(any(), any()) } returns Unit
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
        verify (exactly = 1) {gridMock.handleCellSetValueEvent(setCellValueEvent, spiedSubject)}
    }

    @Test
    fun `onEvent - CellRemoveCandidatesEvent`() {
        // given
        val spiedSubject = spyk(subject)
        every { spiedSubject.grid } returns subject.grid
        // grab a random cell of the GridSegment's cells
        val eventCellMock = spiedSubject.cells.toList()[Random.nextInt(0, gridSize)]
        val cellRemoveCandidatesEvent = CellRemoveCandidatesEvent(eventCellMock, setOf(1, 4, 6), setOf(1, 6))
        // when
        spiedSubject.onEvent(cellRemoveCandidatesEvent)
        // then
        verify (exactly = 1) {gridMock.handleCellRemoveCandidatesEvent(cellRemoveCandidatesEvent, spiedSubject)}
    }

    @Test
    fun `onEvent should ignore event types it is not interested in`() {
        val spiedSubject = spyk(subject)
        val someGridEventSubType: GridEvent = spyk(object : GridEvent {
            override val eventSource = spiedSubject
            override val type = CELL_REMOVE_CANDIDATES
            override fun toString() = "Some unknown event type for test"
        })
        spiedSubject.onEvent(someGridEventSubType)
        verify { spiedSubject.onEvent(someGridEventSubType) }
        verify { someGridEventSubType.toString() } // in the Exception message
        // verify that no further calls were made
        confirmVerified(spiedSubject)
        confirmVerified(someGridEventSubType)
    }

}