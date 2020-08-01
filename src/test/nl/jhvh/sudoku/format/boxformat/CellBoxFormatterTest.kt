package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.Grid.GridBuilder
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.cell.CellRef
import org.apache.commons.lang3.StringUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

/** Unit integration test for [Cell] formatting */
class CellBoxFormatterTest {

    private val subject = CellBoxFormatter(BoxFormatterFactory().simpleCellValueFormatterInstance)

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
    fun `nakedFormat - grid 9*9`() {
        grid9.cellList.forEach {
            assertThat(subject.nakedFormat(it).toString())
                    .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                    .isEqualTo("${if (it.cellValue.isFixed) "►" else " "}${it.cellValue.value?:" "} ")
        }
    }

    @Test
    fun getLeftBorder() {
        grid9.cellList.forEach {
            when {
                it.colIndex %3 == 0 ->
                    assertThat(subject.getLeftBorder(it).toString())
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("║")
                else -> assertThat(subject.getLeftBorder(it).toString())
                        .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                        .isEqualTo("│")
            }
        }
    }

    @Test
    fun getRightBorder() {
        grid9.cellList.forEach {
            when {
                (it.colIndex + 1) %3 == 0 ->
                    assertThat(subject.getRightBorder(it).toString())
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("║")
                else -> assertThat(subject.getRightBorder(it).toString())
                        .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                        .isEqualTo("│")
            }
        }
    }

    @Test
    fun `getTopBorder - grid 9*9`() {
        grid9.cellList.forEach {
            when {
                it.rowIndex %3 == 0 ->
                    assertThat(subject.getTopBorder(it).toString())
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("═══")
                else -> assertThat(subject.getTopBorder(it).toString())
                        .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                        .isEqualTo("───")
            }
        }
    }

    @Test
    fun `getBottomBorder - grid 9*9`() {
        grid9.cellList.forEach {
            when {
                (it.rowIndex + 1) %3 == 0 ->
                    assertThat(subject.getBottomBorder(it).toString())
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("═══")
                else -> assertThat(subject.getBottomBorder(it).toString())
                        .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                        .isEqualTo("───")
            }
        }
    }

