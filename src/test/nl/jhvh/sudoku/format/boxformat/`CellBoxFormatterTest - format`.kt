package nl.jhvh.sudoku.format.boxformat

import io.mockk.every
import io.mockk.spyk
import nl.jhvh.sudoku.grid.model.GridBuilder
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.cell.CellRef
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.text.RegexOption.DOT_MATCHES_ALL

/** Unit integration test for [Cell] formatting */
class `SimpleCellFormatterTest - format` {

    /**
     * * `A, B, C,` etc. denote rows (top row is `A` => y = 0)
     * * `1, 2, 3,` etc. denote columns (left column is `1`, => x = 0)
     * * So
     *     * `A1` corresponds with (x, y) = (0, 0),
     *     * `A2` corresponds with (x, y) = (1, 0),
     *     * ...
     *     * `B5` corresponds with (x, y) = (4, 1)
     *     * ... etc.
     *
     * Block size default = 3
     */
    private val grid9 = GridBuilder()
            .fix("A1", 7) // x = 0, y = 0
            .fix("B1", 4) // x = 0, y = 1
            .fix("B3", 2) // x = 2, y = 1
            .fix("C7", 8) // x = 6, y = 2
            .fix("I6", 3) // x = 5, y = 8
            .build()

    private val subject = CellBoxFormatter()

    @Test
    fun `test format top row of 9 Cells`() {
        // fixed value
        assertThat(subject.format(grid9.findCell(0, 0)).toString()).isEqualTo(
                """
                   ╔═══╤
                   ║►7 │
                   ╟───┼""".tidy())

        // no value
        assertThat(subject.format(grid9.findCell(1, 0)).toString()).isEqualTo(
                """
                   ╤═══╤
                   │   │
                   ┼───┼""".tidy())

        // non-fixed value
        val cell2_0 = spyk(grid9.findCell(2, 0))
        every { cell2_0.cellValue.value } returns 6
        every { cell2_0.cellValue.isFixed } returns false
        every { cell2_0.cellValue.grid } returns grid9
        assertThat(subject.format(cell2_0).toString()).isEqualTo(
                """
                   ╤═══╦
                   │ 6 ║
                   ┼───╫""".tidy())

        assertThat(subject.format(grid9.findCell(3, 0)).toString()).isEqualTo(
                """
                   ╦═══╤
                   ║   │
                   ╫───┼""".tidy())

        assertThat(subject.format(grid9.findCell(4, 0)).toString()).isEqualTo(
                """
                   ╤═══╤
                   │   │
                   ┼───┼""".tidy())

        val cell5_0 = spyk(grid9.findCell(2, 0))
        every { cell5_0.cellValue.value } returns 7
        every { cell5_0.cellValue.isFixed } returns false
        every { cell5_0.cellValue.grid } returns grid9
        assertThat(subject.format(cell5_0).toString()).isEqualTo(
                """
                   ╤═══╦
                   │ 7 ║
                   ┼───╫""".tidy())

        assertThat(subject.format(grid9.findCell(6, 0)).toString()).isEqualTo(
                """
                   ╦═══╤
                   ║   │
                   ╫───┼""".tidy())

        assertThat(subject.format(grid9.findCell(7, 0)).toString()).isEqualTo(
                """
                   ╤═══╤
                   │   │
                   ┼───┼""".tidy())

        assertThat(subject.format(grid9.findCell(8, 0)).toString()).isEqualTo(
                """
                   ╤═══╗
                   │   ║
                   ┼───╢""".tidy())

    }


