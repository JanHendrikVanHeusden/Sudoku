package nl.jhvh.sudoku.grid.solve

import io.mockk.CapturingSlot
import io.mockk.clearAllMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import nl.jhvh.sudoku.grid.GridWithCellsAndSegmentsTestBase
import nl.jhvh.sudoku.grid.event.ValueEventType
import nl.jhvh.sudoku.grid.event.ValueEventType.CELL_REMOVE_CANDIDATES
import nl.jhvh.sudoku.grid.event.ValueEventType.SET_CELL_VALUE
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.Grid.GridBuilder
import nl.jhvh.sudoku.grid.model.cell.CellRef
import nl.jhvh.sudoku.grid.model.cell.CellValue
import nl.jhvh.sudoku.grid.model.cell.CellValue.FixedValue
import nl.jhvh.sudoku.grid.model.cell.CellValue.NonFixedValue
import nl.jhvh.sudoku.grid.model.segment.Block
import nl.jhvh.sudoku.grid.model.segment.Col
import nl.jhvh.sudoku.grid.model.segment.GridSegment
import nl.jhvh.sudoku.grid.model.segment.Row
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Initialization for [GridSolver] grid mocks is computationally & performance wise relatively heavy.
 * For [GridSolver] tests that do not require this hefty initialization, see [GridSolverBasicTests]
 */
internal class GridSolverTest: GridWithCellsAndSegmentsTestBase(blockSize = 3) {

    @BeforeEach
    fun setUp() {
        // additional setup of the gridMock
        setUpFixedValues()
        setUpValueCandidates()
    }

    @Test
    fun `GridSolver initialization when Grid is set`() {
        // given - subclassed to make protected values reachable
        val subject = object : GridSolver() {
            fun subscribedToEvents() = subscribedToEvents
            fun preparedForSolving() = preparedForSolving
        }
        assertThat(subject.subscribedToEvents()).isFalse()
        assertThat(subject.preparedForSolving()).isFalse()
        // when
        subject.gridToSolve = gridMock
        // then
        assertThat(subject.subscribedToEvents()).isFalse()
        assertThat(subject.preparedForSolving()).isTrue()
        val expectedUnSolved = gridMock.cellList
                .filter { it.cellValue is NonFixedValue }
                .map { it.cellValue as NonFixedValue }
        assertThat(subject.getUnSolvedCellValues()).hasSameElementsAs(expectedUnSolved)
    }

    @Test
    fun `getUnSolvedCellValues - initially`() {
        // given - Calling getUnSolvedCellValues with a mocked Grid somehow throws GridNotSetException,
        // even if the gridToSolve it was actually set (and can be retrieved, printed etc.)
        // Not clear why??? Anyhow, we have to test this with a "real" non-mocked Grid
        val blockSize = 2
        val gridSize = blockSize * blockSize
        // Advantage of using real object: we can easily fix cells, and use findCell
        val fixedCellRefs = listOf(CellRef("A1"), CellRef("C3"))
        val grid2: Grid = GridBuilder(blockSize)
                .fix(fixedCellRefs[0], 2)
                .fix(fixedCellRefs[1], 3)
                .build()
        val fixedCells = fixedCellRefs.map { grid2.findCell(it) }.toSet()
        val subject = GridSolver()
        // when
        subject.gridToSolve = grid2
        // then
        val expectedUnSolved = grid2.cellList
                        .filter { !fixedCells.contains(it) }
                        .map { it.cellValue as NonFixedValue }
        val expectedUnSolvedCount = gridSize * gridSize - fixedCells.size
        with (subject.getUnSolvedCellValues()) {
            assertThat(this).size().isEqualTo(expectedUnSolvedCount)
            assertThat(this).hasSameElementsAs(expectedUnSolved)
        }
    }

    @Test
    fun segments() {
        // given - subclassed to make protected value reachable
        val subject = object : GridSolver() {
            fun segments() = segments
        }
        subject.gridToSolve = gridMock
        assertThat(subject.segments()).hasSize(27) // gridSize = 9, so 9 Rows, 9 Cols, 9 Blocks
        assertThat(subject.segments()).hasSameElementsAs(gridMock.rowList + gridMock.colList + gridMock.blockList)
    }

