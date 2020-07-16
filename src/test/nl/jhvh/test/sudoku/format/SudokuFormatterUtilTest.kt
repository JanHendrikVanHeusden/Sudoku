package nl.jhvh.test.sudoku.format

import io.mockk.every
import io.mockk.mockk
import nl.jhvh.sudoku.format.bottomBorderIsBlockBorder
import nl.jhvh.sudoku.format.bottomBorderIsGridBorder
import nl.jhvh.sudoku.format.colIndexIsLeftBlockBorder
import nl.jhvh.sudoku.format.colIndexIsLeftGridBorder
import nl.jhvh.sudoku.format.colIndexIsRightBlockBorder
import nl.jhvh.sudoku.format.colIndexIsRightGridBorder
import nl.jhvh.sudoku.format.leftBorderIsBlockBorder
import nl.jhvh.sudoku.format.leftBorderIsGridBorder
import nl.jhvh.sudoku.format.rightBorderIsBlockBorder
import nl.jhvh.sudoku.format.rightBorderIsGridBorder
import nl.jhvh.sudoku.format.rowIndexIsBottomBlockBorder
import nl.jhvh.sudoku.format.rowIndexIsBottomGridBorder
import nl.jhvh.sudoku.format.rowIndexIsTopBlockBorder
import nl.jhvh.sudoku.format.rowIndexIsTopGridBorder
import nl.jhvh.sudoku.format.topBorderIsBlockBorder
import nl.jhvh.sudoku.format.topBorderIsGridBorder
import nl.jhvh.sudoku.grid.model.cell.Cell
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SudokuFormatterUtilTest {

    @Test
    fun `test leftBorderIsGridBorder`() {
        val cell: Cell = mockk(relaxed = true)
        for (blockSize in 2..10) {
            val gridSize = blockSize * blockSize
            every {cell.grid.gridSize} returns gridSize
            for (colIndex in 0..gridSize - 1) {
                every { cell.colIndex } returns colIndex
                when (colIndex) {
                    0 -> assertThat(leftBorderIsGridBorder(cell))
                            .`as`("Test failure for gridSize=$gridSize and colIndex=$colIndex")
                            .isTrue()
                    else -> assertThat(leftBorderIsGridBorder(cell))
                            .`as`("Test failure for gridSize=$gridSize and colIndex=$colIndex")
                            .isFalse()
                }
            }
        }
    }

    @Test
    fun `test colIndexIsLeftGridBorder`() {
        // The method under test does no input validation.
        // Unrealistic values will cause undefined results and are not tested.
        assertThat(colIndexIsLeftGridBorder(4, 0)).isTrue()
        assertThat(colIndexIsLeftGridBorder(4, 1)).isFalse()
        assertThat(colIndexIsLeftGridBorder(4, 2)).isFalse()
        assertThat(colIndexIsLeftGridBorder(4, 3)).isFalse()

        assertThat(colIndexIsLeftGridBorder(9, 0)).isTrue()
        assertThat(colIndexIsLeftGridBorder(9, 1)).isFalse()
        assertThat(colIndexIsLeftGridBorder(9, 2)).isFalse()
        assertThat(colIndexIsLeftGridBorder(9, 3)).isFalse()
        assertThat(colIndexIsLeftGridBorder(9, 4)).isFalse()
        assertThat(colIndexIsLeftGridBorder(9, 5)).isFalse()
        assertThat(colIndexIsLeftGridBorder(9, 6)).isFalse()
        assertThat(colIndexIsLeftGridBorder(9, 7)).isFalse()
        assertThat(colIndexIsLeftGridBorder(9, 8)).isFalse()

        assertThat(colIndexIsLeftGridBorder(16, 0)).isTrue()
        assertThat(colIndexIsLeftGridBorder(16, 1)).isFalse()
        assertThat(colIndexIsLeftGridBorder(16, 2)).isFalse()
        assertThat(colIndexIsLeftGridBorder(16, 3)).isFalse()
        assertThat(colIndexIsLeftGridBorder(16, 4)).isFalse()
        assertThat(colIndexIsLeftGridBorder(16, 5)).isFalse()
        assertThat(colIndexIsLeftGridBorder(16, 6)).isFalse()
        assertThat(colIndexIsLeftGridBorder(16, 7)).isFalse()
        assertThat(colIndexIsLeftGridBorder(16, 8)).isFalse()
        assertThat(colIndexIsLeftGridBorder(16, 9)).isFalse()
        assertThat(colIndexIsLeftGridBorder(16, 10)).isFalse()
        assertThat(colIndexIsLeftGridBorder(16, 11)).isFalse()
        assertThat(colIndexIsLeftGridBorder(16, 12)).isFalse()
        assertThat(colIndexIsLeftGridBorder(16, 13)).isFalse()
        assertThat(colIndexIsLeftGridBorder(16, 14)).isFalse()
        assertThat(colIndexIsLeftGridBorder(16, 15)).isFalse()
    }

    @Test
    fun `test rightBorderIsGridBorder`() {
        val cell: Cell = mockk(relaxed = true)
        for (blockSize in 2..10) {
            val gridSize = blockSize * blockSize
            every {cell.grid.gridSize} returns gridSize
            for (colIndex in 0..gridSize - 1) {
                every { cell.colIndex } returns colIndex
                when (colIndex) {
                    gridSize-1 -> assertThat(rightBorderIsGridBorder(cell))
                            .`as`("Test failure for gridSize=$gridSize and colIndex=$colIndex")
                            .isTrue()
                    else -> assertThat(rightBorderIsGridBorder(cell))
                            .`as`("Test failure for gridSize=$gridSize and colIndex=$colIndex")
                            .isFalse()
                }
            }
        }
    }

    @Test
    fun `test colIndexIsRightGridBorder`() {
        // The method under test does no input validation.
        // Unrealistic values will cause undefined results and are not tested.
        assertThat(colIndexIsRightGridBorder(4, 0)).isFalse()
        assertThat(colIndexIsRightGridBorder(4, 1)).isFalse()
        assertThat(colIndexIsRightGridBorder(4, 2)).isFalse()
        assertThat(colIndexIsRightGridBorder(4, 3)).isTrue()

        assertThat(colIndexIsRightGridBorder(9, 0)).isFalse()
        assertThat(colIndexIsRightGridBorder(9, 1)).isFalse()
        assertThat(colIndexIsRightGridBorder(9, 2)).isFalse()
        assertThat(colIndexIsRightGridBorder(9, 3)).isFalse()
        assertThat(colIndexIsRightGridBorder(9, 4)).isFalse()
        assertThat(colIndexIsRightGridBorder(9, 5)).isFalse()
        assertThat(colIndexIsRightGridBorder(9, 6)).isFalse()
        assertThat(colIndexIsRightGridBorder(9, 7)).isFalse()
        assertThat(colIndexIsRightGridBorder(9, 8)).isTrue()

        assertThat(colIndexIsRightGridBorder(16, 0)).isFalse()
        assertThat(colIndexIsRightGridBorder(16, 1)).isFalse()
        assertThat(colIndexIsRightGridBorder(16, 2)).isFalse()
        assertThat(colIndexIsRightGridBorder(16, 3)).isFalse()
        assertThat(colIndexIsRightGridBorder(16, 4)).isFalse()
        assertThat(colIndexIsRightGridBorder(16, 5)).isFalse()
        assertThat(colIndexIsRightGridBorder(16, 6)).isFalse()
        assertThat(colIndexIsRightGridBorder(16, 7)).isFalse()
        assertThat(colIndexIsRightGridBorder(16, 8)).isFalse()
        assertThat(colIndexIsRightGridBorder(16, 9)).isFalse()
        assertThat(colIndexIsRightGridBorder(16, 10)).isFalse()
        assertThat(colIndexIsRightGridBorder(16, 11)).isFalse()
        assertThat(colIndexIsRightGridBorder(16, 12)).isFalse()
        assertThat(colIndexIsRightGridBorder(16, 13)).isFalse()
        assertThat(colIndexIsRightGridBorder(16, 14)).isFalse()
        assertThat(colIndexIsRightGridBorder(16, 15)).isTrue()
    }

    @Test
    fun `test topBorderIsGridBorder`() {
        val cell: Cell = mockk(relaxed = true)
        for (blockSize in 2..10) {
            val gridSize = blockSize * blockSize
            every {cell.grid.gridSize} returns gridSize
            for (rowIndex in 0..gridSize - 1) {
                every { cell.rowIndex } returns rowIndex
                when (rowIndex) {
                    0 -> assertThat(topBorderIsGridBorder(cell))
                            .`as`("Test failure for gridSize=$gridSize and rowIndex=$rowIndex")
                            .isTrue()
                    else -> assertThat(topBorderIsGridBorder(cell))
                            .`as`("Test failure for gridSize=$gridSize and rowIndex=$rowIndex")
                            .isFalse()
                }
            }
        }
    }

    @Test
    fun `test rowIndexIsTopGridBorder`() {
        // The method under test does no input validation.
        // Unrealistic values will cause undefined results and are not tested.
        assertThat(rowIndexIsTopGridBorder(4, 0)).isTrue()
        assertThat(rowIndexIsTopGridBorder(4, 1)).isFalse()
        assertThat(rowIndexIsTopGridBorder(4, 2)).isFalse()
        assertThat(rowIndexIsTopGridBorder(4, 3)).isFalse()

        assertThat(rowIndexIsTopGridBorder(9, 0)).isTrue()
        assertThat(rowIndexIsTopGridBorder(9, 1)).isFalse()
        assertThat(rowIndexIsTopGridBorder(9, 2)).isFalse()
        assertThat(rowIndexIsTopGridBorder(9, 3)).isFalse()
        assertThat(rowIndexIsTopGridBorder(9, 4)).isFalse()
        assertThat(rowIndexIsTopGridBorder(9, 5)).isFalse()
        assertThat(rowIndexIsTopGridBorder(9, 6)).isFalse()
        assertThat(rowIndexIsTopGridBorder(9, 7)).isFalse()
        assertThat(rowIndexIsTopGridBorder(9, 8)).isFalse()

        assertThat(rowIndexIsTopGridBorder(16, 0)).isTrue()
        assertThat(rowIndexIsTopGridBorder(16, 1)).isFalse()
        assertThat(rowIndexIsTopGridBorder(16, 2)).isFalse()
        assertThat(rowIndexIsTopGridBorder(16, 3)).isFalse()
        assertThat(rowIndexIsTopGridBorder(16, 4)).isFalse()
        assertThat(rowIndexIsTopGridBorder(16, 5)).isFalse()
        assertThat(rowIndexIsTopGridBorder(16, 6)).isFalse()
        assertThat(rowIndexIsTopGridBorder(16, 7)).isFalse()
        assertThat(rowIndexIsTopGridBorder(16, 8)).isFalse()
        assertThat(rowIndexIsTopGridBorder(16, 9)).isFalse()
        assertThat(rowIndexIsTopGridBorder(16, 10)).isFalse()
        assertThat(rowIndexIsTopGridBorder(16, 11)).isFalse()
        assertThat(rowIndexIsTopGridBorder(16, 12)).isFalse()
        assertThat(rowIndexIsTopGridBorder(16, 13)).isFalse()
        assertThat(rowIndexIsTopGridBorder(16, 14)).isFalse()
        assertThat(rowIndexIsTopGridBorder(16, 15)).isFalse()
    }

    @Test
    fun `test bottomBorderIsGridBorder`() {
        val cell: Cell = mockk(relaxed = true)
        for (blockSize in 2..10) {
            val gridSize = blockSize * blockSize
            every {cell.grid.gridSize} returns gridSize
            for (rowIndex in 0..gridSize - 1) {
                every { cell.rowIndex } returns rowIndex
                when (rowIndex) {
                    gridSize-1 -> assertThat(bottomBorderIsGridBorder(cell))
                            .`as`("Test failure for gridSize=$gridSize and rowIndex=$rowIndex")
                            .isTrue()
                    else -> assertThat(bottomBorderIsGridBorder(cell))
                            .`as`("Test failure for gridSize=$gridSize and rowIndex=$rowIndex")
                            .isFalse()
                }
            }
        }
    }

    @Test
    fun `test rowIndexIsBottomGridBorder`() {
        // The method under test does no input validation.
        // Unrealistic values will cause undefined results and are not tested.
        assertThat(rowIndexIsBottomGridBorder(4, 0)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(4, 1)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(4, 2)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(4, 3)).isTrue()

        assertThat(rowIndexIsBottomGridBorder(9, 0)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(9, 1)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(9, 2)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(9, 3)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(9, 4)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(9, 5)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(9, 6)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(9, 7)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(9, 8)).isTrue()

        assertThat(rowIndexIsBottomGridBorder(16, 0)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(16, 1)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(16, 2)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(16, 3)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(16, 4)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(16, 5)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(16, 6)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(16, 7)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(16, 8)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(16, 9)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(16, 10)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(16, 11)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(16, 12)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(16, 13)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(16, 14)).isFalse()
        assertThat(rowIndexIsBottomGridBorder(16, 15)).isTrue()
    }

    @Test
    fun `test colIndexIsLeftBlockBorder`() {
        // The method under test does no input validation.
        // Unrealistic values will cause undefined results and are not tested.
        assertThat(colIndexIsLeftBlockBorder(2, 0)).isTrue()
        assertThat(colIndexIsLeftBlockBorder(2, 1)).isFalse()
        assertThat(colIndexIsLeftBlockBorder(2, 2)).isTrue()
        assertThat(colIndexIsLeftBlockBorder(2, 3)).isFalse()

        assertThat(colIndexIsLeftBlockBorder(3, 0)).isTrue()
        assertThat(colIndexIsLeftBlockBorder(3, 1)).isFalse()
        assertThat(colIndexIsLeftBlockBorder(3, 2)).isFalse()
        assertThat(colIndexIsLeftBlockBorder(3, 3)).isTrue()
        assertThat(colIndexIsLeftBlockBorder(3, 4)).isFalse()
        assertThat(colIndexIsLeftBlockBorder(3, 5)).isFalse()
        assertThat(colIndexIsLeftBlockBorder(3, 6)).isTrue()
        assertThat(colIndexIsLeftBlockBorder(3, 7)).isFalse()
        assertThat(colIndexIsLeftBlockBorder(3, 8)).isFalse()

        assertThat(colIndexIsLeftBlockBorder(4, 0)).isTrue()
        assertThat(colIndexIsLeftBlockBorder(4, 1)).isFalse()
        assertThat(colIndexIsLeftBlockBorder(4, 2)).isFalse()
        assertThat(colIndexIsLeftBlockBorder(4, 3)).isFalse()
        assertThat(colIndexIsLeftBlockBorder(4, 4)).isTrue()
        assertThat(colIndexIsLeftBlockBorder(4, 5)).isFalse()
        assertThat(colIndexIsLeftBlockBorder(4, 6)).isFalse()
        assertThat(colIndexIsLeftBlockBorder(4, 7)).isFalse()
        assertThat(colIndexIsLeftBlockBorder(4, 8)).isTrue()
        assertThat(colIndexIsLeftBlockBorder(4, 9)).isFalse()
        assertThat(colIndexIsLeftBlockBorder(4, 10)).isFalse()
        assertThat(colIndexIsLeftBlockBorder(4, 11)).isFalse()
        assertThat(colIndexIsLeftBlockBorder(4, 12)).isTrue()
        assertThat(colIndexIsLeftBlockBorder(4, 13)).isFalse()
        assertThat(colIndexIsLeftBlockBorder(4, 14)).isFalse()
        assertThat(colIndexIsLeftBlockBorder(4, 15)).isFalse()
    }

    @Test
    fun `test leftBorderIsBlockBorder`() {
        val cell: Cell = mockk(relaxed = true)
        for (blockSize in 2..10) {
            val gridSize = blockSize * blockSize
            every {cell.grid.blockSize} returns blockSize
            for (colIndex in 0..gridSize - 1) {
                every { cell.colIndex } returns colIndex
                when {
                    colIndex % blockSize == 0 -> assertThat(leftBorderIsBlockBorder(cell))
                            .`as`("Test failure for gridSize=$gridSize, blockSize=$blockSize, colIndex=$colIndex")
                            .isTrue()
                    else -> assertThat(leftBorderIsBlockBorder(cell))
                            .`as`("Test failure for gridSize=$gridSize, blockSize=$blockSize, colIndex=$colIndex")
                            .isFalse()
                }
            }
        }
    }

    @Test
    fun `test rightBorderIsBlockBorder`() {
        val cell: Cell = mockk(relaxed = true)
        for (blockSize in 2..10) {
            val gridSize = blockSize * blockSize
            every {cell.grid.blockSize} returns blockSize
            for (colIndex in 0..gridSize - 1) {
                every { cell.colIndex } returns colIndex
                when {
                    (colIndex+1) % blockSize == 0 -> assertThat(rightBorderIsBlockBorder(cell))
                            .`as`("Test failure for gridSize=$gridSize, blockSize=$blockSize, colIndex=$colIndex")
                            .isTrue()
                    else -> assertThat(rightBorderIsBlockBorder(cell))
                            .`as`("Test failure for gridSize=$gridSize, blockSize=$blockSize, colIndex=$colIndex")
                            .isFalse()
                }
            }
        }
    }

    @Test
    fun `test colIndexIsRightBlockBorder`() {
        // The method under test does no input validation.
        // Unrealistic values will cause undefined results and are not tested.
        assertThat(colIndexIsRightBlockBorder(2, 0)).isFalse()
        assertThat(colIndexIsRightBlockBorder(2, 1)).isTrue()
        assertThat(colIndexIsRightBlockBorder(2, 2)).isFalse()
        assertThat(colIndexIsRightBlockBorder(2, 3)).isTrue()

        assertThat(colIndexIsRightBlockBorder(3, 0)).isFalse()
        assertThat(colIndexIsRightBlockBorder(3, 1)).isFalse()
        assertThat(colIndexIsRightBlockBorder(3, 2)).isTrue()
        assertThat(colIndexIsRightBlockBorder(3, 3)).isFalse()
        assertThat(colIndexIsRightBlockBorder(3, 4)).isFalse()
        assertThat(colIndexIsRightBlockBorder(3, 5)).isTrue()
        assertThat(colIndexIsRightBlockBorder(3, 6)).isFalse()
        assertThat(colIndexIsRightBlockBorder(3, 7)).isFalse()
        assertThat(colIndexIsRightBlockBorder(3, 8)).isTrue()

        assertThat(colIndexIsRightBlockBorder(4, 0)).isFalse()
        assertThat(colIndexIsRightBlockBorder(4, 1)).isFalse()
        assertThat(colIndexIsRightBlockBorder(4, 2)).isFalse()
        assertThat(colIndexIsRightBlockBorder(4, 3)).isTrue()
        assertThat(colIndexIsRightBlockBorder(4, 4)).isFalse()
        assertThat(colIndexIsRightBlockBorder(4, 5)).isFalse()
        assertThat(colIndexIsRightBlockBorder(4, 6)).isFalse()
        assertThat(colIndexIsRightBlockBorder(4, 7)).isTrue()
        assertThat(colIndexIsRightBlockBorder(4, 8)).isFalse()
        assertThat(colIndexIsRightBlockBorder(4, 9)).isFalse()
        assertThat(colIndexIsRightBlockBorder(4, 10)).isFalse()
        assertThat(colIndexIsRightBlockBorder(4, 11)).isTrue()
        assertThat(colIndexIsRightBlockBorder(4, 12)).isFalse()
        assertThat(colIndexIsRightBlockBorder(4, 13)).isFalse()
        assertThat(colIndexIsRightBlockBorder(4, 14)).isFalse()
        assertThat(colIndexIsRightBlockBorder(4, 15)).isTrue()
    }

    @Test
    fun `test topBorderIsBlockBorder`() {
        val cell: Cell = mockk(relaxed = true)
        for (blockSize in 2..10) {
            val gridSize = blockSize * blockSize
            every {cell.grid.blockSize} returns blockSize
            for (rowIndex in 0..gridSize - 1) {
                every { cell.rowIndex } returns rowIndex
                when {
                    rowIndex % blockSize == 0 -> assertThat(topBorderIsBlockBorder(cell))
                            .`as`("Test failure for gridSize=$gridSize, blockSize=$blockSize, rowIndex=$rowIndex")
                            .isTrue()
                    else -> assertThat(topBorderIsBlockBorder(cell))
                            .`as`("Test failure for gridSize=$gridSize, blockSize=$blockSize, rowIndex=$rowIndex")
                            .isFalse()
                }
            }
        }
    }

    @Test
    fun `test rowIndexIsTopBlockBorder`() {
        // The method under test does no input validation.
        // Unrealistic values will cause undefined results and are not tested.
        assertThat(rowIndexIsTopBlockBorder(2, 0)).isTrue()
        assertThat(rowIndexIsTopBlockBorder(2, 1)).isFalse()
        assertThat(rowIndexIsTopBlockBorder(2, 2)).isTrue()
        assertThat(rowIndexIsTopBlockBorder(2, 3)).isFalse()

        assertThat(rowIndexIsTopBlockBorder(3, 0)).isTrue()
        assertThat(rowIndexIsTopBlockBorder(3, 1)).isFalse()
        assertThat(rowIndexIsTopBlockBorder(3, 2)).isFalse()
        assertThat(rowIndexIsTopBlockBorder(3, 3)).isTrue()
        assertThat(rowIndexIsTopBlockBorder(3, 4)).isFalse()
        assertThat(rowIndexIsTopBlockBorder(3, 5)).isFalse()
        assertThat(rowIndexIsTopBlockBorder(3, 6)).isTrue()
        assertThat(rowIndexIsTopBlockBorder(3, 7)).isFalse()
        assertThat(rowIndexIsTopBlockBorder(3, 8)).isFalse()

        assertThat(rowIndexIsTopBlockBorder(4, 0)).isTrue()
        assertThat(rowIndexIsTopBlockBorder(4, 1)).isFalse()
        assertThat(rowIndexIsTopBlockBorder(4, 2)).isFalse()
        assertThat(rowIndexIsTopBlockBorder(4, 3)).isFalse()
        assertThat(rowIndexIsTopBlockBorder(4, 4)).isTrue()
        assertThat(rowIndexIsTopBlockBorder(4, 5)).isFalse()
        assertThat(rowIndexIsTopBlockBorder(4, 6)).isFalse()
        assertThat(rowIndexIsTopBlockBorder(4, 7)).isFalse()
        assertThat(rowIndexIsTopBlockBorder(4, 8)).isTrue()
        assertThat(rowIndexIsTopBlockBorder(4, 9)).isFalse()
        assertThat(rowIndexIsTopBlockBorder(4, 10)).isFalse()
        assertThat(rowIndexIsTopBlockBorder(4, 11)).isFalse()
        assertThat(rowIndexIsTopBlockBorder(4, 12)).isTrue()
        assertThat(rowIndexIsTopBlockBorder(4, 13)).isFalse()
        assertThat(rowIndexIsTopBlockBorder(4, 14)).isFalse()
        assertThat(rowIndexIsTopBlockBorder(4, 15)).isFalse()
    }

    @Test
    fun `test bottomBorderIsBlockBorder`() {
        bottomBorderIsBlockBorder
        val cell: Cell = mockk(relaxed = true)
        for (blockSize in 2..10) {
            val gridSize = blockSize * blockSize
            every {cell.grid.blockSize} returns blockSize
            for (rowIndex in 0..gridSize - 1) {
                every { cell.rowIndex } returns rowIndex
                when {
                    (rowIndex+1) % blockSize == 0 -> assertThat(bottomBorderIsBlockBorder(cell))
                            .`as`("Test failure for gridSize=$gridSize, blockSize=$blockSize, rowIndex=$rowIndex")
                            .isTrue()
                    else -> assertThat(bottomBorderIsBlockBorder(cell))
                            .`as`("Test failure for gridSize=$gridSize, blockSize=$blockSize, rowIndex=$rowIndex")
                            .isFalse()
                }
            }
        }
    }

    @Test
    fun `test rowIndexIsBottomBlockBorder`() {
        // The method under test does no input validation.
        // Unrealistic values will cause undefined results and are not tested.
        assertThat(rowIndexIsBottomBlockBorder(2, 0)).isFalse()
        assertThat(rowIndexIsBottomBlockBorder(2, 1)).isTrue()
        assertThat(rowIndexIsBottomBlockBorder(2, 2)).isFalse()
        assertThat(rowIndexIsBottomBlockBorder(2, 3)).isTrue()

        assertThat(rowIndexIsBottomBlockBorder(3, 0)).isFalse()
        assertThat(rowIndexIsBottomBlockBorder(3, 1)).isFalse()
        assertThat(rowIndexIsBottomBlockBorder(3, 2)).isTrue()
        assertThat(rowIndexIsBottomBlockBorder(3, 3)).isFalse()
        assertThat(rowIndexIsBottomBlockBorder(3, 4)).isFalse()
        assertThat(rowIndexIsBottomBlockBorder(3, 5)).isTrue()
        assertThat(rowIndexIsBottomBlockBorder(3, 6)).isFalse()
        assertThat(rowIndexIsBottomBlockBorder(3, 7)).isFalse()
        assertThat(rowIndexIsBottomBlockBorder(3, 8)).isTrue()

        assertThat(rowIndexIsBottomBlockBorder(4, 0)).isFalse()
        assertThat(rowIndexIsBottomBlockBorder(4, 1)).isFalse()
        assertThat(rowIndexIsBottomBlockBorder(4, 2)).isFalse()
        assertThat(rowIndexIsBottomBlockBorder(4, 3)).isTrue()
        assertThat(rowIndexIsBottomBlockBorder(4, 4)).isFalse()
        assertThat(rowIndexIsBottomBlockBorder(4, 5)).isFalse()
        assertThat(rowIndexIsBottomBlockBorder(4, 6)).isFalse()
        assertThat(rowIndexIsBottomBlockBorder(4, 7)).isTrue()
        assertThat(rowIndexIsBottomBlockBorder(4, 8)).isFalse()
        assertThat(rowIndexIsBottomBlockBorder(4, 9)).isFalse()
        assertThat(rowIndexIsBottomBlockBorder(4, 10)).isFalse()
        assertThat(rowIndexIsBottomBlockBorder(4, 11)).isTrue()
        assertThat(rowIndexIsBottomBlockBorder(4, 12)).isFalse()
        assertThat(rowIndexIsBottomBlockBorder(4, 13)).isFalse()
        assertThat(rowIndexIsBottomBlockBorder(4, 14)).isFalse()
        assertThat(rowIndexIsBottomBlockBorder(4, 15)).isTrue()
    }

}