    @Test
    fun `test format of Cells - 4*4 Grid`() {
        /** Blocksize 2, so `A..D`, `1..4`. Nothing fixed */
        val grid4 = GridBuilder(2)
                .fix("C2", 4) // x = 1, y = 2
                .fix("B3", 1) // x = 2, y = 1
                .build()

        // Top cells
        assertThat(subject.format(grid4.findCell(0, 0)).toString()).isEqualTo(
                """
                   ╔═══╤
                   ║   │
                   ╟───┼""".tidy())

        assertThat(subject.format(grid4.findCell(1, 0)).toString()).isEqualTo(
                """
                   ╤═══╦
                   │   ║
                   ┼───╫""".tidy())

        assertThat(subject.format(grid4.findCell(2, 0)).toString()).isEqualTo(
                """
                   ╦═══╤
                   ║   │
                   ╫───┼""".tidy())

        assertThat(subject.format(grid4.findCell(3, 0)).toString()).isEqualTo(
                """
                   ╤═══╗
                   │   ║
                   ┼───╢""".tidy())

        // Bottom cells
        assertThat(subject.format(grid4.findCell(0, 3)).toString()).isEqualTo(
                """
                   ╟───┼
                   ║   │
                   ╚═══╧""".tidy())

        // no value
        assertThat(subject.format(grid4.findCell(1, 3)).toString()).isEqualTo(
                """
                   ┼───╫
                   │   ║
                   ╧═══╩""".tidy())

        assertThat(subject.format(grid4.findCell(2, 3)).toString()).isEqualTo(
                """
                   ╫───┼
                   ║   │
                   ╩═══╧""".tidy())

        assertThat(subject.format(grid4.findCell(3, 3)).toString()).isEqualTo(
                """
                   ┼───╢
                   │   ║
                   ╧═══╝""".tidy())
        
        // Left cells
        assertThat(subject.format(grid4.findCell(0, 1)).toString()).isEqualTo(
                """
                   ╟───┼
                   ║   │
                   ╠═══╪""".tidy())

        assertThat(subject.format(grid4.findCell(0, 2)).toString()).isEqualTo(
                """
                   ╠═══╪
                   ║   │
                   ╟───┼""".tidy())
        
        // Right cells
        assertThat(subject.format(grid4.findCell(3, 1)).toString()).isEqualTo(
                """
                   ┼───╢
                   │   ║
                   ╪═══╣""".tidy())

        assertThat(subject.format(grid4.findCell(3, 2)).toString()).isEqualTo(
                """
                   ╪═══╣
                   │   ║
                   ┼───╢""".tidy())

        // Non-gridborder cells
        assertThat(subject.format(grid4.findCell(1, 1)).toString()).isEqualTo(
                """
                   ┼───╫
                   │   ║
                   ╪═══╬""".tidy())

        assertThat(subject.format(grid4.findCell(1, 2)).toString()).isEqualTo(
                """
                   ╪═══╬
                   │►4 ║
                   ┼───╫""".tidy())

        assertThat(subject.format(grid4.findCell(2, 1)).toString()).isEqualTo(
                """
                   ╫───┼
                   ║►1 │
                   ╬═══╪""".tidy())

        assertThat(subject.format(grid4.findCell(2, 2)).toString()).isEqualTo(
                """
                   ╬═══╪
                   ║   │
                   ╫───┼""".tidy())

    }

    @Test
    fun `test format bottom row of 9 Cells`() {
        assertThat(subject.format(grid9.findCell(0, 8)).toString()).isEqualTo(
                """
                   ╟───┼
                   ║   │
                   ╚═══╧""".tidy())

        // no value
        assertThat(subject.format(grid9.findCell(1, 8)).toString()).isEqualTo(
                """
                   ┼───┼
                   │   │
                   ╧═══╧""".tidy())

        assertThat(subject.format(grid9.findCell(2, 8)).toString()).isEqualTo(
                """
                   ┼───╫
                   │   ║
                   ╧═══╩""".tidy())

        // non-fixed value
        val cell3_8 = spyk(grid9.findCell(3, 8))
        every { cell3_8.cellValue.value } returns 4
        every { cell3_8.cellValue.isFixed } returns false
        every { cell3_8.cellValue.grid } returns grid9
        assertThat(subject.format(cell3_8).toString()).isEqualTo(
                """
                   ╫───┼
                   ║ 4 │
                   ╩═══╧""".tidy())

        assertThat(subject.format(grid9.findCell(4, 8)).toString()).isEqualTo(
                """
                   ┼───┼
                   │   │
                   ╧═══╧""".tidy())

        assertThat(subject.format(grid9.findCell(5, 8)).toString()).isEqualTo(
                """
                   ┼───╫
                   │►3 ║
                   ╧═══╩""".tidy())

        assertThat(subject.format(grid9.findCell(6, 8)).toString()).isEqualTo(
                """
                   ╫───┼
                   ║   │
                   ╩═══╧""".tidy())

        assertThat(subject.format(grid9.findCell(7, 8)).toString()).isEqualTo(
                """
                   ┼───┼
                   │   │
                   ╧═══╧""".tidy())

        assertThat(subject.format(grid9.findCell(8, 8)).toString()).isEqualTo(
                """
                   ┼───╢
                   │   ║
                   ╧═══╝""".tidy())


    }

