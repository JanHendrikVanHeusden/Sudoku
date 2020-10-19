package nl.jhvh.sudoku.format

/* Copyright 2020 Jan-Hendrik van Heusden

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

import io.mockk.every
import io.mockk.mockk
import nl.jhvh.sudoku.format.boxformat.CellBoxFormatter.BorderChar.bottomBorder
import nl.jhvh.sudoku.format.boxformat.CellBoxFormatter.BorderChar.bottomLeftEdge
import nl.jhvh.sudoku.format.boxformat.CellBoxFormatter.BorderChar.bottomRightEdge
import nl.jhvh.sudoku.format.boxformat.CellBoxFormatter.BorderChar.leftBorder
import nl.jhvh.sudoku.format.boxformat.CellBoxFormatter.BorderChar.rightBorder
import nl.jhvh.sudoku.format.boxformat.CellBoxFormatter.BorderChar.topBorder
import nl.jhvh.sudoku.format.boxformat.CellBoxFormatter.BorderChar.topLeftEdge
import nl.jhvh.sudoku.format.boxformat.CellBoxFormatter.BorderChar.topRightEdge
import nl.jhvh.sudoku.grid.model.cell.Cell
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SudokuFormatterUtilTest {

    private lateinit var cellMock: Cell

    @BeforeEach
    fun setUp() {
        cellMock = mockk(relaxed = false)
        every {cellMock.grid.blockSize} returns 3
        every {cellMock.grid.gridSize} returns 9
    }

    @Test
    fun `test leftBorderIsGridBorder`() {
        val cell: Cell = mockk(relaxed = true)
        for (blockSize in 2..10) {
            val gridSize = blockSize * blockSize
            every {cell.grid.gridSize} returns gridSize
            for (colIndex in 0 until gridSize) {
                every { cell.colIndex } returns colIndex
                when (colIndex) {
                    0 -> assertThat(cell.leftBorderIsGridBorder())
                            .`as`("Test failure for gridSize=$gridSize and colIndex=$colIndex")
                            .isTrue()
                    else -> assertThat(cell.leftBorderIsGridBorder())
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
            for (colIndex in 0 until gridSize) {
                every { cell.colIndex } returns colIndex
                when (colIndex) {
                    gridSize-1 -> assertThat(cell.rightBorderIsGridBorder())
                            .`as`("Test failure for gridSize=$gridSize and colIndex=$colIndex")
                            .isTrue()
                    else -> assertThat(cell.rightBorderIsGridBorder())
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
            for (rowIndex in 0 until gridSize) {
                every { cell.rowIndex } returns rowIndex
                when (rowIndex) {
                    0 -> assertThat(cell.topBorderIsGridBorder())
                            .`as`("Test failure for gridSize=$gridSize and rowIndex=$rowIndex")
                            .isTrue()
                    else -> assertThat(cell.topBorderIsGridBorder())
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
            for (rowIndex in 0 until gridSize) {
                every { cell.rowIndex } returns rowIndex
                when (rowIndex) {
                    gridSize-1 -> assertThat(cell.bottomBorderIsGridBorder())
                            .`as`("Test failure for gridSize=$gridSize and rowIndex=$rowIndex")
                            .isTrue()
                    else -> assertThat(cell.bottomBorderIsGridBorder())
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
            for (colIndex in 0 until gridSize) {
                every { cell.colIndex } returns colIndex
                when {
                    colIndex % blockSize == 0 -> assertThat(cell.leftBorderIsBlockBorder())
                            .`as`("Test failure for gridSize=$gridSize, blockSize=$blockSize, colIndex=$colIndex")
                            .isTrue()
                    else -> assertThat(cell.leftBorderIsBlockBorder())
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
            for (colIndex in 0 until gridSize) {
                every { cell.colIndex } returns colIndex
                when {
                    (colIndex+1) % blockSize == 0 -> assertThat(cell.rightBorderIsBlockBorder())
                            .`as`("Test failure for gridSize=$gridSize, blockSize=$blockSize, colIndex=$colIndex")
                            .isTrue()
                    else -> assertThat(cell.rightBorderIsBlockBorder())
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
            for (rowIndex in 0 until gridSize) {
                every { cell.rowIndex } returns rowIndex
                when {
                    rowIndex % blockSize == 0 -> assertThat(cell.topBorderIsBlockBorder())
                            .`as`("Test failure for gridSize=$gridSize, blockSize=$blockSize, rowIndex=$rowIndex")
                            .isTrue()
                    else -> assertThat(cell.topBorderIsBlockBorder())
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
        val cell: Cell = mockk(relaxed = true)
        for (blockSize in 2..10) {
            val gridSize = blockSize * blockSize
            every {cell.grid.blockSize} returns blockSize
            for (rowIndex in 0 until gridSize) {
                every { cell.rowIndex } returns rowIndex
                when {
                    (rowIndex+1) % blockSize == 0 -> assertThat(cell.bottomBorderIsBlockBorder())
                            .`as`("Test failure for gridSize=$gridSize, blockSize=$blockSize, rowIndex=$rowIndex")
                            .isTrue()
                    else -> assertThat(cell.bottomBorderIsBlockBorder())
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


    @Test
    fun `Cell - leftBorder`() {
        // given: blockSize = 3, gridSize = 9

        // left grid border cell
        every {cellMock.colIndex} returns 0
        assertThat(cellMock.leftBorder()).isEqualTo('║')
        // left block border cell
        every {cellMock.colIndex} returns 3
        assertThat(cellMock.leftBorder()).isEqualTo('║')
        every {cellMock.colIndex} returns 6
        assertThat(cellMock.leftBorder()).isEqualTo('║')
        // Left border not grid or block border
        every {cellMock.colIndex} returns 1
        assertThat(cellMock.leftBorder()).isEqualTo('│')
        every {cellMock.colIndex} returns 2
        assertThat(cellMock.leftBorder()).isEqualTo('│')
        every {cellMock.colIndex} returns 4
        assertThat(cellMock.leftBorder()).isEqualTo('│')
        every {cellMock.colIndex} returns 5
        assertThat(cellMock.leftBorder()).isEqualTo('│')
        every {cellMock.colIndex} returns 7
        assertThat(cellMock.leftBorder()).isEqualTo('│')
        every {cellMock.colIndex} returns 8
    }

    @Test
    fun `Cell - rightBorder`() {
        // given: blockSize = 3, gridSize = 9

        // right grid border cell
        every {cellMock.colIndex} returns 8
        assertThat(cellMock.rightBorder()).isEqualTo('║')
        // right block border cell
        every {cellMock.colIndex} returns 2
        assertThat(cellMock.rightBorder()).isEqualTo('║')
        every {cellMock.colIndex} returns 5
        assertThat(cellMock.rightBorder()).isEqualTo('║')
        // Left border not grid or block border
        every {cellMock.colIndex} returns 0
        assertThat(cellMock.rightBorder()).isEqualTo('│')
        every {cellMock.colIndex} returns 1
        assertThat(cellMock.rightBorder()).isEqualTo('│')
        every {cellMock.colIndex} returns 3
        assertThat(cellMock.rightBorder()).isEqualTo('│')
        every {cellMock.colIndex} returns 4
        assertThat(cellMock.rightBorder()).isEqualTo('│')
        every {cellMock.colIndex} returns 6
        assertThat(cellMock.rightBorder()).isEqualTo('│')
        every {cellMock.colIndex} returns 7
    }

    @Test
    fun `Cell - topBorder`() {
        // given: blockSize = 3, gridSize = 9

        // top grid border cell
        every {cellMock.rowIndex} returns 0
        assertThat(cellMock.topBorder()).isEqualTo('═')
        // top block border cell
        every {cellMock.rowIndex} returns 3
        assertThat(cellMock.topBorder()).isEqualTo('═')
        every {cellMock.rowIndex} returns 6
        assertThat(cellMock.topBorder()).isEqualTo('═')
        // Left border not grid or block border
        every {cellMock.rowIndex} returns 1
        assertThat(cellMock.topBorder()).isEqualTo('─')
        every {cellMock.rowIndex} returns 2
        assertThat(cellMock.topBorder()).isEqualTo('─')
        every {cellMock.rowIndex} returns 4
        assertThat(cellMock.topBorder()).isEqualTo('─')
        every {cellMock.rowIndex} returns 5
        assertThat(cellMock.topBorder()).isEqualTo('─')
        every {cellMock.rowIndex} returns 7
        assertThat(cellMock.topBorder()).isEqualTo('─')
        every {cellMock.rowIndex} returns 8
    }

    @Test
    fun `Cell - bottomBorder`() {
        // given: blockSize = 3, gridSize = 9

        // bottom grid border cell
        every {cellMock.rowIndex} returns 8
        assertThat(cellMock.bottomBorder()).isEqualTo('═')
        // bottom block border cell
        every {cellMock.rowIndex} returns 2
        assertThat(cellMock.bottomBorder()).isEqualTo('═')
        every {cellMock.rowIndex} returns 5
        assertThat(cellMock.bottomBorder()).isEqualTo('═')
        // Left border not grid or block border
        every {cellMock.rowIndex} returns 0
        assertThat(cellMock.bottomBorder()).isEqualTo('─')
        every {cellMock.rowIndex} returns 1
        assertThat(cellMock.bottomBorder()).isEqualTo('─')
        every {cellMock.rowIndex} returns 3
        assertThat(cellMock.bottomBorder()).isEqualTo('─')
        every {cellMock.rowIndex} returns 4
        assertThat(cellMock.bottomBorder()).isEqualTo('─')
        every {cellMock.rowIndex} returns 6
        assertThat(cellMock.bottomBorder()).isEqualTo('─')
        every {cellMock.rowIndex} returns 7
    }

    @Test
    fun `Cell - topLeftEdge`() {
        // given: blockSize = 3, gridSize = 9

        // top left cell
        every {cellMock.rowIndex} returns 0
        every {cellMock.colIndex} returns 0
        assertThat(cellMock.topLeftEdge()).isEqualTo('╔')

        // top cell, left border is block border
        every {cellMock.rowIndex} returns 0
        every {cellMock.colIndex} returns 3
        assertThat(cellMock.topLeftEdge()).isEqualTo('╦')
        every {cellMock.rowIndex} returns 0
        every {cellMock.colIndex} returns 6
        assertThat(cellMock.topLeftEdge()).isEqualTo('╦')

        // top cell, left border is not a block border
        every {cellMock.rowIndex} returns 0
        every {cellMock.colIndex} returns 1
        assertThat(cellMock.topLeftEdge()).isEqualTo('╤')
        every {cellMock.rowIndex} returns 0
        every {cellMock.colIndex} returns 2
        assertThat(cellMock.topLeftEdge()).isEqualTo('╤')
        every {cellMock.rowIndex} returns 0
        every {cellMock.colIndex} returns 4
        assertThat(cellMock.topLeftEdge()).isEqualTo('╤')
        every {cellMock.rowIndex} returns 0
        every {cellMock.colIndex} returns 5
        assertThat(cellMock.topLeftEdge()).isEqualTo('╤')
        every {cellMock.rowIndex} returns 0
        every {cellMock.colIndex} returns 7
        assertThat(cellMock.topLeftEdge()).isEqualTo('╤')
        every {cellMock.rowIndex} returns 0
        every {cellMock.colIndex} returns 8
        assertThat(cellMock.topLeftEdge()).isEqualTo('╤')

        // top is block border but not grid border, left is grid border
        every {cellMock.rowIndex} returns 3
        every {cellMock.colIndex} returns 0
        assertThat(cellMock.topLeftEdge()).isEqualTo('╠')
        every {cellMock.rowIndex} returns 6
        every {cellMock.colIndex} returns 0
        assertThat(cellMock.topLeftEdge()).isEqualTo('╠')

        // top is not block border, left is grid border
        every {cellMock.rowIndex} returns 1
        every {cellMock.colIndex} returns 0
        assertThat(cellMock.topLeftEdge()).isEqualTo('╟')
        every {cellMock.rowIndex} returns 2
        every {cellMock.colIndex} returns 0
        assertThat(cellMock.topLeftEdge()).isEqualTo('╟')
        every {cellMock.rowIndex} returns 4
        every {cellMock.colIndex} returns 0
        assertThat(cellMock.topLeftEdge()).isEqualTo('╟')
        every {cellMock.rowIndex} returns 5
        every {cellMock.colIndex} returns 0
        assertThat(cellMock.topLeftEdge()).isEqualTo('╟')
        every {cellMock.rowIndex} returns 7
        every {cellMock.colIndex} returns 0
        assertThat(cellMock.topLeftEdge()).isEqualTo('╟')
        every {cellMock.rowIndex} returns 8
        every {cellMock.colIndex} returns 0
        assertThat(cellMock.topLeftEdge()).isEqualTo('╟')

        // top is block border but not grid border, left is block border but not grid border
        every {cellMock.rowIndex} returns 3
        every {cellMock.colIndex} returns 3
        assertThat(cellMock.topLeftEdge()).isEqualTo('╬')
        every {cellMock.rowIndex} returns 3
        every {cellMock.colIndex} returns 6
        assertThat(cellMock.topLeftEdge()).isEqualTo('╬')
        every {cellMock.rowIndex} returns 6
        every {cellMock.colIndex} returns 3
        assertThat(cellMock.topLeftEdge()).isEqualTo('╬')
        every {cellMock.rowIndex} returns 6
        every {cellMock.colIndex} returns 6
        assertThat(cellMock.topLeftEdge()).isEqualTo('╬')

        // top is not block border and not grid border, left is block border but not grid border
        every {cellMock.rowIndex} returns 1
        every {cellMock.colIndex} returns 3
        assertThat(cellMock.topLeftEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 2
        every {cellMock.colIndex} returns 3
        assertThat(cellMock.topLeftEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 4
        every {cellMock.colIndex} returns 3
        assertThat(cellMock.topLeftEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 5
        every {cellMock.colIndex} returns 3
        assertThat(cellMock.topLeftEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 7
        every {cellMock.colIndex} returns 3
        assertThat(cellMock.topLeftEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 8
        every {cellMock.colIndex} returns 3
        assertThat(cellMock.topLeftEdge()).isEqualTo('╫')

        every {cellMock.rowIndex} returns 1
        every {cellMock.colIndex} returns 6
        assertThat(cellMock.topLeftEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 2
        every {cellMock.colIndex} returns 6
        assertThat(cellMock.topLeftEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 4
        every {cellMock.colIndex} returns 6
        assertThat(cellMock.topLeftEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 5
        every {cellMock.colIndex} returns 6
        assertThat(cellMock.topLeftEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 7
        every {cellMock.colIndex} returns 6
        assertThat(cellMock.topLeftEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 8
        every {cellMock.colIndex} returns 6
        assertThat(cellMock.topLeftEdge()).isEqualTo('╫')

        val gridSize = cellMock.grid.gridSize
        val blockSize = cellMock.grid.blockSize
        // no block border or grid borders
        for (x in 0 until gridSize) {
            if (x % blockSize == 0) {
                continue
            }
            for (y in 0 until gridSize) {
                if (y % blockSize == 0) {
                    continue
                }
                every {cellMock.colIndex} returns x
                every {cellMock.rowIndex} returns y
            }
            assertThat(cellMock.topLeftEdge())
                    .`as`("Failure for colIndex=${cellMock.colIndex} and rowIndex=${cellMock.rowIndex}")
                    .isEqualTo('┼')
        }
    }

    @Test
    fun `Cell - topRightEdge`() {
        // given: blockSize = 3, gridSize = 9

        // top right cell
        every {cellMock.rowIndex} returns 0
        every {cellMock.colIndex} returns 8
        assertThat(cellMock.topRightEdge()).isEqualTo('╗')

        // top cell, right border is block border
        every {cellMock.rowIndex} returns 0
        every {cellMock.colIndex} returns 2
        assertThat(cellMock.topRightEdge()).isEqualTo('╦')
        every {cellMock.rowIndex} returns 0
        every {cellMock.colIndex} returns 5
        assertThat(cellMock.topRightEdge()).isEqualTo('╦')

        // top cell, right border is not a block border
        every {cellMock.rowIndex} returns 0
        every {cellMock.colIndex} returns 0
        assertThat(cellMock.topRightEdge()).isEqualTo('╤')
        every {cellMock.rowIndex} returns 0
        every {cellMock.colIndex} returns 1
        assertThat(cellMock.topRightEdge()).isEqualTo('╤')
        every {cellMock.rowIndex} returns 0
        every {cellMock.colIndex} returns 3
        assertThat(cellMock.topRightEdge()).isEqualTo('╤')
        every {cellMock.rowIndex} returns 0
        every {cellMock.colIndex} returns 4
        assertThat(cellMock.topRightEdge()).isEqualTo('╤')
        every {cellMock.rowIndex} returns 0
        every {cellMock.colIndex} returns 6
        assertThat(cellMock.topRightEdge()).isEqualTo('╤')
        every {cellMock.rowIndex} returns 0
        every {cellMock.colIndex} returns 7
        assertThat(cellMock.topRightEdge()).isEqualTo('╤')

        // top is block border but not grid border, right is grid border
        every {cellMock.rowIndex} returns 3
        every {cellMock.colIndex} returns 8
        assertThat(cellMock.topRightEdge()).isEqualTo('╣')
        every {cellMock.rowIndex} returns 6
        every {cellMock.colIndex} returns 8
        assertThat(cellMock.topRightEdge()).isEqualTo('╣')

        // top is not block border, right is grid border
        every {cellMock.rowIndex} returns 1
        every {cellMock.colIndex} returns 8
        assertThat(cellMock.topRightEdge()).isEqualTo('╢')
        every {cellMock.rowIndex} returns 2
        every {cellMock.colIndex} returns 8
        assertThat(cellMock.topRightEdge()).isEqualTo('╢')
        every {cellMock.rowIndex} returns 4
        every {cellMock.colIndex} returns 8
        assertThat(cellMock.topRightEdge()).isEqualTo('╢')
        every {cellMock.rowIndex} returns 5
        every {cellMock.colIndex} returns 8
        assertThat(cellMock.topRightEdge()).isEqualTo('╢')
        every {cellMock.rowIndex} returns 7
        every {cellMock.colIndex} returns 8
        assertThat(cellMock.topRightEdge()).isEqualTo('╢')
        every {cellMock.rowIndex} returns 8
        every {cellMock.colIndex} returns 8
        assertThat(cellMock.topRightEdge()).isEqualTo('╢')

        // top is block border but not grid border, right is block border but not grid border
        every {cellMock.rowIndex} returns 3
        every {cellMock.colIndex} returns 2
        assertThat(cellMock.topRightEdge()).isEqualTo('╬')
        every {cellMock.rowIndex} returns 3
        every {cellMock.colIndex} returns 5
        assertThat(cellMock.topRightEdge()).isEqualTo('╬')
        every {cellMock.rowIndex} returns 6
        every {cellMock.colIndex} returns 2
        assertThat(cellMock.topRightEdge()).isEqualTo('╬')
        every {cellMock.rowIndex} returns 6
        every {cellMock.colIndex} returns 5
        assertThat(cellMock.topRightEdge()).isEqualTo('╬')

        // top is not block border and not grid border, right is block border but not grid border
        every {cellMock.rowIndex} returns 1
        every {cellMock.colIndex} returns 2
        assertThat(cellMock.topRightEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 2
        every {cellMock.colIndex} returns 2
        assertThat(cellMock.topRightEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 4
        every {cellMock.colIndex} returns 2
        assertThat(cellMock.topRightEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 5
        every {cellMock.colIndex} returns 2
        assertThat(cellMock.topRightEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 7
        every {cellMock.colIndex} returns 2
        assertThat(cellMock.topRightEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 8
        every {cellMock.colIndex} returns 2
        assertThat(cellMock.topRightEdge()).isEqualTo('╫')

        every {cellMock.rowIndex} returns 1
        every {cellMock.colIndex} returns 5
        assertThat(cellMock.topRightEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 2
        every {cellMock.colIndex} returns 5
        assertThat(cellMock.topRightEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 4
        every {cellMock.colIndex} returns 5
        assertThat(cellMock.topRightEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 5
        every {cellMock.colIndex} returns 5
        assertThat(cellMock.topRightEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 7
        every {cellMock.colIndex} returns 5
        assertThat(cellMock.topRightEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 8
        every {cellMock.colIndex} returns 5
        assertThat(cellMock.topRightEdge()).isEqualTo('╫')

        val gridSize = cellMock.grid.gridSize
        val blockSize = cellMock.grid.blockSize
        // no block border or grid borders
        for (x in 0 until gridSize) {
            if ((x+1) % blockSize == 0) {
                continue
            }
            for (y in 0 until gridSize) {
                if ((y+1) % blockSize == 0) {
                    continue
                }
                every {cellMock.colIndex} returns x
                every {cellMock.rowIndex} returns y
            }
            assertThat(cellMock.topRightEdge())
                    .`as`("Failure for colIndex=${cellMock.colIndex} and rowIndex=${cellMock.rowIndex}")
                    .isEqualTo('┼')
        }
    }

    @Test
    fun `Cell - bottomLeftEdge`() {
        // given: blockSize = 3, gridSize = 9

        // bottom left cell
        every {cellMock.rowIndex} returns 8
        every {cellMock.colIndex} returns 0
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╚')

        // bottom cell, left border is block border
        every {cellMock.rowIndex} returns 8
        every {cellMock.colIndex} returns 3
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╩')
        every {cellMock.rowIndex} returns 8
        every {cellMock.colIndex} returns 6
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╩')

        // bottom cell, left border is not a block border
        every {cellMock.rowIndex} returns 8
        every {cellMock.colIndex} returns 1
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╧')
        every {cellMock.rowIndex} returns 8
        every {cellMock.colIndex} returns 2
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╧')
        every {cellMock.rowIndex} returns 8
        every {cellMock.colIndex} returns 4
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╧')
        every {cellMock.rowIndex} returns 8
        every {cellMock.colIndex} returns 5
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╧')
        every {cellMock.rowIndex} returns 8
        every {cellMock.colIndex} returns 7
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╧')
        every {cellMock.rowIndex} returns 8
        every {cellMock.colIndex} returns 8
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╧')

        // bottom is block border but not grid border, left is grid border
        every {cellMock.rowIndex} returns 2
        every {cellMock.colIndex} returns 0
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╠')
        every {cellMock.rowIndex} returns 5
        every {cellMock.colIndex} returns 0
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╠')

        // bottom is not block border, left is grid border
        every {cellMock.rowIndex} returns 0
        every {cellMock.colIndex} returns 0
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╟')
        every {cellMock.rowIndex} returns 1
        every {cellMock.colIndex} returns 0
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╟')
        every {cellMock.rowIndex} returns 3
        every {cellMock.colIndex} returns 0
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╟')
        every {cellMock.rowIndex} returns 4
        every {cellMock.colIndex} returns 0
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╟')
        every {cellMock.rowIndex} returns 6
        every {cellMock.colIndex} returns 0
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╟')
        every {cellMock.rowIndex} returns 7
        every {cellMock.colIndex} returns 0
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╟')
        
        // bottom is block border but not grid border, left is block border but not grid border
        every {cellMock.rowIndex} returns 2
        every {cellMock.colIndex} returns 3
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╬')
        every {cellMock.rowIndex} returns 2
        every {cellMock.colIndex} returns 6
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╬')
        every {cellMock.rowIndex} returns 5
        every {cellMock.colIndex} returns 3
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╬')
        every {cellMock.rowIndex} returns 5
        every {cellMock.colIndex} returns 6
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╬')

        // bottom is not block border and not grid border, left is block border but not grid border
        every {cellMock.rowIndex} returns 0
        every {cellMock.colIndex} returns 3
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 1
        every {cellMock.colIndex} returns 3
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 3
        every {cellMock.colIndex} returns 3
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 4
        every {cellMock.colIndex} returns 3
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 6
        every {cellMock.colIndex} returns 3
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 7
        every {cellMock.colIndex} returns 3
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╫')

        every {cellMock.rowIndex} returns 0
        every {cellMock.colIndex} returns 6
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 1
        every {cellMock.colIndex} returns 6
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 3
        every {cellMock.colIndex} returns 6
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 4
        every {cellMock.colIndex} returns 6
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 6
        every {cellMock.colIndex} returns 6
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 7
        every {cellMock.colIndex} returns 6
        assertThat(cellMock.bottomLeftEdge()).isEqualTo('╫')

        val gridSize = cellMock.grid.gridSize
        val blockSize = cellMock.grid.blockSize
        // no block border or grid borders
        for (x in 0 until gridSize) {
            if (x % blockSize == 0) {
                continue
            }
            for (y in 0 until gridSize) {
                if ((y+1) % blockSize == 0) {
                    continue
                }
                every {cellMock.colIndex} returns x
                every {cellMock.rowIndex} returns y
            }
            assertThat(cellMock.bottomLeftEdge())
                    .`as`("Failure for colIndex=${cellMock.colIndex} and rowIndex=${cellMock.rowIndex}")
                    .isEqualTo('┼')
        }
    }

    @Test
    fun `Cell - bottomRightEdge`() {
        // given: blockSize = 3, gridSize = 9

        // bottom right cell
        every {cellMock.rowIndex} returns 8
        every {cellMock.colIndex} returns 8
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╝')

        // bottom cell, right border is block border
        every {cellMock.rowIndex} returns 8
        every {cellMock.colIndex} returns 2
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╩')
        every {cellMock.rowIndex} returns 8
        every {cellMock.colIndex} returns 5
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╩')

        // bottom cell, right border is not a block border
        every {cellMock.rowIndex} returns 8
        every {cellMock.colIndex} returns 0
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╧')
        every {cellMock.rowIndex} returns 8
        every {cellMock.colIndex} returns 1
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╧')
        every {cellMock.rowIndex} returns 8
        every {cellMock.colIndex} returns 3
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╧')
        every {cellMock.rowIndex} returns 8
        every {cellMock.colIndex} returns 4
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╧')
        every {cellMock.rowIndex} returns 8
        every {cellMock.colIndex} returns 6
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╧')
        every {cellMock.rowIndex} returns 8
        every {cellMock.colIndex} returns 7
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╧')

        // bottom is block border but not grid border, right is grid border
        every {cellMock.rowIndex} returns 2
        every {cellMock.colIndex} returns 8
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╣')
        every {cellMock.rowIndex} returns 5
        every {cellMock.colIndex} returns 8
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╣')

        // bottom is not block border, right is grid border
        every {cellMock.rowIndex} returns 0
        every {cellMock.colIndex} returns 8
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╢')
        every {cellMock.rowIndex} returns 1
        every {cellMock.colIndex} returns 8
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╢')
        every {cellMock.rowIndex} returns 3
        every {cellMock.colIndex} returns 8
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╢')
        every {cellMock.rowIndex} returns 4
        every {cellMock.colIndex} returns 8
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╢')
        every {cellMock.rowIndex} returns 6
        every {cellMock.colIndex} returns 8
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╢')
        every {cellMock.rowIndex} returns 7
        every {cellMock.colIndex} returns 8
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╢')

        // bottom is block border but not grid border, right is block border but not grid border
        every {cellMock.rowIndex} returns 2
        every {cellMock.colIndex} returns 2
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╬')
        every {cellMock.rowIndex} returns 2
        every {cellMock.colIndex} returns 5
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╬')
        every {cellMock.rowIndex} returns 5
        every {cellMock.colIndex} returns 2
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╬')
        every {cellMock.rowIndex} returns 5
        every {cellMock.colIndex} returns 5
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╬')

        // bottom is not block border and not grid border, right is block border but not grid border
        every {cellMock.rowIndex} returns 0
        every {cellMock.colIndex} returns 2
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 1
        every {cellMock.colIndex} returns 2
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 3
        every {cellMock.colIndex} returns 2
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 4
        every {cellMock.colIndex} returns 2
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 6
        every {cellMock.colIndex} returns 2
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 7
        every {cellMock.colIndex} returns 2
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╫')

        every {cellMock.rowIndex} returns 0
        every {cellMock.colIndex} returns 5
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 1
        every {cellMock.colIndex} returns 5
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 3
        every {cellMock.colIndex} returns 5
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 4
        every {cellMock.colIndex} returns 5
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 6
        every {cellMock.colIndex} returns 5
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╫')
        every {cellMock.rowIndex} returns 7
        every {cellMock.colIndex} returns 5
        assertThat(cellMock.bottomRightEdge()).isEqualTo('╫')

        val gridSize = cellMock.grid.gridSize
        val blockSize = cellMock.grid.blockSize
        // no block border or grid borders
        for (x in 0 until gridSize) {
            if ((x+1) % blockSize == 0) {
                continue
            }
            for (y in 0 until gridSize) {
                if ((y+1) % blockSize == 0) {
                    continue
                }
                every {cellMock.colIndex} returns x
                every {cellMock.rowIndex} returns y
            }
            assertThat(cellMock.bottomRightEdge())
                    .`as`("Failure for colIndex=${cellMock.colIndex} and rowIndex=${cellMock.rowIndex}")
                    .isEqualTo('┼')
        }
    }
}
