package nl.jhvh.sudoku.grid.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

/** Unit tests for [Grid] */
internal class GridTest {

    private val grid4 = Grid.GridBuilder(2).build()
    private val grid9 = Grid.GridBuilder(3).build()
    private val grid16 = Grid.GridBuilder(4).build()

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
    @Disabled("Not implemented yet")
    fun findCell() {
        TODO("Not implemented yet")
    }

    @Test
    @Disabled("Not implemented yet")
    fun testFindCell() {
        TODO("Not implemented yet")
    }

    @Test
    @Disabled("Not implemented yet")
    fun testFindCell1() {
        TODO("Not implemented yet")
    }

    @Test
    @Disabled("Not implemented yet")
    fun fixCell() {
        TODO("Not implemented yet")
    }

    @Test
    @Disabled("Not implemented yet")
    fun getMaxValueLength() {
        assertThat(grid4.maxValueLength).isEqualTo(1)  // "4".length()
        assertThat(grid9.maxValueLength).isEqualTo(1)  // "9".length()
        assertThat(grid16.maxValueLength).isEqualTo(2) // "16".length()
    }

    @Test
    @Disabled("Not implemented yet")
    fun testToString() {
        TODO("Not implemented yet")
    }

    @Test
    @Disabled("Not implemented yet")
    fun format() {
        TODO("Not implemented yet")
    }

    @Test
    @Disabled("Not implemented yet")
    fun getBlockSize() {
        TODO("Not implemented yet")
    }
}