    @Test
    fun `test format left column of 9 Cells`() {
        /* >> top and bottom cells of the column already tested in other methods */
        // fixed value
        assertThat(subject.format(grid9.findCell(0, 1)).toString()).isEqualTo(
                """
                   ╟───┼
                   ║►4 │
                   ╟───┼""".tidy())

        assertThat(subject.format(grid9.findCell(0, 2)).toString()).isEqualTo(
                """
                   ╟───┼
                   ║   │
                   ╠═══╪""".tidy())

        // non-fixed value
        val cell0_3 = spyk(grid9.findCell(0, 3))
        every { cell0_3.cellValue.value } returns 6
        every { cell0_3.cellValue.isFixed } returns false
        every { cell0_3.cellValue.grid } returns grid9
        assertThat(subject.format(cell0_3).toString()).isEqualTo(
                """
                   ╠═══╪
                   ║ 6 │
                   ╟───┼""".tidy())

        assertThat(subject.format(grid9.findCell(0, 4)).toString()).isEqualTo(
                """
                   ╟───┼
                   ║   │
                   ╟───┼""".tidy())

        val cell0_5 = spyk(grid9.findCell(0, 5))
        every { cell0_5.cellValue.value } returns 7
        every { cell0_5.cellValue.isFixed } returns false
        every { cell0_5.cellValue.grid } returns grid9
        assertThat(subject.format(cell0_5).toString()).isEqualTo(
                """
                   ╟───┼
                   ║ 7 │
                   ╠═══╪""".tidy())

        assertThat(subject.format(grid9.findCell(0, 6)).toString()).isEqualTo(
                """
                   ╠═══╪
                   ║   │
                   ╟───┼""".tidy())

        assertThat(subject.format(grid9.findCell(0, 7)).toString()).isEqualTo(
                """
                   ╟───┼
                   ║   │
                   ╟───┼""".tidy())

    }

    @Test
    fun `test format right column of 9 Cells`() {
        /* >> top and bottom cells of the column already tested in other methods */
        // no value
        assertThat(subject.format(grid9.findCell(8, 1)).toString()).isEqualTo(
                """
                   ┼───╢
                   │   ║
                   ┼───╢""".tidy())

        // non-fixed value
        val cell8_2 = spyk(grid9.findCell(8, 2))
        every { cell8_2.cellValue.value } returns 4
        every { cell8_2.cellValue.isFixed } returns false
        every { cell8_2.cellValue.grid } returns grid9
        assertThat(subject.format(cell8_2).toString()).isEqualTo(
                """
                   ┼───╢
                   │ 4 ║
                   ╪═══╣""".tidy())

        assertThat(subject.format(grid9.findCell(8, 3)).toString()).isEqualTo(
                """
                   ╪═══╣
                   │   ║
                   ┼───╢""".tidy())

        assertThat(subject.format(grid9.findCell(8, 4)).toString()).isEqualTo(
                """
                   ┼───╢
                   │   ║
                   ┼───╢""".tidy())

        val cell8_5 = spyk(grid9.findCell(8, 5))
        every { cell8_5.cellValue.value } returns 3
        every { cell8_5.cellValue.isFixed } returns false
        every { cell8_5.cellValue.grid } returns grid9
        assertThat(subject.format(cell8_5).toString()).isEqualTo(
                """
                   ┼───╢
                   │ 3 ║
                   ╪═══╣""".tidy())

        assertThat(subject.format(grid9.findCell(8, 6)).toString()).isEqualTo(
                """
                   ╪═══╣
                   │   ║
                   ┼───╢""".tidy())

        assertThat(subject.format(grid9.findCell(8, 7)).toString()).isEqualTo(
                """
                   ┼───╢
                   │   ║
                   ┼───╢""".tidy())

    }

