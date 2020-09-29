package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.Grid.GridBuilder
import nl.jhvh.sudoku.grid.model.segment.Block
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/** Unit integration test for [Block] formatting */
internal class BlockBoxFormatterTest {

    private val subject = BlockBoxFormatter(BoxFormatterFactory().cellFormatterInstance as CellBoxFormatter)

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
        var formatted: String
        var block: Block
        // given, when
        block = grid9.blockList[0]
        formatted = subject.format(block).toString()
        assertThat(formatted)
                .`as`("formatting fails for $block")
                .isEqualTo(
                """
                    ╔═══╤═══╤═══╦
                    ║►7 │   │   ║
                    ╟───┼───┼───╫
                    ║►4 │   │►2 ║
                    ╟───┼───┼───╫
                    ║   │   │   ║
                    ╠═══╪═══╪═══╬
                """.tidy())
        block = grid9.blockList[1]
        formatted = subject.format(block).toString()
        assertThat(formatted)
                .`as`("formatting fails for $block")
                .isEqualTo(
                """
                    ╦═══╤═══╤═══╦
                    ║   │   │   ║
                    ╫───┼───┼───╫
                    ║   │   │   ║
                    ╫───┼───┼───╫
                    ║   │   │   ║
                    ╬═══╪═══╪═══╬
                """.tidy())
        block = grid9.blockList[2]
        formatted = subject.format(block).toString()
        assertThat(formatted)
                .`as`("formatting fails for $block")
                .isEqualTo(
                """
                    ╦═══╤═══╤═══╗
                    ║   │   │   ║
                    ╫───┼───┼───╢
                    ║   │   │   ║
                    ╫───┼───┼───╢
                    ║►8 │   │   ║
                    ╬═══╪═══╪═══╣
                """.tidy())
        block = grid9.blockList[3]
        formatted = subject.format(block).toString()
        assertThat(formatted)
                .`as`("formatting fails for $block")
                .isEqualTo(
                """
                    ╠═══╪═══╪═══╬
                    ║   │   │   ║
                    ╟───┼───┼───╫
                    ║   │   │   ║
                    ╟───┼───┼───╫
                    ║   │   │   ║
                    ╠═══╪═══╪═══╬
                """.tidy())
        block = grid9.blockList[4]
        formatted = subject.format(block).toString()
        assertThat(formatted)
                .`as`("formatting fails for $block")
                .isEqualTo(
                """
                    ╬═══╪═══╪═══╬
                    ║   │   │ 4 ║
                    ╫───┼───┼───╫
                    ║   │   │   ║
                    ╫───┼───┼───╫
                    ║   │   │   ║
                    ╬═══╪═══╪═══╬
                """.tidy())
        block = grid9.blockList[5]
        formatted = subject.format(block).toString()
        assertThat(formatted)
                .`as`("formatting fails for $block")
                .isEqualTo(
                """
                    ╬═══╪═══╪═══╣
                    ║   │ 2 │   ║
                    ╫───┼───┼───╢
                    ║   │   │   ║
                    ╫───┼───┼───╢
                    ║   │   │   ║
                    ╬═══╪═══╪═══╣
                """.tidy())
        block = grid9.blockList[6]
        formatted = subject.format(block).toString()
        assertThat(formatted)
                .`as`("formatting fails for $block")
                .isEqualTo(
                """
                    ╠═══╪═══╪═══╬
                    ║   │   │   ║
                    ╟───┼───┼───╫
                    ║   │   │   ║
                    ╟───┼───┼───╫
                    ║   │   │   ║
                    ╚═══╧═══╧═══╩
                """.tidy())
        block = grid9.blockList[7]
        formatted = subject.format(block).toString()
        assertThat(formatted)
                .`as`("formatting fails for $block")
                .isEqualTo(
                """
                    ╬═══╪═══╪═══╬
                    ║   │   │   ║
                    ╫───┼───┼───╫
                    ║   │   │   ║
                    ╫───┼───┼───╫
                    ║   │   │►3 ║
                    ╩═══╧═══╧═══╩
                """.tidy())
        block = grid9.blockList[8]
        formatted = subject.format(block).toString()
        assertThat(formatted)
                .`as`("formatting fails for $block")
                .isEqualTo(
                """
                    ╬═══╪═══╪═══╣
                    ║   │   │   ║
                    ╫───┼───┼───╢
                    ║   │   │   ║
                    ╫───┼───┼───╢
                    ║   │   │   ║
                    ╩═══╧═══╧═══╝
                """.tidy())
    }

    @Test
    fun nakedFormat() {
        var nakedFormat: String
        var block: Block
        // given, when
        block = grid9.blockList[0]
        nakedFormat = subject.nakedFormat(block).toString()
        assertThat(nakedFormat)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                    ►7 │   │   
                    ───┼───┼───
                    ►4 │   │►2 
                    ───┼───┼───
                       │   │   
                """.tidy())
        block = grid9.blockList[1]
        nakedFormat = subject.nakedFormat(block).toString()
        assertThat(nakedFormat)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                       │   │   
                    ───┼───┼───
                       │   │   
                    ───┼───┼───
                       │   │   
                """.tidy())
        block = grid9.blockList[2]
        nakedFormat = subject.nakedFormat(block).toString()
        assertThat(nakedFormat)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                       │   │   
                    ───┼───┼───
                       │   │   
                    ───┼───┼───
                    ►8 │   │   
                """.tidy())
        block = grid9.blockList[3]
        nakedFormat = subject.nakedFormat(block).toString()
        assertThat(nakedFormat)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                       │   │   
                    ───┼───┼───
                       │   │   
                    ───┼───┼───
                       │   │   
                """.tidy())
        block = grid9.blockList[4]
        nakedFormat = subject.nakedFormat(block).toString()
        assertThat(nakedFormat)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                       │   │ 4 
                    ───┼───┼───
                       │   │   
                    ───┼───┼───
                       │   │   
                """.tidy())
        block = grid9.blockList[5]
        nakedFormat = subject.nakedFormat(block).toString()
        assertThat(nakedFormat)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                       │ 2 │   
                    ───┼───┼───
                       │   │   
                    ───┼───┼───
                       │   │   
                """.tidy())
        block = grid9.blockList[6]
        nakedFormat = subject.nakedFormat(block).toString()
        assertThat(nakedFormat)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                       │   │   
                    ───┼───┼───
                       │   │   
                    ───┼───┼───
                       │   │   
                """.tidy())
        block = grid9.blockList[7]
        nakedFormat = subject.nakedFormat(block).toString()
        assertThat(nakedFormat)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                       │   │   
                    ───┼───┼───
                       │   │   
                    ───┼───┼───
                       │   │►3 
                """.tidy())
        block = grid9.blockList[8]
        nakedFormat = subject.nakedFormat(block).toString()
        assertThat(nakedFormat)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                       │   │   
                    ───┼───┼───
                       │   │   
                    ───┼───┼───
                       │   │   
                """.tidy())
    }

    @Test
    fun getLeftBorder() {
        var leftBorder: String
        var block: Block
        // given, when
        block = grid9.blockList[0]
        leftBorder = subject.getLeftBorder(block).toString()
        assertThat(leftBorder)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                    ║
                    ╟
                    ║
                    ╟
                    ║
                """.tidy())
        block = grid9.blockList[1]
        leftBorder = subject.getLeftBorder(block).toString()
        assertThat(leftBorder)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                    ║
                    ╫
                    ║
                    ╫
                    ║
                """.tidy())
        block = grid9.blockList[2]
        leftBorder = subject.getLeftBorder(block).toString()
        assertThat(leftBorder)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                    ║
                    ╫
                    ║
                    ╫
                    ║
                """.tidy())
        block = grid9.blockList[3]
        leftBorder = subject.getLeftBorder(block).toString()
        assertThat(leftBorder)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                    ║
                    ╟
                    ║
                    ╟
                    ║
                """.tidy())
        block = grid9.blockList[4]
        leftBorder = subject.getLeftBorder(block).toString()
        assertThat(leftBorder)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                    ║
                    ╫
                    ║
                    ╫
                    ║
                """.tidy())
        block = grid9.blockList[5]
        leftBorder = subject.getLeftBorder(block).toString()
        assertThat(leftBorder)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                    ║
                    ╫
                    ║
                    ╫
                    ║
                """.tidy())
        block = grid9.blockList[6]
        leftBorder = subject.getLeftBorder(block).toString()
        assertThat(leftBorder)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                    ║
                    ╟
                    ║
                    ╟
                    ║
                """.tidy())
        block = grid9.blockList[7]
        leftBorder = subject.getLeftBorder(block).toString()
        assertThat(leftBorder)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                    ║
                    ╫
                    ║
                    ╫
                    ║
                """.tidy())
        block = grid9.blockList[8]
        leftBorder = subject.getLeftBorder(block).toString()
        assertThat(leftBorder)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                    ║
                    ╫
                    ║
                    ╫
                    ║
                """.tidy())
    }

    @Test
    fun getRightBorder() {
        var rightBorder: String
        var block: Block
        // given, when
        block = grid9.blockList[0]
        rightBorder = subject.getRightBorder(block).toString()
        assertThat(rightBorder)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                    ║
                    ╫
                    ║
                    ╫
                    ║
                """.tidy())
        block = grid9.blockList[1]
        rightBorder = subject.getRightBorder(block).toString()
        assertThat(rightBorder)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                    ║
                    ╫
                    ║
                    ╫
                    ║
                """.tidy())
        block = grid9.blockList[2]
        rightBorder = subject.getRightBorder(block).toString()
        assertThat(rightBorder)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                    ║
                    ╢
                    ║
                    ╢
                    ║
                """.tidy())
        block = grid9.blockList[3]
        rightBorder = subject.getRightBorder(block).toString()
        assertThat(rightBorder)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                    ║
                    ╫
                    ║
                    ╫
                    ║
                """.tidy())
        block = grid9.blockList[4]
        rightBorder = subject.getRightBorder(block).toString()
        assertThat(rightBorder)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                    ║
                    ╫
                    ║
                    ╫
                    ║
                """.tidy())
        block = grid9.blockList[5]
        rightBorder = subject.getRightBorder(block).toString()
        assertThat(rightBorder)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                    ║
                    ╢
                    ║
                    ╢
                    ║
                """.tidy())
        block = grid9.blockList[6]
        rightBorder = subject.getRightBorder(block).toString()
        assertThat(rightBorder)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                    ║
                    ╫
                    ║
                    ╫
                    ║
                """.tidy())
        block = grid9.blockList[7]
        rightBorder = subject.getRightBorder(block).toString()
        assertThat(rightBorder)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                    ║
                    ╫
                    ║
                    ╫
                    ║
                """.tidy())
        block = grid9.blockList[8]
        rightBorder = subject.getRightBorder(block).toString()
        assertThat(rightBorder)
                .`as`("formatting fails for $block")
                .isEqualTo(
                        """
                    ║
                    ╢
                    ║
                    ╢
                    ║
                """.tidy())
    }

    @Test
    fun getTopBorder() {
        var topBorder: String
        var block: Block
        // given, when
        block = grid9.blockList[0]
        topBorder = subject.getTopBorder(block).toString()
        assertThat(topBorder)
                .`as`("formatting fails for $block")
                .isEqualTo("═══╤═══╤═══")
        block = grid9.blockList[1]
        topBorder = subject.getTopBorder(block).toString()
        assertThat(topBorder)
                .`as`("formatting fails for $block")
                .isEqualTo("═══╤═══╤═══")
        block = grid9.blockList[2]
        topBorder = subject.getTopBorder(block).toString()
        assertThat(topBorder)
                .`as`("formatting fails for $block")
                .isEqualTo("═══╤═══╤═══")
        block = grid9.blockList[3]
        topBorder = subject.getTopBorder(block).toString()
        assertThat(topBorder)
                .`as`("formatting fails for $block")
                .isEqualTo("═══╪═══╪═══")
        block = grid9.blockList[4]
        topBorder = subject.getTopBorder(block).toString()
        assertThat(topBorder)
                .`as`("formatting fails for $block")
                .isEqualTo("═══╪═══╪═══")
        block = grid9.blockList[5]
        topBorder = subject.getTopBorder(block).toString()
        assertThat(topBorder)
                .`as`("formatting fails for $block")
                .isEqualTo("═══╪═══╪═══")
        block = grid9.blockList[6]
        topBorder = subject.getTopBorder(block).toString()
        assertThat(topBorder)
                .`as`("formatting fails for $block")
                .isEqualTo("═══╪═══╪═══")
        block = grid9.blockList[7]
        topBorder = subject.getTopBorder(block).toString()
        assertThat(topBorder)
                .`as`("formatting fails for $block")
                .isEqualTo("═══╪═══╪═══")
        block = grid9.blockList[8]
        topBorder = subject.getTopBorder(block).toString()
        assertThat(topBorder)
                .`as`("formatting fails for $block")
                .isEqualTo("═══╪═══╪═══")
    }

    @Test
    fun getBottomBorder() {
        var bottomBorder: String
        var block: Block
        // given, when
        block = grid9.blockList[0]
        bottomBorder = subject.getBottomBorder(block).toString()
        assertThat(bottomBorder)
                .`as`("formatting fails for $block")
                .isEqualTo("═══╪═══╪═══")
        block = grid9.blockList[1]
        bottomBorder = subject.getBottomBorder(block).toString()
        assertThat(bottomBorder)
                .`as`("formatting fails for $block")
                .isEqualTo("═══╪═══╪═══")
        block = grid9.blockList[2]
        bottomBorder = subject.getBottomBorder(block).toString()
        assertThat(bottomBorder)
                .`as`("formatting fails for $block")
                .isEqualTo("═══╪═══╪═══")
        block = grid9.blockList[3]
        bottomBorder = subject.getBottomBorder(block).toString()
        assertThat(bottomBorder)
                .`as`("formatting fails for $block")
                .isEqualTo("═══╪═══╪═══")
        block = grid9.blockList[4]
        bottomBorder = subject.getBottomBorder(block).toString()
        assertThat(bottomBorder)
                .`as`("formatting fails for $block")
                .isEqualTo("═══╪═══╪═══")
        block = grid9.blockList[5]
        bottomBorder = subject.getBottomBorder(block).toString()
        assertThat(bottomBorder)
                .`as`("formatting fails for $block")
                .isEqualTo("═══╪═══╪═══")
        block = grid9.blockList[6]
        bottomBorder = subject.getBottomBorder(block).toString()
        assertThat(bottomBorder)
                .`as`("formatting fails for $block")
                .isEqualTo("═══╧═══╧═══")
        block = grid9.blockList[7]
        bottomBorder = subject.getBottomBorder(block).toString()
        assertThat(bottomBorder)
                .`as`("formatting fails for $block")
                .isEqualTo("═══╧═══╧═══")
        block = grid9.blockList[8]
        bottomBorder = subject.getBottomBorder(block).toString()
        assertThat(bottomBorder)
                .`as`("formatting fails for $block")
                .isEqualTo("═══╧═══╧═══")
    }

    @Test
    fun getTopLeftEdge() {
        var topLeftEdge: String
        var block: Block
        // given, when
        block = grid9.blockList[0]
        topLeftEdge = subject.getTopLeftEdge(block)
        assertThat(topLeftEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╔")
        block = grid9.blockList[1]
        topLeftEdge = subject.getTopLeftEdge(block)
        assertThat(topLeftEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╦")
        block = grid9.blockList[2]
        topLeftEdge = subject.getTopLeftEdge(block)
        assertThat(topLeftEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╦")
        block = grid9.blockList[3]
        topLeftEdge = subject.getTopLeftEdge(block)
        assertThat(topLeftEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╠")
        block = grid9.blockList[4]
        topLeftEdge = subject.getTopLeftEdge(block)
        assertThat(topLeftEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╬")
        block = grid9.blockList[5]
        topLeftEdge = subject.getTopLeftEdge(block)
        assertThat(topLeftEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╬")
        block = grid9.blockList[6]
        topLeftEdge = subject.getTopLeftEdge(block)
        assertThat(topLeftEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╠")
        block = grid9.blockList[7]
        topLeftEdge = subject.getTopLeftEdge(block)
        assertThat(topLeftEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╬")
        block = grid9.blockList[8]
        topLeftEdge = subject.getTopLeftEdge(block)
        assertThat(topLeftEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╬")
    }

    @Test
    fun getTopRightEdge() {
        var topRightEdge: String
        var block: Block
        // given, when
        block = grid9.blockList[0]
        topRightEdge = subject.getTopRightEdge(block)
        assertThat(topRightEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╦")
        block = grid9.blockList[1]
        topRightEdge = subject.getTopRightEdge(block)
        assertThat(topRightEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╦")
        block = grid9.blockList[2]
        topRightEdge = subject.getTopRightEdge(block)
        assertThat(topRightEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╗")
        block = grid9.blockList[3]
        topRightEdge = subject.getTopRightEdge(block)
        assertThat(topRightEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╬")
        block = grid9.blockList[4]
        topRightEdge = subject.getTopRightEdge(block)
        assertThat(topRightEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╬")
        block = grid9.blockList[5]
        topRightEdge = subject.getTopRightEdge(block)
        assertThat(topRightEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╣")
        block = grid9.blockList[6]
        topRightEdge = subject.getTopRightEdge(block)
        assertThat(topRightEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╬")
        block = grid9.blockList[7]
        topRightEdge = subject.getTopRightEdge(block)
        assertThat(topRightEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╬")
        block = grid9.blockList[8]
        topRightEdge = subject.getTopRightEdge(block)
        assertThat(topRightEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╣")
    }

    @Test
    fun getBottomLeftEdge() {
        var bottomLeftEdge: String
        var block: Block
        // given, when
        block = grid9.blockList[0]
        bottomLeftEdge = subject.getBottomLeftEdge(block)
        assertThat(bottomLeftEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╠")
        block = grid9.blockList[1]
        bottomLeftEdge = subject.getBottomLeftEdge(block)
        assertThat(bottomLeftEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╬")
        block = grid9.blockList[2]
        bottomLeftEdge = subject.getBottomLeftEdge(block)
        assertThat(bottomLeftEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╬")
        block = grid9.blockList[3]
        bottomLeftEdge = subject.getBottomLeftEdge(block)
        assertThat(bottomLeftEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╠")
        block = grid9.blockList[4]
        bottomLeftEdge = subject.getBottomLeftEdge(block)
        assertThat(bottomLeftEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╬")
        block = grid9.blockList[5]
        bottomLeftEdge = subject.getBottomLeftEdge(block)
        assertThat(bottomLeftEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╬")
        block = grid9.blockList[6]
        bottomLeftEdge = subject.getBottomLeftEdge(block)
        assertThat(bottomLeftEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╚")
        block = grid9.blockList[7]
        bottomLeftEdge = subject.getBottomLeftEdge(block)
        assertThat(bottomLeftEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╩")
        block = grid9.blockList[8]
        bottomLeftEdge = subject.getBottomLeftEdge(block)
        assertThat(bottomLeftEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╩")
    }

    @Test
    fun getBottomRightEdge() {
        var bottomRightEdge: String
        var block: Block
        // given, when
        block = grid9.blockList[0]
        bottomRightEdge = subject.getBottomRightEdge(block)
        assertThat(bottomRightEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╬")
        block = grid9.blockList[1]
        bottomRightEdge = subject.getBottomRightEdge(block)
        assertThat(bottomRightEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╬")
        block = grid9.blockList[2]
        bottomRightEdge = subject.getBottomRightEdge(block)
        assertThat(bottomRightEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╣")
        block = grid9.blockList[3]
        bottomRightEdge = subject.getBottomRightEdge(block)
        assertThat(bottomRightEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╬")
        block = grid9.blockList[4]
        bottomRightEdge = subject.getBottomRightEdge(block)
        assertThat(bottomRightEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╬")
        block = grid9.blockList[5]
        bottomRightEdge = subject.getBottomRightEdge(block)
        assertThat(bottomRightEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╣")
        block = grid9.blockList[6]
        bottomRightEdge = subject.getBottomRightEdge(block)
        assertThat(bottomRightEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╩")
        block = grid9.blockList[7]
        bottomRightEdge = subject.getBottomRightEdge(block)
        assertThat(bottomRightEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╩")
        block = grid9.blockList[8]
        bottomRightEdge = subject.getBottomRightEdge(block)
        assertThat(bottomRightEdge)
                .`as`("formatting fails for $block")
                .isEqualTo("╝")
    }

}
