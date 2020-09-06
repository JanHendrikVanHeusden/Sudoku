package nl.jhvh.sudoku.grid.model

import io.mockk.every
import io.mockk.mockk
import nl.jhvh.sudoku.base.MAX_BLOCK_SIZE
import nl.jhvh.sudoku.grid.model.Grid.GridBuilder
import nl.jhvh.sudoku.grid.model.cell.CellRef
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random
import kotlin.test.assertFailsWith

/** Unit tests for [Grid] */
internal class GridTest {

    private val grid4 = GridBuilder(2).build()
    private val grid9 = GridBuilder(3).build()
    private val grid16 = GridBuilder(4).build()

    @Test
    fun gridSize() {
        assertThat(grid4.gridSize).isEqualTo(4)
        assertThat(grid9.gridSize).isEqualTo(9)
        assertThat(grid16.gridSize).isEqualTo(16)
    }

    @Test
    fun getMaxValue() {
        assertThat(grid4.maxValue).isEqualTo(4)
        assertThat(grid9.maxValue).isEqualTo(9)
        assertThat(grid16.maxValue).isEqualTo(16)
    }

    @Test
    fun getCellList() {
        for (grid in listOf(grid4, grid9, grid16)) {
            val gridSize = grid.gridSize
            val cellList = grid.cellList
            assertThat(cellList.size).isEqualTo(gridSize*gridSize)
            cellList.forEachIndexed {index, cell->
                assertThat(cell.colIndex).isEqualTo(index % gridSize)
                assertThat(cell.rowIndex).isEqualTo(index / gridSize)
            }
        }
    }

    @Test
    fun getRowList() {
        for (grid in listOf(grid4, grid9, grid16)) {
            val gridSize = grid.gridSize
            val rowList = grid.rowList
            assertThat(rowList.size).isEqualTo(gridSize)
            rowList.forEachIndexed {index, row ->
                assertThat(row.rowIndex).isEqualTo(index)
            }
        }
    }

    @Test
    fun getColList() {
        for (grid in listOf(grid4, grid9, grid16)) {
            val gridSize = grid.gridSize
            val colList = grid.colList
            assertThat(colList.size).isEqualTo(gridSize)
            colList.forEachIndexed {index, col ->
                assertThat(col.colIndex).isEqualTo(index)
            }
        }
    }

    @Test
    fun getBlockList() {
        for (grid in listOf(grid4, grid9, grid16)) {
            val gridSize = grid.gridSize
            val blockSize = grid.blockSize
            val blockList = grid.blockList
            assertThat(blockList.size).isEqualTo(gridSize)
            blockList.forEachIndexed {index, block ->
                assertThat(block.leftColIndex)
                        .`as`("Failure for block.leftXIndex=${block.leftColIndex} for blockList[$index] ")
                        .isEqualTo((index * blockSize) % gridSize)
                assertThat(block.topRowIndex)
                        .`as`("Failure for block.topYIndex=${block.topRowIndex} for blockList[$index] ")
                        .isEqualTo((index / blockSize) * blockSize)
            }
        }
    }

    @Test
    fun `findCell - by String`() {
        for (grid in listOf(grid4, grid9, grid16)) {
            val cells = HashSet(grid.cellList)
            for (colRef in 1..grid.gridSize) {
                for (y in 0 until grid.gridSize) {
                    // upper case
                    val rowRef = ('A'+y).toString()
                    val cellRefString = rowRef + colRef.toString()
                    val cell = grid.findCell(cellRefString)
                    assertThat(cell.colIndex).isEqualTo(colRef-1)
                    assertThat(cell.rowIndex).isEqualTo(y)
                    assertThat(cells.remove(cell)).isTrue()
                }
            }
            // all cells found now, so the set should be empty now
            assertThat(cells).isEmpty()
        }
        for (grid in listOf(grid4, grid9, grid16)) {
            val cells = HashSet(grid.cellList)
            for (colRef in 1..grid.gridSize) {
                for (y in 0 until grid.gridSize) {
                    // lower case
                    val rowRef = ('a'+y).toString()
                    val cellRefString = rowRef + colRef.toString()
                    val cell = grid.findCell(cellRefString)
                    assertThat(cell.colIndex).isEqualTo(colRef-1)
                    assertThat(cell.rowIndex).isEqualTo(y)
                    assertThat(cells.remove(cell)).isTrue()
                }
            }
            // all cells found now, so the set should be empty now
            assertThat(cells).isEmpty()
        }
    }

    @Test
    fun `findCell - by CellRef`() {
        for (grid in listOf(grid4, grid9, grid16)) {
            val cells = HashSet(grid.cellList)
            for (colIndex in 0 until grid.gridSize) {
                for (rowIndex in 0 until grid.gridSize) {
                    val cellRefMock: CellRef = mockk()
                    every {cellRefMock.x} returns colIndex
                    every {cellRefMock.y} returns rowIndex
                    val cell = grid.findCell(cellRefMock)
                    assertThat(cell.colIndex).isEqualTo(colIndex)
                    assertThat(cell.rowIndex).isEqualTo(rowIndex)
                    assertThat(cells.remove(cell)).isTrue()
                }
            }
            // all cells found now, so the set should be empty now
            assertThat(cells).isEmpty()
        }
    }