    @Test
    fun `test format non-gridborder Cells - 9*9 Grid`() {
        val grid = GridBuilder().build() // no values fixed or set yet

        // This test should be exhaustive for all non-gridborder Cells.
        // There should be 49 of these (7*7, as we skip the outer borders of the 9*9 grid)
        val expectedTestedCellCount = 49
        val tested: MutableSet<Pair<Int, Int>> = HashSet()

        // no block borders, so single-line on all sides
        for (x in listOf(1, 4, 7)) {
            for (y in listOf(1, 4, 7)) {
                tested.add(Pair(x, y))
                val cellSpyk = spyk(grid.findCell(x, y))
                assertThat(subject.format(cellSpyk).toString())
                        .`as`("Error in format for (x,y) = ($x, $y)")
                        .isEqualTo("""
                                      ┼───┼
                                      │   │
                                      ┼───┼""".tidy())
            }
        }
        // block border on left side, so double line at left side
        for (x in listOf(3, 6)) {
            for (y in listOf(1, 4, 7)) {
                tested.add(Pair(x, y))
                val cellSpyk = spyk(grid.findCell(x, y))
                assertThat(subject.format(cellSpyk).toString())
                        .`as`("Error in format for (x,y) = ($x, $y)")
                        .isEqualTo("""
                                      ╫───┼
                                      ║   │
                                      ╫───┼""".tidy())
            }
        }
        // block border on right side, so double line at right side
        for (x in listOf(2, 5)) {
            for (y in listOf(1, 4, 7)) {
                tested.add(Pair(x, y))
                val cellSpyk = spyk(grid.findCell(x, y))
                assertThat(subject.format(cellSpyk).toString())
                        .`as`("Error in format for (x,y) = ($x, $y)")
                        .isEqualTo("""
                                      ┼───╫
                                      │   ║
                                      ┼───╫""".tidy())
            }
        }
        // block border on top side, so double line at top
        for (x in listOf(1, 4, 7)) {
            for (y in listOf(3, 6)) {
                tested.add(Pair(x, y))
                val cellSpyk = spyk(grid.findCell(x, y))
                assertThat(subject.format(cellSpyk).toString())
                        .`as`("Error in format for (x,y) = ($x, $y)")
                        .isEqualTo("""
                                      ╪═══╪
                                      │   │
                                      ┼───┼""".tidy())
            }
        }
        // block border on bottom side, so double line at bottom
        for (x in listOf(1, 4, 7)) {
            for (y in listOf(2, 5)) {
                tested.add(Pair(x, y))
                val cellSpyk = spyk(grid.findCell(x, y))
                assertThat(subject.format(cellSpyk).toString())
                        .`as`("Error in format for (x,y) = ($x, $y)")
                        .isEqualTo("""
                                      ┼───┼
                                      │   │
                                      ╪═══╪""".tidy())
            }
        }
        // block border on top and left side, so double lines there
        for (x in listOf(3, 6)) {
            for (y in listOf(3, 6)) {
                tested.add(Pair(x, y))
                val cellSpyk = spyk(grid.findCell(x, y))
                assertThat(subject.format(cellSpyk).toString())
                        .`as`("Error in format for (x,y) = ($x, $y)")
                        .isEqualTo("""
                                      ╬═══╪
                                      ║   │
                                      ╫───┼""".tidy())
            }
        }
        // block border on top and right side, so double lines there
        for (x in listOf(2, 5)) {
            for (y in listOf(3, 6)) {
                tested.add(Pair(x, y))
                val cellSpyk = spyk(grid.findCell(x, y))
                assertThat(subject.format(cellSpyk).toString())
                        .`as`("Error in format for (x,y) = ($x, $y)")
                        .isEqualTo("""
                                      ╪═══╬
                                      │   ║
                                      ┼───╫""".tidy())
            }
        }
        // block border on bottom and left side, so double lines there
        for (x in listOf(3, 6)) {
            for (y in listOf(2, 5)) {
                tested.add(Pair(x, y))
                val cellSpyk = spyk(grid.findCell(x, y))
                assertThat(subject.format(cellSpyk).toString())
                        .`as`("Error in format for (x,y) = ($x, $y)")
                        .isEqualTo("""
                                      ╫───┼
                                      ║   │
                                      ╬═══╪""".tidy())
            }
        }
        // block border on bottom and right side, so double lines there
        for (x in listOf(2, 5)) {
            for (y in listOf(2, 5)) {
                tested.add(Pair(x, y))
                val cellSpyk = spyk(grid.findCell(x, y))
                assertThat(subject.format(cellSpyk).toString())
                        .`as`("Error in format for (x,y) = ($x, $y)")
                        .isEqualTo("""
                                      ┼───╫
                                      │   ║
                                      ╪═══╬""".tidy())
            }
        }
        assertThat(tested).hasSize(expectedTestedCellCount)
    }