    @Test
    fun `setting the Grid to solve should perform further preparation & initialization of the GridSolver`() {
        // given
        val spiedSubject = spyk(GridSolver())
        assertThat(spiedSubject.gridToSolve).isNull()

        // when
        spiedSubject.gridToSolve = gridMock

        // then - assert that things are initialized to prepare the Grid can be solved
        assertThat(spiedSubject.gridToSolve === gridMock).isTrue()
        verify (exactly = 1) { spiedSubject setProperty gridToSolvePropertyName value gridMock }
        verify { spiedSubject.gridToSolve }
        confirmVerified(spiedSubject)

        // verify subscription to CellValues events etc.
        gridMock.cellList
                .map { it.cellValue }
                .forEach { cellValue ->

                    verify (exactly = 1) { cellValue.subscribe(spiedSubject, SET_CELL_VALUE) }
                    verify (atLeast = 1) { cellValue.isFixed }

                    if (cellValue is NonFixedValue) {
                        verify { cellValue.value }
                        verify { cellValue.cell }
                        confirmVerified(cellValue)
                    }
                }

        // verify that FixedValues publish their initial event
        // (publishing causes elimination of candidate values; a fixed value of 4 will remove all 4s in its row / column / block)
        gridMock.cellList
                .map { it.cellValue }
                .filter { it.isFixed }
                .forEach { fixedValue ->

                    verify (atLeast = 1) { fixedValue.isFixed }
                    verify (exactly = 1) { fixedValue.subscribe(spiedSubject, SET_CELL_VALUE) }

                    val eventCapturer: CapturingSlot<SetCellValueEvent> = slot()
                    verify (exactly = 1) { fixedValue.publish(capture(eventCapturer)) }
                    val event = eventCapturer.captured
                    assertThat(event.eventSource).isEqualTo(fixedValue)
                    assertThat(event.newValue).isEqualTo(fixedValue.value)

                    verify { fixedValue.value }
                    verify { fixedValue.cell }
                    confirmVerified(fixedValue)
                }
    }

    @Test
    fun segmentsByCellValue() {
        // given - subclassed to make protected values reachable
        val subject = object : GridSolver() {
            fun segmentsByCellValue() = segmentsByCellValue
        }
        subject.gridToSolve = gridMock

        // when
        val result: Map<CellValue, Set<GridSegment>> = subject.segmentsByCellValue()

        // then
        val expectedSegments = gridMock.rowList + gridMock.colList + gridMock.blockList
        val foundSegments = result.values.flatten().toSet()

        assertThat(result.keys).hasSameElementsAs(gridMock.cellList.map { it.cellValue })
        assertThat(foundSegments.toSet()).hasSameElementsAs(expectedSegments)

        result.keys.forEach { cellValue ->
            assertThat(result[cellValue]).hasSize(3) // every cellValue appears in one Row, one Col and one Block
            result[cellValue]!!.forEach { gridSegment ->
                assertThat(gridSegment.cells.map { cell -> cell.cellValue }).contains(cellValue)
                when (gridSegment) {
                    is Row -> { assertThat(cellValue.cell.rowIndex)
                            .`as`("Cell not in right Row! Row=$gridSegment, cellValue=$cellValue")
                            .isEqualTo(gridSegment.rowIndex) }
                    is Col -> { assertThat(cellValue.cell.colIndex)
                            .`as`("Cell not in right Col! Col=$gridSegment, cellValue=$cellValue")
                            .isEqualTo(gridSegment.colIndex) }
                    is Block -> { assertThat(cellValue.cell.rowIndex)
                            .`as`("Cell not in right Block! Block=$gridSegment, cellValue=$cellValue")
                            .isBetween(gridSegment.topRowIndex, gridSegment.bottomRowIndex )}
                }
            }
        }
    }

