package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.grid.model.Grid
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

/** Unit integration test for [Block] formatting */
@Disabled("Not yet implemented")
internal class BlockBoxFormatterTest {

    private val subject = GridBoxFormatter()

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
    private lateinit var grid9: Grid

    @Test
    fun format() {
        TODO("Not yet implemented")
    }

    @Test
    fun nakedFormat() {
        TODO("Not yet implemented")
    }

    @Test
    fun getLeftBorder() {
        TODO("Not yet implemented")
    }

    @Test
    fun getRightBorder() {
        TODO("Not yet implemented")
    }

    @Test
    fun getTopBorder() {
        TODO("Not yet implemented")
    }

    @Test
    fun getBottomBorder() {
        TODO("Not yet implemented")
    }

    @Test
    fun getTopLeftEdge() {
        TODO("Not yet implemented")
    }

    @Test
    fun getTopRightEdge() {
        TODO("Not yet implemented")
    }

    @Test
    fun getBottomLeftEdge() {
        TODO("Not yet implemented")
    }

    @Test
    fun getBottomRightEdge() {
        TODO("Not yet implemented")
    }
}
