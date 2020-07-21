package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.grid.model.GridBuilder
import org.junit.jupiter.api.Test

/** Unit integration test for [Row] formatting */
internal class RowBoxFormatterTest {

    private val grid = GridBuilder()
            .fix("A3", 7)
            .fix("D3", 2)
            .fix("F7", 8)
            .fix("H6", 3)
            .build()
    private val formatter = RowBoxFormatter()

    @Test
    fun format() {
        TODO("Not implemented")
    }

    @Test
    fun nakedFormat() {
        println(formatter.nakedFormat(grid.rowList[0]))
    }

    @Test
    fun getLeftBorder() {
        TODO("Not implemented")
    }

    @Test
    fun getRightBorder() {
        TODO("Not implemented")
    }

    @Test
    fun getTopBorder() {
        TODO("Not implemented")
    }

    @Test
    fun getBottomBorder() {
        TODO("Not implemented")
    }

    @Test
    fun getTopLeftEdge() {
        TODO("Not implemented")
    }

    @Test
    fun getTopRightEdge() {
        TODO("Not implemented")
    }

    @Test
    fun getBottomLeftEdge() {
        TODO("Not implemented")
    }

    @Test
    fun getBottomRightEdge() {
        TODO("Not implemented")
    }
}
