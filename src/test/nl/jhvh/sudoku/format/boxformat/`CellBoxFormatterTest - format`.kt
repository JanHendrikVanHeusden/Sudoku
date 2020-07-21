package nl.jhvh.sudoku.format.boxformat

import io.mockk.every
import io.mockk.spyk
import nl.jhvh.sudoku.grid.model.GridBuilder
import nl.jhvh.sudoku.grid.model.cell.Cell
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/** Unit integration test for [Cell] formatting */
class `SimpleCellFormatterTest - format` {

    /**
     * A, B, C etc. denote rows (top row is A); 1, 2, 3 etc. denote columns (left row is 1)
     * A1 corresponds with (x, y) = (0, 0), B3 corresponds with (x, y) = (1, 2), etc.
     */
    private val grid = GridBuilder()
            .fix("A1", 7)
            .fix("B1", 4)
            .fix("B3", 2)
            .fix("C7", 8)
            .fix("I6", 3)
            .build()
    private val formatter = SudokuBoxFormatter()

    @Test
    fun `test format of top row Cells`() {
        // fixed value
        assertThat(formatter.format(grid.findCell(0, 0)).toString()).isEqualTo(
                """
                   ╔═══╤
                   ║►7 │
                   ╟───┼""".tidy())
        // no value
        assertThat(formatter.format(grid.findCell(1, 0)).toString()).isEqualTo(
                """
                   ╤═══╤
                   │   │
                   ┼───┼""".tidy())
        // non-fixed value
        val cell2_0 = spyk(grid.findCell(2, 0))
        every { cell2_0.cellValue.value } returns 6
        every { cell2_0.cellValue.isFixed } returns false
        assertThat(formatter.format(cell2_0).toString()).isEqualTo(
                """
                   ╤═══╦
                   │ 6 ║
                   ┼───╫""".tidy())
        assertThat(formatter.format(grid.findCell(3, 0)).toString()).isEqualTo(
                """
                   ╦═══╤
                   ║   │
                   ╫───┼""".tidy())
        assertThat(formatter.format(grid.findCell(4, 0)).toString()).isEqualTo(
                """
                   ╤═══╤
                   │   │
                   ┼───┼""".tidy())

        val cell5_0 = spyk(grid.findCell(2, 0))
        every { cell5_0.cellValue.value } returns 7
        every { cell5_0.cellValue.isFixed } returns false
        assertThat(formatter.format(cell5_0).toString()).isEqualTo(
                """
                   ╤═══╦
                   │ 7 ║
                   ┼───╫""".tidy())
        assertThat(formatter.format(grid.findCell(6, 0)).toString()).isEqualTo(
                """
                   ╦═══╤
                   ║   │
                   ╫───┼""".tidy())
        assertThat(formatter.format(grid.findCell(7, 0)).toString()).isEqualTo(
                """
                   ╤═══╤
                   │   │
                   ┼───┼""".tidy())
        assertThat(formatter.format(grid.findCell(8, 0)).toString()).isEqualTo(
                """
                   ╤═══╗
                   │   ║
                   ┼───╢""".tidy())
    }

    @Test
    fun `test format of bottom row Cells`() {
        assertThat(formatter.format(grid.findCell(0, 8)).toString()).isEqualTo(
                """
                   ╟───┼
                   ║   │
                   ╚═══╧""".tidy())
        // no value
        assertThat(formatter.format(grid.findCell(1, 8)).toString()).isEqualTo(
                """
                   ┼───┼
                   │   │
                   ╧═══╧""".tidy())
        assertThat(formatter.format(grid.findCell(2, 8)).toString()).isEqualTo(
                """
                   ┼───╫
                   │   ║
                   ╧═══╩""".tidy())

        // non-fixed value
        val cell3_8 = spyk(grid.findCell(3, 8))
        every { cell3_8.cellValue.value } returns 4
        every { cell3_8.cellValue.isFixed } returns false
        assertThat(formatter.format(cell3_8).toString()).isEqualTo(
                """
                   ╫───┼
                   ║ 4 │
                   ╩═══╧""".tidy())
        assertThat(formatter.format(grid.findCell(4, 8)).toString()).isEqualTo(
                """
                   ┼───┼
                   │   │
                   ╧═══╧""".tidy())
        assertThat(formatter.format(grid.findCell(5, 8)).toString()).isEqualTo(
                """
                   ┼───╫
                   │►3 ║
                   ╧═══╩""".tidy())
        assertThat(formatter.format(grid.findCell(6, 8)).toString()).isEqualTo(
                """
                   ╫───┼
                   ║   │
                   ╩═══╧""".tidy())
        assertThat(formatter.format(grid.findCell(7, 8)).toString()).isEqualTo(
                """
                   ┼───┼
                   │   │
                   ╧═══╧""".tidy())
        assertThat(formatter.format(grid.findCell(8, 8)).toString()).isEqualTo(
                """
                   ┼───╢
                   │   ║
                   ╧═══╝""".tidy())
    }
}
