package nl.jhvh.sudoku.format.boxformat

import io.mockk.clearAllMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.simple.SimpleCellValueFormatter
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.cell.CellValue
import nl.jhvh.sudoku.grid.model.segment.Block
import nl.jhvh.sudoku.grid.model.segment.Col
import nl.jhvh.sudoku.grid.model.segment.Row
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Unit tests for [SudokuBoxFormatter]. That class delegates all it's work to formatters for each element,
 * so the tests only check that the formatting is correctly delegated.
 * The delegates have their own tests, so no functional tests needed here.
 */
internal class SudokuBoxFormatterTest {

    private lateinit var subject: SudokuBoxFormatter

    // mocks for the delegates
    private lateinit var cellValueFormatterDelegate: SimpleCellValueFormatter
    private lateinit var cellFormatterDelegate: CellBoxFormatter
    private lateinit var rowFormatterDelegate: RowBoxFormatter
    private lateinit var colFormatterDelegate: ColumnBoxFormatter
    private lateinit var blockFormatterDelegate: BlockBoxFormatter
    private lateinit var gridFormatterDelegate: GridBoxFormatter

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @BeforeEach
    fun setUp() {
        cellValueFormatterDelegate = mockk()
        cellFormatterDelegate = mockk()
        rowFormatterDelegate = mockk()
        colFormatterDelegate = mockk()
        blockFormatterDelegate = mockk()
        gridFormatterDelegate = mockk()

        val formatterFactoryMock: BoxFormatterFactory = mockk(relaxed = false)
        every { formatterFactoryMock.cellValueFormatterInstance } returns cellValueFormatterDelegate
        every { formatterFactoryMock.cellBoxFormatterInstance } returns cellFormatterDelegate
        every { formatterFactoryMock.rowBoxFormatterInstance } returns rowFormatterDelegate
        every { formatterFactoryMock.columnBoxFormatterInstance } returns colFormatterDelegate
        every { formatterFactoryMock.blockBoxFormatterInstance } returns blockFormatterDelegate
        every { formatterFactoryMock.gridBoxFormatterInstance } returns gridFormatterDelegate

        subject = SudokuBoxFormatter(formatterFactoryMock)
    }

    @Test
    fun `format - CellValue`() {
        // given
        val cellValueFormatResult = FormattableList(listOf("mockedFormattedCellValue"))
        val cellValueMock: CellValue = mockk()
        every {cellValueFormatterDelegate.format(cellValueMock)} returns cellValueFormatResult
        // when
        val formatResult = subject.format(cellValueMock)
        // then
        assertThat(formatResult).isEqualTo(cellValueFormatResult)
        verify (exactly = 1) { cellValueFormatterDelegate.format(cellValueMock) }
        confirmVerified(cellValueFormatterDelegate, cellValueMock)
    }

    @Test
    fun `format - Cell`() {
        // given
        val cellFormatResult = FormattableList(listOf("mockedFormattedCell"))
        val cellMock: Cell = mockk()
        every {cellFormatterDelegate.format(cellMock)} returns cellFormatResult
        // when
        val formatResult = subject.format(cellMock)
        // then
        assertThat(formatResult).isEqualTo(cellFormatResult)
        verify (exactly = 1) { cellFormatterDelegate.format(cellMock) }
        confirmVerified(cellFormatterDelegate, cellMock)
    }

    @Test
    fun `format - Row`() {
        // given
        val rowFormatResult = FormattableList(listOf("mockedFormattedRow"))
        val rowMock: Row = mockk()
        every {rowFormatterDelegate.format(rowMock)} returns rowFormatResult
        // when
        val formatResult = subject.format(rowMock)
        // then
        assertThat(formatResult).isEqualTo(rowFormatResult)
        verify (exactly = 1) { rowFormatterDelegate.format(rowMock) }
        confirmVerified(rowFormatterDelegate, rowMock)
    }

    @Test
    fun `format - Column`() {
        // given
        val colFormatResult = FormattableList(listOf("mockedFormattedCol"))
        val colMock: Col = mockk()
        every {colFormatterDelegate.format(colMock)} returns colFormatResult
        // when
        val formatResult = subject.format(colMock)
        // then
        assertThat(formatResult).isEqualTo(colFormatResult)
        verify (exactly = 1) { colFormatterDelegate.format(colMock) }
        confirmVerified(colFormatterDelegate, colMock)
    }

    @Test
    fun `format - Block`() {
        // given
        val blockFormatResult = FormattableList(listOf("mockedFormattedBlock"))
        val blockMock: Block = mockk()
        every {blockFormatterDelegate.format(blockMock)} returns blockFormatResult
        // when
        val formatResult = subject.format(blockMock)
        // then
        assertThat(formatResult).isEqualTo(blockFormatResult)
        verify (exactly = 1) { blockFormatterDelegate.format(blockMock) }
        confirmVerified(blockFormatterDelegate, blockMock)
    }

    @Test
    fun `format - Grid`() {
        // given
        val gridFormatResult = FormattableList(listOf("mockedFormattedGrid"))
        val gridMock: Grid = mockk()
        every {gridFormatterDelegate.format(gridMock)} returns gridFormatResult
        // when
        val formatResult = subject.format(gridMock)
        // then
        assertThat(formatResult).isEqualTo(gridFormatResult)
        verify (exactly = 1) { gridFormatterDelegate.format(gridMock) }
        confirmVerified(gridFormatterDelegate, gridMock)
    }

}
