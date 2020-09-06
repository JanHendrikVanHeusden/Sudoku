package nl.jhvh.sudoku.grid.solve

import io.mockk.every
import io.mockk.verify
import nl.jhvh.sudoku.format.Formattable
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.GridWithCellsTestBase
import nl.jhvh.sudoku.grid.event.GridEventType.SET_CELL_VALUE
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.model.segment.GridSegment
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.LinkedHashSet
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.collections.Set
import kotlin.collections.emptyList
import kotlin.collections.filter
import kotlin.collections.forEach
import kotlin.collections.map
import kotlin.collections.minus
import kotlin.collections.random
import kotlin.collections.toList
import kotlin.random.Random
import kotlin.test.assertFailsWith

/** grid mock and cell mocks initialized in [GridWithCellsTestBase.gridSetUp] */
internal class GridSolverTest: GridWithCellsTestBase(blockSize = 3) {

    private var subject = GridSolver()
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
        segment.cells.forEach { cellMock ->
            every { cellMock.getValueCandidates() } returns initialCandidateProvider.invoke()
        }
    }

    @Test
    fun `handleSetCellValueEvent from cell in containing segment`() {
        // given
        val segmentCellValueList = segment.cells.map { it.cellValue }.toList()
        val eventSource = segmentCellValueList.random()
        val eventSourceCell = eventSource.cell
        val newValue = Random.nextInt(1, gridSize+1)
        val gridEvent = SetCellValueEvent(eventSource, newValue)
        // when
        subject.handleSetCellValueEvent(gridEvent, segment)
        // then
        verify { eventSourceCell.clearValueCandidates() }
        segment.cells.filter { it.cellValue != eventSource }.forEach {
            verify { it.removeValueCandidate(newValue) }
            verify { it.unsubscribe(segment, SET_CELL_VALUE) }
        }
    }

    @Test
    fun `handleSetCellValueEvent from cell not in containing segment should fail`() {
        // given
        val nonSegementCellValueList = gridMock.cellList.minus(segment.cells)
        // just grab a few random cells not in the segment; not necessary to test all 72 cells
        for (x in 1..5) {
            val eventSourceCell = nonSegementCellValueList.random()
            val eventSource = eventSourceCell.cellValue
            val newValue = Random.nextInt(1, gridSize+1)
            val gridEvent = SetCellValueEvent(eventSource, newValue)
            // when, then
            assertFailsWith<IllegalStateException> { subject.handleSetCellValueEvent(gridEvent, segment) }
        }
    }

    @Test
    fun handleCellRemoveCandidatesEvent() {
        // TODO
    }
}