    @Test
    fun subscribeToEvents() {
        // subclassed to make protected methods reachable
        class GridSolverForTest: GridSolver() {
            // 4 overloads, so we can test both defaults and explicit values
            fun subscribeForTest() = subscribeToEvents()
            fun subscribeForTest(excludeFixed: Boolean, valueEventTypes: Array<ValueEventType>) =
                    subscribeToEvents(excludeFixed, *valueEventTypes)
            fun subscribeForTest(excludeFixed: Boolean) = subscribeToEvents(excludeFixed)
            fun subscribeForTest(valueEventTypes: Array<ValueEventType>) = subscribeToEvents(valueEventTypes = valueEventTypes)
        }
        fun constructSubject(): GridSolverForTest {
            val subject = GridSolverForTest()
            subject.gridToSolve = gridMock
            return subject
        }
        val allValueEventTypes = ValueEventType.values()
        var subject: GridSolverForTest

        // given
        subject = constructSubject()
        clearAllMocks(answers = false, recordedCalls = true)
        // when - all params explicitly specified, 1st param non-default
        subject.subscribeForTest(excludeFixed = false, allValueEventTypes)
        // then
        subject.gridToSolve!!.cellList.map { it.cellValue }
                .forEach { cellValue ->
                    allValueEventTypes.forEach {eventType ->
                        verify (exactly = 1) { cellValue.subscribe(subject, eventType) }
                    }
                    confirmVerified(cellValue)
                }

        // given
        subject = constructSubject()
        clearAllMocks(answers = false, recordedCalls = true)
        // when - all params default
        subject.subscribeForTest()
        // then
        subject.gridToSolve!!.cellList.map { it.cellValue }
                .forEach { cellValue ->
                    allValueEventTypes.forEach { eventType ->
                        if (!cellValue.isFixed) {
                            verify(exactly = 1) { cellValue.subscribe(subject, eventType) }
                        }
                    }
                    verify { cellValue.isFixed }
                    confirmVerified(cellValue)
                }

        // given
        subject = constructSubject()
        clearAllMocks(answers = false, recordedCalls = true)
        // when - all params explicitly specified, both params default values
        subject.subscribeForTest(excludeFixed = true, allValueEventTypes)
        // then
        subject.gridToSolve!!.cellList.map { it.cellValue }
                .forEach { cellValue ->
                    allValueEventTypes.forEach { eventType ->
                        if (!cellValue.isFixed) {
                            verify(exactly = 1) { cellValue.subscribe(subject, eventType) }
                        }
                    }
                    verify { cellValue.isFixed }
                    confirmVerified(cellValue)
                }

        // given
        subject = constructSubject()
        clearAllMocks(answers = false, recordedCalls = true)
        // when - excludeFixed specified: non default, so not excluding fixed ones
        subject.subscribeForTest(excludeFixed = false)
        // then
        subject.gridToSolve!!.cellList.map { it.cellValue }
                .forEach { cellValue ->
                    allValueEventTypes.forEach { eventType ->
                        verify(exactly = 1) { cellValue.subscribe(subject, eventType) }
                    }
                    confirmVerified(cellValue)
                }

        // given
        subject = constructSubject()
        clearAllMocks(answers = false, recordedCalls = true)
        // when - both params specified, both non-default
        val eventTypeSetCellValue = arrayOf(SET_CELL_VALUE)
        subject.subscribeForTest(excludeFixed = false, eventTypeSetCellValue)
        // then
        subject.gridToSolve!!.cellList.map { it.cellValue }
                .forEach { cellValue ->
                    verify (exactly = 1) { cellValue.subscribe(subject, SET_CELL_VALUE) }
                    confirmVerified(cellValue)
                }

        // given
        subject = constructSubject()
        clearAllMocks(answers = false, recordedCalls = true)
        // when - event type specified, non-default
        val eventTypeRemoveCandidatesEvent = arrayOf(CELL_REMOVE_CANDIDATES)
        subject.subscribeForTest(eventTypeRemoveCandidatesEvent)
        // then
        subject.gridToSolve!!.cellList.map { it.cellValue }
                .forEach { cellValue ->
                    if (!cellValue.isFixed) {
                        verify(exactly = 1) { cellValue.subscribe(subject, CELL_REMOVE_CANDIDATES) }
                    }
                    verify { cellValue.isFixed }
                    confirmVerified(cellValue)
                }
    }

    @Test
    fun handleSetCellValueEvent() {
        // given
        // given - subclassed to make protected values reachable
        val spiedSubject = spyk(object : GridSolver() {
            fun handleEvent(valueEvent: SetCellValueEvent) = handleSetCellValueEvent(valueEvent)
        })
        spiedSubject.gridToSolve = gridMock
        var eventSource = spiedSubject.gridToSolve!!.cellList.filter { it.isFixed } .random()
//        val valueEventSpyk = SetCellValueEvent()
        // todo
    }

    @Test
    fun handleRemoveCandidatesEvent() {
        // given
        // TODO
    }

    @Test
    fun solveGrid() {
        // given
        // TODO
    }

    @Test
    fun onEvent() {
        // given
    }

    private fun setUpFixedValues() {
        gridMock.cellList
                // This filter arbitrarily chooses all cells diagonally from top left to bottom right
                .filter { it.colIndex == it.rowIndex }
                .forEach { cellMock ->
                    val fixedValueMock: FixedValue = mockk(relaxed = true)
                    every { cellMock.cellValue } returns fixedValueMock
                    every { cellMock.isFixed } returns true
                    every { fixedValueMock.cell } returns cellMock
                    every { fixedValueMock.cell.rowIndex } returns cellMock.rowIndex
                    every { fixedValueMock.cell.colIndex } returns cellMock.colIndex
                    every { fixedValueMock.isFixed } returns true
                    every { fixedValueMock.cell.isFixed } returns true
                    every { fixedValueMock.isSet } returns true
                    // By choosing the rowIndex as value we are sure
                    // that we do not end up with conflicting fixed cell values
                    every { fixedValueMock.value } returns cellMock.rowIndex
                    every { fixedValueMock.toString() } returns "FixedValue mock;" +
                            " rowIndex=${fixedValueMock.cell.rowIndex}, colIndex=${fixedValueMock.cell.colIndex}, value=${fixedValueMock.value}"
                }
    }

    private fun setUpValueCandidates() {
        gridMock.cellList
                .map { it.cellValue }
                .filter { !it.isFixed }
                .map { it as NonFixedValue }
                .forEach { nonFixedValue ->
                    val valueCandidates = IntRange(1, gridMock.gridSize).toMutableSet()
                    every { nonFixedValue.getValueCandidates() } returns valueCandidates
                }
    }

}