    @Test
    fun `getTopLeftEdge - grid 9*9`() {
        // We want this test to be exhaustive, so count the cells tested
        val expectedCells = 81
        var testedCells = 0
        grid9.cellList.forEach {
            when {
                it.rowIndex == 0 && it.colIndex == 0 ->  {
                    testedCells++
                    assertThat(subject.getTopLeftEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╔")
                }
                it.rowIndex == 0 && it.colIndex % 3 == 0 -> {
                    testedCells++
                    assertThat(subject.getTopLeftEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╦")
                }
                it.rowIndex == 0 -> {
                    testedCells++
                    assertThat(subject.getTopLeftEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╤")
                }
                it.rowIndex %3 == 0 && it.colIndex == 0 ->  {
                    testedCells++
                    assertThat(subject.getTopLeftEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╠")
                }
                it.rowIndex %3 == 0 && it.colIndex % 3 == 0 ->  {
                    testedCells++
                    assertThat(subject.getTopLeftEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╬")
                }
                it.rowIndex %3 == 0 ->  {
                    testedCells++
                    assertThat(subject.getTopLeftEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╪")
                }
                it.colIndex == 0 ->  {
                    testedCells++
                    assertThat(subject.getTopLeftEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╟")
                }
                it.colIndex % 3 == 0 -> {
                    testedCells++
                    assertThat(subject.getTopLeftEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╫")
                }
                else -> {
                    testedCells++
                    assertThat(subject.getTopLeftEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("┼")
                }
            }
        }
        assertThat(testedCells).isEqualTo(expectedCells)
    }

    @Test
    fun `getTopRightEdge - grid 9*9`() {
        // We want this test to be exhaustive, so count the cells tested
        val expectedCells = 81
        var testedCells = 0
        grid9.cellList.forEach {
            when {
                it.rowIndex == 0 && it.colIndex == 8 ->  {
                    testedCells++
                    assertThat(subject.getTopRightEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╗")
                }
                it.rowIndex == 0 && (it.colIndex + 1) % 3 == 0 -> {
                    testedCells++
                    assertThat(subject.getTopRightEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╦")
                }
                it.rowIndex == 0 -> {
                    testedCells++
                    assertThat(subject.getTopRightEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╤")
                }
                it.rowIndex %3 == 0 && it.colIndex == 8 ->  {
                    testedCells++
                    assertThat(subject.getTopRightEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╣")
                }
                it.rowIndex %3 == 0 && (it.colIndex + 1) % 3 == 0 ->  {
                    testedCells++
                    assertThat(subject.getTopRightEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╬")
                }
                it.rowIndex %3 == 0 ->  {
                    testedCells++
                    assertThat(subject.getTopRightEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╪")
                }
                it.colIndex == 8 ->  {
                    testedCells++
                    assertThat(subject.getTopRightEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╢")
                }
                (it.colIndex + 1) % 3 == 0 -> {
                    testedCells++
                    assertThat(subject.getTopRightEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╫")
                }
                else -> {
                    testedCells++
                    assertThat(subject.getTopRightEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("┼")
                }
            }
        }
        assertThat(testedCells).isEqualTo(expectedCells)
    }

    @Test
    fun `getBottomLeftEdge - grid 9*9`() {
        // We want this test to be exhaustive, so count the cells tested
        val expectedCells = 81
        var testedCells = 0
        grid9.cellList.forEach {
            when {
                it.rowIndex == 8 && it.colIndex == 0 ->  {
                    testedCells++
                    assertThat(subject.getBottomLeftEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╚")
                }
                it.rowIndex == 8 && it.colIndex % 3 == 0 -> {
                    testedCells++
                    assertThat(subject.getBottomLeftEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╩")
                }
                it.rowIndex == 8 -> {
                    testedCells++
                    assertThat(subject.getBottomLeftEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╧")
                }
                (it.rowIndex + 1) %3 == 0 && it.colIndex == 0 ->  {
                    testedCells++
                    assertThat(subject.getBottomLeftEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╠")
                }
                (it.rowIndex + 1) %3 == 0 && it.colIndex % 3 == 0 ->  {
                    testedCells++
                    assertThat(subject.getBottomLeftEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╬")
                }
                (it.rowIndex + 1) %3 == 0 ->  {
                    testedCells++
                    assertThat(subject.getBottomLeftEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╪")
                }
                it.colIndex == 0 ->  {
                    testedCells++
                    assertThat(subject.getBottomLeftEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╟")
                }
                it.colIndex % 3 == 0 -> {
                    testedCells++
                    assertThat(subject.getBottomLeftEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╫")
                }
                else -> {
                    testedCells++
                    assertThat(subject.getBottomLeftEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("┼")
                }
            }
        }
        assertThat(testedCells).isEqualTo(expectedCells)
    }

    @Test
    fun `getBottomRightEdge - grid 9*9`() {
        // We want this test to be exhaustive, so count the cells tested
        val expectedCells = 81
        var testedCells = 0
        grid9.cellList.forEach {
            when {
                it.rowIndex == 8 && it.colIndex == 8 ->  {
                    testedCells++
                    assertThat(subject.getBottomRightEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╝")
                }
                it.rowIndex == 8 && (it.colIndex + 1) % 3 == 0 -> {
                    testedCells++
                    assertThat(subject.getBottomRightEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╩")
                }
                it.rowIndex == 8 -> {
                    testedCells++
                    assertThat(subject.getBottomRightEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╧")
                }
                (it.rowIndex + 1) %3 == 0 && it.colIndex == 8 ->  {
                    testedCells++
                    assertThat(subject.getBottomRightEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╣")
                }
                (it.rowIndex + 1) %3 == 0 && (it.colIndex + 1) % 3 == 0 ->  {
                    testedCells++
                    assertThat(subject.getBottomRightEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╬")
                }
                (it.rowIndex + 1) %3 == 0 ->  {
                    testedCells++
                    assertThat(subject.getBottomRightEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╪")
                }
                it.colIndex == 8 ->  {
                    testedCells++
                    assertThat(subject.getBottomRightEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╢")
                }
                (it.colIndex + 1) % 3 == 0 -> {
                    testedCells++
                    assertThat(subject.getBottomRightEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("╫")
                }
                else -> {
                    testedCells++
                    assertThat(subject.getBottomRightEdge(it))
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("┼")
                }
            }
        }
        assertThat(testedCells).isEqualTo(expectedCells)
    }

    @Test
    fun `nakedFormat - grid 4*4`() {
        var numFixed = 0
        val gridBuilder4 = GridBuilder(2)
        with (gridBuilder4) {
            // fix some values
            for (x in 0..3) {
                for (y in 0..3) {
                    if ((x + 3*y) % 2 == 0) {
                        numFixed++
                        this.fix(CellRef(x, y), Random.nextInt(1,4))
                    }
                }
            }
        }
        println("numFixed: $numFixed")
        val grid4 = gridBuilder4.build()
        var numSet = 0
        with (grid4) {
            // set some other values
            for (x in 0..3) {
                for (y in 0..3) {
                    if ((x + 2*y) % 3 == 0) {
                        val cellValue = this.findCell(x, y).cellValue
                        if (!cellValue.isFixed) {
                            numSet++
                            cellValue.setValue(Random.nextInt(1, 4))
                        }
                    }
                }
            }
        }
        println("numSet: $numSet")

        grid4.cellList.filter { it.cellValue.hasValue() }.forEach {
            assertThat(subject.nakedFormat(it).toString())
                    .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                    .isEqualTo(StringUtils.center("${if (it.cellValue.isFixed) "►" else " "}${it.cellValue.value!!}", 3))
        }
        val cellListNoValue = grid4.cellList.filter { !it.cellValue.hasValue() }
        cellListNoValue.forEach {
            assertThat(subject.nakedFormat(it).toString())
                    .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                    .isEqualTo("   ")
        }
    }

    @Test
    fun `nakedFormat - grid 100*100`() {
        val gridBuilder100 = GridBuilder(10)
        with (gridBuilder100) {
            // fix some values
            for (x in 0..9) {
                for (y in 0..9) {
                    if ((x + 3*y) % 7 == 0) {
                        this.fix(CellRef(x, y), Random.nextInt(1,101))
                    }
                }
            }
        }
        // create a Grid with at least 1 fixed value 100, so 3 digits + fixed value indicator, to make sure that that is tested
        val grid100 = gridBuilder100.fix("A1", 100).build()
        with (grid100) {
            // set some other values
            for (x in 0..9) {
                for (y in 0..9) {
                    if ((x + 4*y) % 5 == 0) {
                        val cellValue = this.findCell(x, y).cellValue
                        if (!cellValue.isFixed) {
                            cellValue.setValue(Random.nextInt(1, 101))
                        }
                    }
                }
            }
        }
        // To make sure that a non-fixed number with 3 digits is tested (others are random, most will have 1 or 2 digits)
        grid100.cellList.last().cellValue.setValue(100)

        grid100.cellList.filter { it.cellValue.hasValue() }.forEach {
            assertThat(subject.nakedFormat(it).toString())
                    .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                    .isEqualTo(StringUtils.center("${if (it.cellValue.isFixed) "►" else " "}${it.cellValue.value!!}", 4))
        }
        val cellListNoValue = grid100.cellList.filter { !it.cellValue.hasValue() }
        cellListNoValue.forEachIndexed { index, cell ->
            // Not necessary to test all almost 10000 cells
            if (index % 91 == 0) {
                assertThat(subject.nakedFormat(cell).toString())
                        .`as`("Test fails for Cell x=${cell.colIndex} y=${cell.rowIndex}")
                        .isEqualTo("    ")
            }
        }
    }

    @Test
    fun `getTopBorder - grid 16*16`() {
        val grid16 = GridBuilder(4).build()
        // test that the border is long enough for 3-digit values
        // Only every 11th cell, we don't need to test all 16*16 cells :-)
        grid16.cellList.filterIndexed { index, _-> index % 11 == 0 }.forEach {
            when {
                it.rowIndex % 4 == 0 ->
                    assertThat(subject.getTopBorder(it).toString())
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("═══")
                else -> assertThat(subject.getTopBorder(it).toString())
                        .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                        .isEqualTo("───")
            }
        }
    }

    @Test
    fun `getBottomBorder - grid 16*16`() {
        val grid16 = GridBuilder(4).build()
        // test that the border is long enough for 3-digit values
        // Only every 11th cell, we don't need to test all 16*16 cells :-)
        grid16.cellList.filterIndexed { index, _-> index % 11 == 0 }.forEach {
            when {
                (it.rowIndex + 1) % 4 == 0 ->
                    assertThat(subject.getBottomBorder(it).toString())
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("═══")
                else -> assertThat(subject.getBottomBorder(it).toString())
                        .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                        .isEqualTo("───")
            }
        }
    }

    @Test
    fun `getTopBorder - grid 100*100`() {
        val grid100 = GridBuilder(10).build()
        // test that the border is long enough for 3-digit values + fixed value marker
        // Only every 91th cell, we don't need to test all 100*100 cells :-)
        grid100.cellList.filterIndexed { index, _-> index % 91 == 0 }.forEach {
            when {
                it.rowIndex % 10 == 0 ->
                    assertThat(subject.getTopBorder(it).toString())
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("════")
                else -> assertThat(subject.getTopBorder(it).toString())
                        .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                        .isEqualTo("────")
            }
        }
    }

    @Test
    fun `getBottomBorder - grid 100*100`() {
        val grid100 = GridBuilder(10).build()
        // test that the border is long enough for 3-digit values + fixed value marker
        // Only every 91th cell, we don't need to test all 100*100 cells :-)
        grid100.cellList.filterIndexed { index, _-> index % 91 == 0 }.forEach {
            when {
                (it.rowIndex + 1) % 10 == 0 ->
                    assertThat(subject.getBottomBorder(it).toString())
                            .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                            .isEqualTo("════")
                else -> assertThat(subject.getBottomBorder(it).toString())
                        .`as`("Test fails for Cell x=${it.colIndex} y=${it.rowIndex}")
                        .isEqualTo("────")
            }
        }
    }

}