    @Test
    fun `findCell - coordinates outside of Grid should throw Exception`() {
        val blockSize = 4
        val gridSize = blockSize * blockSize

        val grid = GridBuilder(blockSize).build()

        // edge values OK
        grid.findCell(0, 0)
        grid.findCell(gridSize-1, 0)
        grid.findCell(0, gridSize-1)
        grid.findCell(gridSize-1, gridSize-1)

        // Outside Grid - exception
        assertThrows<IllegalArgumentException> { grid.findCell(-1, -1) }
        assertThrows<IllegalArgumentException> { grid.findCell(-1, 0) }
        assertThrows<IllegalArgumentException> { grid.findCell(0, -1) }
        assertThrows<IllegalArgumentException> { grid.findCell(gridSize, 0) }
        assertThrows<IllegalArgumentException> { grid.findCell(gridSize, gridSize) }
        assertThrows<IllegalArgumentException> { grid.findCell(Random.nextInt(gridSize, Int.MAX_VALUE), 0) }
        assertThrows<IllegalArgumentException> { grid.findCell(0, Random.nextInt(gridSize, Int.MAX_VALUE)) }
    }

    @Test
    fun `findCell - by colIndex, rowIndex`() {
        for (grid in listOf(grid4, grid9, grid16)) {
            val cells = HashSet(grid.cellList)
            for (colIndex in 0 until grid.gridSize) {
                for (rowIndex in 0 until grid.gridSize) {
                    val cell = grid.findCell(colIndex, rowIndex)
                    assertThat(cell.colIndex).isEqualTo(colIndex)
                    assertThat(cell.rowIndex).isEqualTo(rowIndex)
                    assertThat(cells.remove(cell)).isTrue()
                }
            }
            // all cells found now, so the set should be empty now
            assertThat(cells).isEmpty()
        }
    }

    @Test
    fun getMaxValueLength() {
        assertThat(grid4.maxValueLength).isEqualTo(1)  // "4".length()
        assertThat(grid9.maxValueLength).isEqualTo(1)  // "9".length()
        assertThat(grid16.maxValueLength).isEqualTo(2) // "16".length()
        assertThat(GridBuilder(10).build().maxValueLength).isEqualTo(3) // "100".length()
    }

    @Test
    fun validateBlockSize() {
        // Blocksize 1 is not very useful, but should not fail
        GridBuilder(1).build()
        // should fail
        assertFailsWith<java.lang.IllegalArgumentException> { GridBuilder(0).build() }
        assertFailsWith<java.lang.IllegalArgumentException> { GridBuilder(-1).build() }
        assertFailsWith<java.lang.IllegalArgumentException> { GridBuilder(Int.MIN_VALUE).build() }
        assertFailsWith<java.lang.IllegalArgumentException> { GridBuilder(MAX_BLOCK_SIZE+1).build() }
        assertFailsWith<java.lang.IllegalArgumentException> { GridBuilder(Int.MAX_VALUE).build() }
    }

    @Test
    fun testToString() {
        for (grid in listOf(grid4, grid9, grid16)) {
            assertThat(grid.toString()).contains("${Grid::class.simpleName}: ", "(blockSize=${grid.blockSize}, gridSize=${grid.gridSize})")
        }
    }

    @Test
    fun getBlockSize() {
        assertThat(grid4.blockSize).isEqualTo(2)
        assertThat(grid9.blockSize).isEqualTo(3)
        assertThat(grid16.blockSize).isEqualTo(4)
    }

    @Test
    fun fixedValues() {
        // given
        val gridBuilder = GridBuilder(5)
        val expected: MutableMap<CellRef, Int> = mutableMapOf()

        let {CellRef("A8")}
                .also { gridBuilder.fix(it, 17) }
                .also { expected[it] = 17 }

        let {CellRef("B24")}
                .also { gridBuilder.fix(it, 25) }
                .also { expected[it] = 25 }

        let {CellRef("X9")}
                .also { gridBuilder.fix(it, 2) }
                .also { expected[it] = 2 }

        let {CellRef("Y25")}
                .also { gridBuilder.fix(it, 3) }
                .also { expected[it] = 3 }

        // when
        val grid = gridBuilder.build()
        // then
        assertThat(grid.fixedValues).isEqualTo(expected)
        // loop through cells and assert correctly fixed / non fixed values
        for (x in 0 until grid.gridSize) {
            for (y in 0 until grid.gridSize) {
                val cellRef = CellRef(x, y)
                let {grid.findCell(cellRef)}
                        .also { assertThat(it.isFixed).isEqualTo(expected.containsKey(cellRef)) }
                        .also { assertThat(it.fixedValue).isEqualTo(expected[cellRef]) }
            }
        }
    }

}
