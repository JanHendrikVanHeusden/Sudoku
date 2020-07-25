package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.GridBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

/** Unit integration test for [Block] formatting */
@Disabled("Not yet implemented")
internal class BlockBoxFormatterTest {

    private val subject = BlockBoxFormatter(BoxFormatterFactory().cellBoxFormatterInstance)

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

    @BeforeEach
    fun setUp() {
        grid9 = GridBuilder()
                .fix("A1", 7) // x = 0, y = 0
                .fix("B1", 4) // x = 0, y = 1
                .fix("B3", 2) // x = 2, y = 1
                .fix("C7", 8) // x = 6, y = 2
                .fix("I6", 3) // x = 5, y = 8
                .build()

        grid9.findCell(5, 3).cellValue.setValue(4)
        grid9.findCell(7, 3).cellValue.setValue(2)
    }

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
