package nl.jhvh.sudoku.grid.model.segment

import io.mockk.every
import nl.jhvh.sudoku.base.incrementFromZero
import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.event.cellvalue.CellSetValueEvent
import nl.jhvh.sudoku.grid.model.Grid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class GridSegmentTest : GridSegmentTestBase() {

    /** grid mock and cell mocks initialized in [GridSegmentTestBase.gridSetUp] */
    override lateinit var gridMock: Grid
    override val blockSize = 3
    override val gridSize = 9

    private lateinit var subject: GridSegment

    @BeforeEach
    fun setUp() {
        subject = object : GridSegment(gridMock) {
            // the cells are mocks, initialized in
            override val cells = LinkedHashSet(incrementFromZero(gridSize).map { gridMock.findCell(colIndex = it, rowIndex = it) })
            override fun format(formatter: SudokuFormatter): FormattableList = FormattableList(emptyList())
        }
        subject.cells.forEach {cellMock ->
            every { cellMock.valueCandidates } returns HashSet(IntRange(1, gridSize).toList())
        }
    }

    @Test
    fun onEvent() {
        // given
        val newValue = Random.nextInt(1, gridSize+1)
        val valueSet = IntRange(1, gridSize).toSet()
        // precondition: for all cells assert that valueCandidates contains all values
        subject.cells.forEach { assertThat(it.valueCandidates).hasSameElementsAs(valueSet) }
        subject.cells.forEach { assertThat(it.valueCandidates).contains(newValue) }

        // grab a random cell of the GridSegment's cells
        val eventCell = subject.cells.toList()[Random.nextInt(0, gridSize)]
        // when
        subject.onEvent(CellSetValueEvent(eventCell, newValue))
        println()
        println()
        // then: for all cells assert that valueCandidates contains all values except the event's newValue
        subject.cells.forEach { assertThat(it.valueCandidates).doesNotContain(newValue) }
        subject.cells.forEach { assertThat(it.valueCandidates).hasSameElementsAs(valueSet.filter { it != newValue }) }
    }
}