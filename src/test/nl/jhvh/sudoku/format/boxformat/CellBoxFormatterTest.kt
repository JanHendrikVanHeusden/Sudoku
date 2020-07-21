package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.lineSeparator
import nl.jhvh.sudoku.grid.model.GridBuilder
import nl.jhvh.sudoku.grid.model.cell.Cell
import org.junit.jupiter.api.Test

/** Unit integration test for [Cell] formatting */
class CellBoxFormatterTest {

    /**
     * A, B, C etc. denote rows (top row is A); 1, 2, 3 etc. denote columns (left row is 1)
     * A1 corresponds with (x, y) = (0, 0), B3 corresponds with (x, y) = (1, 2), etc.
     */
    private val grid = GridBuilder()
            .fix("A1", 7)
            .fix("B3", 2)
            .fix("C7", 8)
            .fix("H6", 3)
            .build()
    private val formatter = SudokuBoxFormatter()

    private fun String.tidy() = this.trimIndent().replace(Regex("""([\r\n])+"""), lineSeparator)

    @Test
    fun nakedFormat() {
    }

    @Test
    fun getLeftBorder() {
    }

    @Test
    fun getRightBorder() {
    }

    @Test
    fun getTopBorder() {
    }

    @Test
    fun getBottomBorder() {
    }

    @Test
    fun getTopLeftEdge() {
    }

    @Test
    fun getTopRightEdge() {
    }

    @Test
    fun getBottomLeftEdge() {
    }

    @Test
    fun getBottomRightEdge() {
    }
}
