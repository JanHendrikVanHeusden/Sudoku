package nl.jhvh.test.sudoku.grid.model

import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.GridBuilder
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.segment.Block
import nl.jhvh.sudoku.grid.model.segment.Col
import nl.jhvh.sudoku.grid.model.segment.Row
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * Tests for [Grid] initialization.
 * Is more or less a component integration test, as it tests the whole [Grid] (with [Cell]s, [Row]s, [Col]s and [Block]s)
 * for correct positions of each within the [Grid].
 */
internal class GridInitializationTest {
    /**
     * Test that correct [Block] positions are assigned.
     * <br></br>The
     * [Grid] has a [Size.DEFAULT_GRID_SIZE]
     */
    @Test
    fun `test correctly initialized Block positions are assigned`() {
        // Grid has DEFAULT_GRID_SIZE]
        val grid = GridBuilder().build()
        val blockList = grid.blockList
        assertThat(blockList[0].leftXIndex).isEqualTo(0)
        assertThat(blockList[1].leftXIndex).isEqualTo(3)
        assertThat(blockList[2].leftXIndex).isEqualTo(6)
        assertThat(blockList[3].leftXIndex).isEqualTo(0)
        assertThat(blockList[4].leftXIndex).isEqualTo(3)
        assertThat(blockList[5].leftXIndex).isEqualTo(6)
        assertThat(blockList[6].leftXIndex).isEqualTo(0)
        assertThat(blockList[7].leftXIndex).isEqualTo(3)
        assertThat(blockList[8].leftXIndex).isEqualTo(6)
        assertThat(blockList[0].rightXIndex).isEqualTo(2)
        assertThat(blockList[1].rightXIndex).isEqualTo(5)
        assertThat(blockList[2].rightXIndex).isEqualTo(8)
        assertThat(blockList[3].rightXIndex).isEqualTo(2)
        assertThat(blockList[4].rightXIndex).isEqualTo(5)
        assertThat(blockList[5].rightXIndex).isEqualTo(8)
        assertThat(blockList[6].rightXIndex).isEqualTo(2)
        assertThat(blockList[7].rightXIndex).isEqualTo(5)
        assertThat(blockList[8].rightXIndex).isEqualTo(8)
        assertThat(blockList[0].topYIndex).isEqualTo(0)
        assertThat(blockList[1].topYIndex).isEqualTo(0)
        assertThat(blockList[2].topYIndex).isEqualTo(0)
        assertThat(blockList[3].topYIndex).isEqualTo(3)
        assertThat(blockList[4].topYIndex).isEqualTo(3)
        assertThat(blockList[5].topYIndex).isEqualTo(3)
        assertThat(blockList[6].topYIndex).isEqualTo(6)
        assertThat(blockList[7].topYIndex).isEqualTo(6)
        assertThat(blockList[8].topYIndex).isEqualTo(6)
        assertThat(blockList[0].bottomYIndex).isEqualTo(2)
        assertThat(blockList[1].bottomYIndex).isEqualTo(2)
        assertThat(blockList[2].bottomYIndex).isEqualTo(2)
        assertThat(blockList[3].bottomYIndex).isEqualTo(5)
        assertThat(blockList[4].bottomYIndex).isEqualTo(5)
        assertThat(blockList[5].bottomYIndex).isEqualTo(5)
        assertThat(blockList[6].bottomYIndex).isEqualTo(8)
        assertThat(blockList[7].bottomYIndex).isEqualTo(8)
        assertThat(blockList[8].bottomYIndex).isEqualTo(8)
    }

    /**
     * Test method for [GridBuilder.build] -> [Grid] constructor, with different [Grid.getGridSize]s:
     * test that [Cell.getColIndex] and [Cell.getRowIndex] correspond to the [Block] positions.
     */
    @Test
    fun `test correctly initialized Cell positions`() {
        for (size in 2..4) {
            val grid = GridBuilder(size).build()
            val blockList = grid.blockList
            for (block in blockList) {
                for (x in 0 until grid.gridSize) {
                    val cell = block.cellList[x]
                    assertThat(cell.colIndex).`as`("Wrong X-index! Block: $block\t Cell:$cell")
                            .isEqualTo(block.leftXIndex + x % grid.blockSize)
                    assertThat(block.topYIndex + x / grid.blockSize).`as`("Wrong Y-index! Block: $block\t Cell:$cell")
                            .isEqualTo(cell.rowIndex)
                }
            }
        }
    }

    /**
     * Test method for [GridBuilder.build] -> [Grid] constructor, with different [Grid.getGridSize]s:
     * test that correct [Col] positions are assigned.
     */
    @Test
    fun `test correctly initialized Column positions`() {
        for (size in 2..4) {
            val grid = GridBuilder(size).build()
            val colList = grid.colList
            for (x in 0 until grid.gridSize) {
                assertThat(colList[x].colIndex).isEqualTo(x)
            }
        }
    }

    /**
     * Test method for [GridBuilder.build] -> [Grid] constructor, with different [Grid.getGridSize]s:
     * test that correct [Row] positions are assigned.
     */
    @Test
    fun `test correctly initialized Row positions`() {
        for (size in 2..4) {
            val grid = GridBuilder(size).build()
            val rowList = grid.rowList
            for (y in 0 until grid.gridSize) {
                assertThat(rowList[y].rowIndex).isEqualTo(y)
            }
        }
    }

}
