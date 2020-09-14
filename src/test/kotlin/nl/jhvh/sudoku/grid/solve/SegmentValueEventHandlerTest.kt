package nl.jhvh.sudoku.grid.solve

import io.mockk.clearAllMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import nl.jhvh.sudoku.format.Formattable
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.GridWithCellsTestBase
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.model.cell.CellValue.NonFixedValue
import nl.jhvh.sudoku.grid.model.segment.GridSegment
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.LinkedHashSet
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.random.Random
import kotlin.test.assertFailsWith

/** grid mock and cell mocks initialized in [GridWithCellsTestBase.gridSetUp] */
@Disabled("Event handling will be done by GridSolver - work in progress!")
internal class SegmentValueEventHandlerTest: GridWithCellsTestBase(blockSize = 3) {

    private var subject = SegmentValueEventHandler()
    private lateinit var segment: GridSegment
    private val initialCandidateProvider: () -> Set<Int> = { HashSet(IntRange(1, gridSize).toList())}

    @BeforeEach
    fun setUp() {
        // Just grab some of the (mocked) cells to populate our mocked GridSegment
        val cellsInSegment = ArrayList(gridMock.cellList.filter { it.colIndex == it.rowIndex })
        segment = object : GridSegment(gridMock) {
            // Just grab some of the (mocked) cells to populate our mocked GridSegment
            override val cells = LinkedHashSet(cellsInSegment)
            override fun format(formatter: SudokuFormatter): Formattable.FormattableList = Formattable.FormattableList(emptyList())
            override fun toString() = "GridSegment for testing"
        }
        // all Cell.cellValue are mocked as NonFixedValue in GridWithCellsTestBase, so no problems with cast below
        segment.cells.map{it.cellValue as NonFixedValue} .forEach { cellValue ->
            every { cellValue.getValueCandidates() } returns initialCandidateProvider.invoke()
        }
    }

    @Test
    fun `handleSetCellValueEvent from cell in containing segment`() {
        // given
        val segmentCellValueList = segment.cells.map { it.cellValue }.toList()
        // all Cell.cellValue are mocked as NonFixedValue in GridWithCellsTestBase, so no problems with cast below
        val eventSource = segmentCellValueList.random() as NonFixedValue
        val newValue = Random.nextInt(1, gridSize+1)
        val valueEvent = SetCellValueEvent(eventSource, newValue)
        clearAllMocks(answers = false, recordedCalls = true)

        // when
        subject.handleSetCellValueEvent(valueEvent, segment)

        // then
        // verify event source calls
        val valueEventType = valueEvent.type
        verify (exactly = 1) { eventSource.clearValueCandidates() }
        verify (exactly = 1) { eventSource.unsubscribe(segment, valueEventType) }
        verify { eventSource.cell }
        confirmVerified(eventSource)

        // verify other cellValues in the segment
        segment.cells.map { cell -> cell.cellValue as NonFixedValue }
                .filter { it !== eventSource }
                .forEach { cellValue ->
                    verify (exactly = 1) { cellValue.removeValueCandidate(newValue) }
                    confirmVerified(cellValue)
                }
    }

    @Test
    fun `handleSetCellValueEvent from cell not in containing segment should fail`() {
        // given
        val nonSegementCellValueList = gridMock.cellList.minus(segment.cells)
        // just grab a few random cells not in the segment; not necessary to test all 72 cells
        for (x in 1..5) {
            val eventSource = nonSegementCellValueList.random().cellValue
            val newValue = Random.nextInt(1, gridSize+1)
            val valueEvent = SetCellValueEvent(eventSource, newValue)
            // when, then
            assertFailsWith<IllegalStateException> { subject.handleSetCellValueEvent(valueEvent, segment) }
        }
    }

    @Test
    fun handleCellRemoveCandidatesEvent() {
        // TODO
    }
}