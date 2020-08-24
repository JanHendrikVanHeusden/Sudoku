package nl.jhvh.sudoku.grid.model

import nl.jhvh.sudoku.grid.model.Grid.GridBuilder
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.segment.Block
import nl.jhvh.sudoku.grid.model.segment.Col
import nl.jhvh.sudoku.grid.model.segment.Row
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * Tests for [Grid] initialization.
 * Is more or less a component integration test, as it tests the whole [Grid] being build with [GridBuilder],
 * and with [Cell]s, [Row]s, [Col]s and [Block]s) correctly positioned within the [Grid].
 */
internal class GridInitializationTest {
    /**
     * Test that correct [Block] positions are assigned.
     * <br></br>The
     * [Grid] has a default grid size
     */
    @Test
    fun `test correctly initialized Block positions are assigned`() {
        // Grid has DEFAULT_GRID_SIZE]
        val grid = GridBuilder().build()
        val blockList = grid.blockList
        assertThat(blockList[0].leftColIndex).isEqualTo(0)
        assertThat(blockList[1].leftColIndex).isEqualTo(3)
        assertThat(blockList[2].leftColIndex).isEqualTo(6)
        assertThat(blockList[3].leftColIndex).isEqualTo(0)
        assertThat(blockList[4].leftColIndex).isEqualTo(3)
        assertThat(blockList[5].leftColIndex).isEqualTo(6)
        assertThat(blockList[6].leftColIndex).isEqualTo(0)
        assertThat(blockList[7].leftColIndex).isEqualTo(3)
        assertThat(blockList[8].leftColIndex).isEqualTo(6)
        assertThat(blockList[0].rightColIndex).isEqualTo(2)
        assertThat(blockList[1].rightColIndex).isEqualTo(5)
        assertThat(blockList[2].rightColIndex).isEqualTo(8)
        assertThat(blockList[3].rightColIndex).isEqualTo(2)
        assertThat(blockList[4].rightColIndex).isEqualTo(5)
        assertThat(blockList[5].rightColIndex).isEqualTo(8)
        assertThat(blockList[6].rightColIndex).isEqualTo(2)
        assertThat(blockList[7].rightColIndex).isEqualTo(5)
        assertThat(blockList[8].rightColIndex).isEqualTo(8)
        assertThat(blockList[0].topRowIndex).isEqualTo(0)
        assertThat(blockList[1].topRowIndex).isEqualTo(0)
        assertThat(blockList[2].topRowIndex).isEqualTo(0)
        assertThat(blockList[3].topRowIndex).isEqualTo(3)
        assertThat(blockList[4].topRowIndex).isEqualTo(3)
        assertThat(blockList[5].topRowIndex).isEqualTo(3)
        assertThat(blockList[6].topRowIndex).isEqualTo(6)
        assertThat(blockList[7].topRowIndex).isEqualTo(6)
        assertThat(blockList[8].topRowIndex).isEqualTo(6)
        assertThat(blockList[0].bottomRowIndex).isEqualTo(2)
        assertThat(blockList[1].bottomRowIndex).isEqualTo(2)
        assertThat(blockList[2].bottomRowIndex).isEqualTo(2)
        assertThat(blockList[3].bottomRowIndex).isEqualTo(5)
        assertThat(blockList[4].bottomRowIndex).isEqualTo(5)
        assertThat(blockList[5].bottomRowIndex).isEqualTo(5)
        assertThat(blockList[6].bottomRowIndex).isEqualTo(8)
        assertThat(blockList[7].bottomRowIndex).isEqualTo(8)
        assertThat(blockList[8].bottomRowIndex).isEqualTo(8)
    }

    /**
     * Test method for [GridBuilder.build] -> [Grid] constructor, with different [Grid.gridSize]s:
     * test that [Cell.colIndex] and [Cell.rowIndex] correspond to the [Block] positions.
     */
    @Test
    fun `test correctly initialized Cell positions`() {
        for (size in 2..4) {
            val grid = GridBuilder(size).build()
            val blockList = grid.blockList
            for (block in blockList) {
                for (x in 0 until grid.gridSize) {
                    val cell = block.cells.toList()[x]
                    assertThat(cell.rowIndex in block.topRowIndex..block.bottomRowIndex)
                            .`as`("Wrong rowIndex! Block: $block\t Cell:$cell")
                            .isTrue()
                    assertThat(cell.colIndex in block.leftColIndex..block.rightColIndex)
                            .`as`("Wrong rowIndex! Block: $block\t Cell:$cell")
                            .isTrue()
                    assertThat(cell.colIndex)
                            .`as`("Wrong X-index! Block: $block\t Cell:$cell")
                            .isEqualTo(block.leftColIndex + x % grid.blockSize)
                    assertThat(block.topRowIndex + x / grid.blockSize)
                            .`as`("Wrong Y-index! Block: $block\t Cell:$cell")
                            .isEqualTo(cell.rowIndex)
                }
            }
        }
    }

    /**
     * Test method for [GridBuilder.build] -> [Grid] constructor, with different [Grid.gridSize]s:
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
     * Test method for [GridBuilder.build] -> [Grid] constructor, with different [Grid.gridSize]s:
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