    @Test
    fun `test format Cells of 16*16 Grid`() {
        // Rows are A..P, columns are 1..16
        val grid16 = GridBuilder(4)
                // Fixed values
                .fix("P16", 16) // x = 15, y = 15
                .fix("A8" , 14) // x =  7, y =  0
                .fix("B12",  1) // x = 11, y =  1
                .fix(CellRef(5, 10), 7)
                .build()

        // Non-fixed values
        grid16.findCell( 0,  0).cellValue.setValue(3)
        grid16.findCell(10, 12).cellValue.setValue(8)
        grid16.findCell(11,  9).cellValue.setValue(7)
        grid16.findCell(10, 10).cellValue.setValue(10)
        grid16.findCell(3,   7).cellValue.setValue(10)

        assertThat(subject.format(grid16.findCell(0, 0)).toString()).isEqualTo(
                """
                   ╔═══╤
                   ║ 3 │
                   ╟───┼""".tidy())
        assertThat(subject.format(grid16.findCell(10, 10)).toString()).isEqualTo(
                """
                   ┼───┼
                   │ 10│
                   ┼───┼""".tidy())
        assertThat(subject.format(grid16.findCell(15, 15)).toString()).isEqualTo(
                """
                   ┼───╢
                   │►16║
                   ╧═══╝""".tidy())

        // assert that the fixed indicator and value are correctly parsed
        val expectedFixedValues = 4
        var foundFixedValues = 0
        grid16.cellList.filter { it.cellValue.isFixed }.forEach {
            foundFixedValues++
            val formatted = subject.format(it).toString()
            assertTrue(formatted.matches(Regex("""[^0-9]+?►${it.cellValue.value}[^0-9]+""", DOT_MATCHES_ALL)),
                    "Fixed indicator [►] and/or cell value [${it.cellValue.value}] not found in formatted Cell (x=${it.rowIndex}, y=${it.colIndex})" +
                            " formatted value = \n$formatted"
            )
        }
        assertThat(foundFixedValues).isEqualTo(expectedFixedValues)

        val expectedNonFixedValues = 5
        var foundNonFixedValues = 0
        // assert that the fixed indicator and value are correctly parsed
        grid16.cellList.filter {it.cellValue.value != null && !it.cellValue.isFixed }.forEach {
            foundNonFixedValues++
            val formatted = subject.format(it).toString()
            assertTrue(formatted.matches(Regex("""[^0-9]+?${it.cellValue.value}[^0-9]+""", DOT_MATCHES_ALL)),
                    "Cell value [${it.cellValue.value}] not found in formatted Cell (x=${it.rowIndex}, y=${it.colIndex})" +
                            " formatted value = \n$formatted"
            )
        }
        assertThat(foundNonFixedValues).isEqualTo(expectedNonFixedValues)
    }
}
