package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.GridBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/** Unit integration test for [Row] formatting */
internal class RowBoxFormatterTest {

    private val subject = RowBoxFormatter(BoxFormatterFactory().cellBoxFormatterInstance)

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
        grid9.rowList.forEach {
            val formattedRow = subject.format(it)
            assertThat(formattedRow).hasSize(3)
            val topBorder = formattedRow[0]
            val rowContent = formattedRow[1]
            val bottomBorder = formattedRow[2]
            // println(formattedRow)
            assertThat(topBorder).isEqualTo(subject.getTopLeftEdge(it) + subject.getTopBorder(it) + subject.getTopRightEdge(it))
            // Mainly checking for correct borders. Cell content is tested elsewhere.
            assertThat(rowContent)
                    // Note that │ is a box drawing character, not a | pipe character
                    .matches("^║$cellPattern│$cellPattern│$cellPattern║$cellPattern│$cellPattern│$cellPattern║$cellPattern│$cellPattern│$cellPattern║$")
            assertThat(bottomBorder).isEqualTo(subject.getBottomLeftEdge(it) + subject.getBottomBorder(it) + subject.getBottomRightEdge(it))
        }
    }

    @Test
    fun nakedFormat() {
        val cellPattern = cellContentPattern(grid9.gridSize)
        grid9.rowList.forEach {
            // println(formatter.nakedFormat(it))
            // Mainly checking for correct borders. Cell content is tested elsewhere.
            assertThat(subject.nakedFormat(it).toString())
                    // Note that │ is a box drawing character, not a | pipe character
                    .matches("^$cellPattern│$cellPattern│$cellPattern║$cellPattern│$cellPattern│$cellPattern║$cellPattern│$cellPattern│$cellPattern$")
        }
    }

    @Test
    fun getLeftBorder() {
        grid9.rowList.forEach {
            assertThat(subject.getLeftBorder(it).toString()).isEqualTo(("║"))
        }
    }

    @Test
    fun getRightBorder() {
        grid9.rowList.forEach {
            assertThat(subject.getRightBorder(it).toString()).isEqualTo(("║"))
        }
    }

    @Test
    fun getTopBorder() {
        grid9.rowList.forEach {
            when {
                it.rowIndex == 0 -> assertThat(subject.getTopBorder(it).toString()).isEqualTo(("═══╤═══╤═══╦═══╤═══╤═══╦═══╤═══╤═══"))
                it.rowIndex % 3 == 0 -> assertThat(subject.getTopBorder(it).toString()).isEqualTo(("═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══"))
                else -> assertThat(subject.getTopBorder(it).toString()).isEqualTo(("───┼───┼───╫───┼───┼───╫───┼───┼───"))
            }
        }
    }

    @Test
    fun getBottomBorder() {
        grid9.rowList.forEach {
            when {
                it.rowIndex == 8 -> assertThat(subject.getBottomBorder(it).toString()).isEqualTo(("═══╧═══╧═══╩═══╧═══╧═══╩═══╧═══╧═══"))
                (it.rowIndex+1) % 3 == 0 -> assertThat(subject.getBottomBorder(it).toString()).isEqualTo(("═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══"))
                else -> assertThat(subject.getBottomBorder(it).toString()).isEqualTo(("───┼───┼───╫───┼───┼───╫───┼───┼───"))
            }
        }
    }

    @Test
    fun getTopLeftEdge() {
        // We want this test to be exhaustive, so count the rows tested
        val expectedRows = 9
        var testedRows = 0
        grid9.rowList.forEach {
            when {
                it.rowIndex == 0->  {
                    testedRows++
                    assertThat(subject.getTopLeftEdge(it))
                            .`as`("Test fails for rowIndex=${it.rowIndex}")
                            .isEqualTo("╔")
                }
                it.rowIndex % 3 == 0 ->  {
                    testedRows++
                    assertThat(subject.getTopLeftEdge(it))
                            .`as`("Test fails for rowIndex=${it.rowIndex}")
                            .isEqualTo("╠")
                }
                else -> {
                    testedRows++
                    assertThat(subject.getTopLeftEdge(it))
                            .`as`("Test fails for rowIndex=${it.rowIndex}")
                            .isEqualTo("╟")
                }
            }
        }
        assertThat(testedRows).isEqualTo(expectedRows)
    }

    @Test
    fun getTopRightEdge() {
        // We want this test to be exhaustive, so count the rows tested
        val expectedRows = 9
        var testedRows = 0
        grid9.rowList.forEach {
            when {
                it.rowIndex == 0->  {
                    testedRows++
                    assertThat(subject.getTopRightEdge(it))
                            .`as`("Test fails for rowIndex=${it.rowIndex}")
                            .isEqualTo("╗")
                }
                it.rowIndex % 3 == 0 ->  {
                    testedRows++
                    assertThat(subject.getTopRightEdge(it))
                            .`as`("Test fails for rowIndex=${it.rowIndex}")
                            .isEqualTo("╣")
                }
                else -> {
                    testedRows++
                    assertThat(subject.getTopRightEdge(it))
                            .`as`("Test fails for rowIndex=${it.rowIndex}")
                            .isEqualTo("╢")
                }
            }
        }
        assertThat(testedRows).isEqualTo(expectedRows)
    }

    @Test
    fun getBottomLeftEdge() {
        // We want this test to be exhaustive, so count the rows tested
        val expectedRows = 9
        var testedRows = 0
        grid9.rowList.forEach {
            when {
                it.rowIndex == 8->  {
                    testedRows++
                    assertThat(subject.getBottomLeftEdge(it))
                            .`as`("Test fails for rowIndex=${it.rowIndex}")
                            .isEqualTo("╚")
                }
                (it.rowIndex+1) % 3 == 0 ->  {
                    testedRows++
                    assertThat(subject.getBottomLeftEdge(it))
                            .`as`("Test fails for rowIndex=${it.rowIndex}")
                            .isEqualTo("╠")
                }
                else -> {
                    testedRows++
                    assertThat(subject.getBottomLeftEdge(it))
                            .`as`("Test fails for rowIndex=${it.rowIndex}")
                            .isEqualTo("╟")
                }
            }
        }
        assertThat(testedRows).isEqualTo(expectedRows)
    }

    @Test
    fun getBottomRightEdge() {
        // We want this test to be exhaustive, so count the rows tested
        val expectedRows = 9
        var testedRows = 0
        grid9.rowList.forEach {
            when {
                it.rowIndex == 8->  {
                    testedRows++
                    assertThat(subject.getBottomRightEdge(it))
                            .`as`("Test fails for rowIndex=${it.rowIndex}")
                            .isEqualTo("╝")
                }
                (it.rowIndex+1) % 3 == 0 ->  {
                    testedRows++
                    assertThat(subject.getBottomRightEdge(it))
                            .`as`("Test fails for rowIndex=${it.rowIndex}")
                            .isEqualTo("╣")
                }
                else -> {
                    testedRows++
                    assertThat(subject.getBottomRightEdge(it))
                            .`as`("Test fails for rowIndex=${it.rowIndex}")
                            .isEqualTo("╢")
                }
            }
        }
        assertThat(testedRows).isEqualTo(expectedRows)
    }
}
