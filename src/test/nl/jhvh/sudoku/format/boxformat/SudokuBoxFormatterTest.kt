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

/** Unit tests for [SudokuBoxFormatter] */
internal class SudokuBoxFormatterTest {

    private lateinit var subject: SudokuBoxFormatter

    private lateinit var cellValueFormatterMock: SimpleCellValueFormatter
    private lateinit var cellFormatterMock: CellBoxFormatter
    private lateinit var rowFormatterMock: RowBoxFormatter
    private lateinit var colFormatterMock: ColumnBoxFormatter
    private lateinit var blockFormatterMock: BlockBoxFormatter
    private lateinit var gridFormatterMock: GridBoxFormatter

    private lateinit var gridMock: Grid

    @AfterEach
    fun tearDownAfterAll() {
        clearAllMocks()
    }

    @BeforeEach
    fun setUp() {
        cellValueFormatterMock = mockk()
        cellFormatterMock = mockk()
        rowFormatterMock = mockk()
        colFormatterMock = mockk()
        blockFormatterMock = mockk()
        gridFormatterMock = mockk()

        val formatterFactory: BoxFormatterFactory = mockk(relaxed = false)
        every { formatterFactory.simpleCellValueFormatterInstance } returns cellValueFormatterMock
        every { formatterFactory.cellBoxFormatterInstance } returns cellFormatterMock
        every { formatterFactory.rowBoxFormatterInstance } returns rowFormatterMock
        every { formatterFactory.columnBoxFormatterInstance } returns colFormatterMock
        every { formatterFactory.blockBoxFormatterInstance } returns blockFormatterMock
        every { formatterFactory.gridBoxFormatterInstance } returns gridFormatterMock

        subject = SudokuBoxFormatter(formatterFactory)
    }

    @Test
    fun `format - CellValue`() {
        // given
        val mockedFormattedCellValue = "vmk" // same length as actual formatted value
        val cellValueFormatResult = FormattableList(listOf(mockedFormattedCellValue))

        val cellValueMock: CellValue = mockk()
        every {cellValueFormatterMock.format(cellValueMock)} returns cellValueFormatResult

        // when
        val formatResult = subject.format(cellValueMock)
        // then
        assertThat(formatResult).isEqualTo(cellValueFormatResult)
        verify (exactly = 1) { cellValueFormatterMock.format(cellValueMock) }
        confirmVerified(cellValueFormatterMock, cellValueMock)
    }

    @Test
    fun `format - Cell`() {
        // given
        val mockedFormattedCell = "cmk"
        val cellFormatResult = FormattableList(listOf(mockedFormattedCell))

        val cellMock: Cell = mockk()
        every {cellFormatterMock.format(cellMock)} returns cellFormatResult

        // when
        val formatResult = subject.format(cellMock)
        // then
        assertThat(formatResult).isEqualTo(cellFormatResult)
        verify (exactly = 1) { cellFormatterMock.format(cellMock) }
        confirmVerified(cellFormatterMock, cellMock)
    }

    @Test
    fun `format - Row`() {
        // given
        val mockedFormattedRow = "rmk"
        val rowFormatResult = FormattableList(listOf(mockedFormattedRow))

        val rowMock: Row = mockk()
        every {rowFormatterMock.format(rowMock)} returns rowFormatResult

        // when
        val formatResult = subject.format(rowMock)
        // then
        assertThat(formatResult).isEqualTo(rowFormatResult)
        verify (exactly = 1) { rowFormatterMock.format(rowMock) }
        confirmVerified(rowFormatterMock, rowMock)
    }

    @Test
    fun `format - Column`() {
        // given
        val mockedFormattedCol = "clm"
        val colFormatResult = FormattableList(listOf(mockedFormattedCol))

        val colMock: Col = mockk()
        every {colFormatterMock.format(colMock)} returns colFormatResult

        // when
        val formatResult = subject.format(colMock)
        // then
        assertThat(formatResult).isEqualTo(colFormatResult)
        verify (exactly = 1) { colFormatterMock.format(colMock) }
        confirmVerified(colFormatterMock, colMock)
    }

    @Test
    fun `format - Block`() {
        // given
        val mockedFormattedBlock = "clm"
        val blockFormatResult = FormattableList(listOf(mockedFormattedBlock))

        val blockMock: Block = mockk()
        every {blockFormatterMock.format(blockMock)} returns blockFormatResult

        // when
        val formatResult = subject.format(blockMock)
        // then
        assertThat(formatResult).isEqualTo(blockFormatResult)
        verify (exactly = 1) { blockFormatterMock.format(blockMock) }
        confirmVerified(blockFormatterMock, blockMock)
    }

    @Test
    fun `format - Grid`() {
        // given
        val mockedFormattedGrid = "clm"
        val gridFormatResult = FormattableList(listOf(mockedFormattedGrid))

        val gridMock: Grid = mockk()
        every {gridFormatterMock.format(gridMock)} returns gridFormatResult

        // when
        val formatResult = subject.format(gridMock)
        // then
        assertThat(formatResult).isEqualTo(gridFormatResult)
        verify (exactly = 1) { gridFormatterMock.format(gridMock) }
        confirmVerified(gridFormatterMock, gridMock)
    }

}
