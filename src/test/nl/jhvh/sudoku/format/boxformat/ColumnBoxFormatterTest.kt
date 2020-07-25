package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.Grid.GridBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/** Unit integration test for [Col] formatting */
internal class ColumnBoxFormatterTest {

    private val subject = ColumnBoxFormatter(BoxFormatterFactory().cellBoxFormatterInstance)

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
    private lateinit var grid16: Grid

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

        grid16 = GridBuilder(4).build()
    }

    @Test
    fun format() {
        val cellPattern = cellContentPattern(grid9.gridSize)
        // We want the test to be exhaustive, so count the lines being tested
        val formattedLineCount = 19
        val expectedLines = grid9.colList.size * formattedLineCount
        var testedLines = 0
        grid9.colList.forEachIndexed {colIndex, col ->
            val formattedColumn = subject.format(col)
            // println("colIndex=${col.colIndex}, formatted column:\n$formattedColumns")
            assertThat(formattedColumn).hasSize(19)
            formattedColumn.forEachIndexed { lineIndex, formattedLine ->
                testedLines++
                when {
                    lineIndex == 0 -> {
                        // Top grid border
                        when {
                            colIndex == 0 -> assertThat(formattedLine).isEqualTo("╔═══╤")
                            colIndex == 8 -> assertThat(formattedLine).isEqualTo("╤═══╗")
                            colIndex % 3 == 0 -> assertThat(formattedLine).isEqualTo("╦═══╤")
                            (colIndex+1) % 3 == 0 -> assertThat(formattedLine).isEqualTo("╤═══╦")
                            else -> assertThat(formattedLine).isEqualTo("╤═══╤")
                        }
                    }
                    lineIndex == formattedLineCount-1 -> {
                        // Bottom grid border
                        when {
                            colIndex == 0 -> assertThat(formattedLine).isEqualTo("╚═══╧")
                            colIndex == 8 -> assertThat(formattedLine).isEqualTo("╧═══╝")
                            colIndex % 3 == 0 -> assertThat(formattedLine).isEqualTo("╩═══╧")
                            (colIndex+1) % 3 == 0 -> assertThat(formattedLine).isEqualTo("╧═══╩")
                            else -> assertThat(formattedLine).isEqualTo("╧═══╧")
                        }
                    }
                    lineIndex % 2 == 1 -> {
                        // Line containing the value (or spaces, if value unknown yet)
                        // We do not extensively test the values, that is done in CellBoxFormatterTest
                        when {
                            colIndex % 3 == 0 -> assertThat(formattedLine).matches("║"+cellPattern+"│")
                            (colIndex+1) % 3 == 0 -> assertThat(formattedLine).matches("│"+cellPattern+"║")
                            else -> assertThat(formattedLine).matches("│"+cellPattern+"│")
                        }
                    }
                    lineIndex % 6 == 0 -> {
                        // horizontal block border
                        when {
                            colIndex == 0 -> assertThat(formattedLine).isEqualTo("╠═══╪")
                            colIndex == 8 -> assertThat(formattedLine).isEqualTo("╪═══╣")
                            colIndex % 3 == 0 -> assertThat(formattedLine).isEqualTo("╬═══╪")
                            (colIndex+1) % 3 == 0 -> assertThat(formattedLine).isEqualTo("╪═══╬")
                            else -> assertThat(formattedLine).isEqualTo("╪═══╪")
                        }
                    }
                    else -> {
                        // not at horizontal block border or grid border
                        when {
                            colIndex == 0 -> assertThat(formattedLine).isEqualTo("╟───┼")
                            colIndex == 8 -> assertThat(formattedLine).isEqualTo("┼───╢")
                            colIndex % 3 == 0 -> assertThat(formattedLine).isEqualTo("╫───┼")
                            (colIndex + 1) % 3 == 0 -> assertThat(formattedLine).isEqualTo("┼───╫")
                            else -> assertThat(formattedLine).isEqualTo("┼───┼")
                        }
                    }
                }
            }
        }
        assertThat(testedLines).isEqualTo(expectedLines)
    }

    @Test
    fun nakedFormat() {
        val cellPattern = cellContentPattern(grid9.gridSize)
        grid9.colList.forEach {col ->
            // println("colIndex=${col.colIndex}, nakedFormat column:\n${subject.nakedFormat(col)}")
            subject.nakedFormat(col).forEachIndexed { lineIndex, formattedLine ->
                when {
                    lineIndex % 2 == 0 -> assertThat(formattedLine).matches(cellPattern)
                    (lineIndex+1) % 6 == 0 -> assertThat(formattedLine).isEqualTo("═══")
                    else -> assertThat(formattedLine).isEqualTo("───")
                }
            }
        }
    }

    @Test
    fun getLeftBorder() {
        grid9.colList.forEach {
            when {
                it.colIndex == 0 -> assertThat(subject.getLeftBorder(it).toString()).isEqualTo(
                        """
                           ║
                           ╟
                           ║
                           ╟
                           ║
                           ╠
                           ║
                           ╟
                           ║
                           ╟
                           ║
                           ╠
                           ║
                           ╟
                           ║
                           ╟
                           ║""".tidy())
                it.colIndex % 3 == 0 -> assertThat(subject.getLeftBorder(it).toString()).isEqualTo(
                        """
                           ║
                           ╫
                           ║
                           ╫
                           ║
                           ╬
                           ║
                           ╫
                           ║
                           ╫
                           ║
                           ╬
                           ║
                           ╫
                           ║
                           ╫
                           ║""".tidy())
                else -> assertThat(subject.getLeftBorder(it).toString()).isEqualTo(
                        """
                           │
                           ┼
                           │
                           ┼
                           │
                           ╪
                           │
                           ┼
                           │
                           ┼
                           │
                           ╪
                           │
                           ┼
                           │
                           ┼
                           │
                        """.tidy())
            }
        }
    }

    @Test
    fun getRightBorder() {
        grid9.colList.forEach {
            when {
                it.colIndex == 8 -> assertThat(subject.getRightBorder(it).toString()).isEqualTo(
                        """
                           ║
                           ╢
                           ║
                           ╢
                           ║
                           ╣
                           ║
                           ╢
                           ║
                           ╢
                           ║
                           ╣
                           ║
                           ╢
                           ║
                           ╢
                           ║""".tidy())
                (it.colIndex + 1) % 3 == 0 -> assertThat(subject.getRightBorder(it).toString()).isEqualTo(
                        """
                           ║
                           ╫
                           ║
                           ╫
                           ║
                           ╬
                           ║
                           ╫
                           ║
                           ╫
                           ║
                           ╬
                           ║
                           ╫
                           ║
                           ╫
                           ║""".tidy())
                else -> assertThat(subject.getRightBorder(it).toString()).isEqualTo(
                        """
                           │
                           ┼
                           │
                           ┼
                           │
                           ╪
                           │
                           ┼
                           │
                           ┼
                           │
                           ╪
                           │
                           ┼
                           │
                           ┼
                           │
                        """.tidy())
            }
        }
    }

    @Test
    fun getTopBorder() {
        grid9.colList.forEach {
            assertThat(subject.getTopBorder(it).toString()).isEqualTo("═══")
        }
    }

    @Test
    fun getBottomBorder() {
        grid9.colList.forEach {
            assertThat(subject.getBottomBorder(it).toString()).isEqualTo("═══")
//            when {
//                it.colIndex == 8 -> assertThat(subject.getBottomBorder(it).toString()).isEqualTo(("═══╧═══╧═══╩═══╧═══╧═══╩═══╧═══╧═══"))
//                (it.colIndex+1) % 3 == 0 -> assertThat(subject.getBottomBorder(it).toString()).isEqualTo(("═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══"))
//                else -> assertThat(subject.getBottomBorder(it).toString()).isEqualTo(("───┼───┼───╫───┼───┼───╫───┼───┼───"))
//            }
        }
    }

    @Test
    fun getTopLeftEdge() {
        // We want this test to be exhaustive, so count the cols tested
        val expectedCols = 9
        var testedCols = 0
        grid9.colList.forEach {
            when {
                it.colIndex == 0->  {
                    testedCols++
                    assertThat(subject.getTopLeftEdge(it))
                            .`as`("Test fails for colIndex=${it.colIndex}")
                            .isEqualTo("╔")
                }
                it.colIndex % 3 == 0 ->  {
                    testedCols++
                    assertThat(subject.getTopLeftEdge(it))
                            .`as`("Test fails for colIndex=${it.colIndex}")
                            .isEqualTo("╦")
                }
                else -> {
                    testedCols++
                    assertThat(subject.getTopLeftEdge(it))
                            .`as`("Test fails for colIndex=${it.colIndex}")
                            .isEqualTo("╤")
                }
            }
        }
        assertThat(testedCols).isEqualTo(expectedCols)
    }

    @Test
    fun getTopRightEdge() {
        // We want this test to be exhaustive, so count the cols tested
        val expectedCols = 9
        var testedCols = 0
        grid9.colList.forEach {
            when {
                it.colIndex == 8 ->  {
                    testedCols++
                    assertThat(subject.getTopRightEdge(it))
                            .`as`("Test fails for colIndex=${it.colIndex}")
                            .isEqualTo("╗")
                }
                (it.colIndex + 1) % 3 == 0 ->  {
                    testedCols++
                    assertThat(subject.getTopRightEdge(it))
                            .`as`("Test fails for colIndex=${it.colIndex}")
                            .isEqualTo("╦")
                }
                else -> {
                    testedCols++
                    assertThat(subject.getTopRightEdge(it))
                            .`as`("Test fails for colIndex=${it.colIndex}")
                            .isEqualTo("╤")
                }
            }
        }
        assertThat(testedCols).isEqualTo(expectedCols)
    }

    @Test
    fun getBottomLeftEdge() {
        // We want this test to be exhaustive, so count the cols tested
        val expectedCols = 9
        var testedCols = 0
        grid9.colList.forEach {
            when {
                it.colIndex == 0->  {
                    testedCols++
                    assertThat(subject.getBottomLeftEdge(it))
                            .`as`("Test fails for colIndex=${it.colIndex}")
                            .isEqualTo("╚")
                }
                it.colIndex % 3 == 0 ->  {
                    testedCols++
                    assertThat(subject.getBottomLeftEdge(it))
                            .`as`("Test fails for colIndex=${it.colIndex}")
                            .isEqualTo("╩")
                }
                else -> {
                    testedCols++
                    assertThat(subject.getBottomLeftEdge(it))
                            .`as`("Test fails for colIndex=${it.colIndex}")
                            .isEqualTo("╧")
                }
            }
        }
        assertThat(testedCols).isEqualTo(expectedCols)
    }

    @Test
    fun getBottomRightEdge() {
        // We want this test to be exhaustive, so count the cols tested
        val expectedCols = 9
        var testedCols = 0
        grid9.colList.forEach {
            when {
                it.colIndex == 8->  {
                    testedCols++
                    assertThat(subject.getBottomRightEdge(it))
                            .`as`("Test fails for colIndex=${it.colIndex}")
                            .isEqualTo("╝")
                }
                (it.colIndex+1) % 3 == 0 ->  {
                    testedCols++
                    assertThat(subject.getBottomRightEdge(it))
                            .`as`("Test fails for colIndex=${it.colIndex}")
                            .isEqualTo("╩")
                }
                else -> {
                    testedCols++
                    assertThat(subject.getBottomRightEdge(it))
                            .`as`("Test fails for colIndex=${it.colIndex}")
                            .isEqualTo("╧")
                }
            }
        }
        assertThat(testedCols).isEqualTo(expectedCols)
    }
}
