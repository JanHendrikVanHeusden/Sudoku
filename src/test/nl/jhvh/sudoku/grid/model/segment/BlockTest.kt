package nl.jhvh.sudoku.grid.model.segment

import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.cell.Cell
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class BlockTest {

    lateinit var grid9Mock: Grid
    val grid9BlockSize = 3
    val grid9GridSize = 9
    val grid9MaxValue = grid9GridSize

    @BeforeEach
    fun setUp() {
        grid9Mock = mockk()
        every {grid9Mock.blockSize} returns grid9BlockSize
        every {grid9Mock.gridSize} returns grid9GridSize
        every {grid9Mock.maxValue} returns grid9GridSize

        val cellColIndexCapturer: CapturingSlot<Int> = slot()
        val cellRowIndexCapturer: CapturingSlot<Int> = slot()
        every {grid9Mock.findCell(capture(cellColIndexCapturer), capture(cellRowIndexCapturer))} answers {
            val cellMock: Cell = mockk()
            every { cellMock.grid } returns grid9Mock
            every {cellMock.colIndex} returns cellColIndexCapturer.captured
            every {cellMock.rowIndex} returns cellRowIndexCapturer.captured
            cellMock
        }

        val grid9CellList = mutableListOf<Cell>()
        for (x in 0..grid9GridSize-1) {
            for (y in 0.. grid9GridSize-1) {
                grid9CellList.add(grid9Mock.findCell(x, y))
            }
        }
        every {grid9Mock.cellList} returns grid9CellList
    }

    @Test
    fun getRightColIndex() {
        for (leftColIndex in listOf(0, 3, 6)) {
            assertThat(Block(grid9Mock, leftColIndex, Random.nextInt(0, grid9GridSize)).rightColIndex)
                    .isEqualTo(leftColIndex + grid9BlockSize - 1)
        }
    }

    @Test
    fun getBottomRowIndex() {
        for (topRowIndex in listOf(0, 3, 6)) {
            assertThat(Block(grid9Mock, Random.nextInt(0, grid9GridSize), topRowIndex).bottomRowIndex)
                    .isEqualTo(topRowIndex + grid9BlockSize - 1)
        }
    }

    @Test
    fun getCellList() {
        for (leftColIndex in listOf(0, 3, 6)) {
            for (topRowIndex in listOf(0, 3, 6)) {
                val subject = Block(grid9Mock, leftColIndex, topRowIndex)
                subject.cellList.forEachIndexed { index, cell ->
                    assertThat(index)
                            .`as`("index should be $index for: leftColIndex=$leftColIndex, topRowIndex=$topRowIndex, cell.colIndex=${cell.colIndex}, cell.rowIndex=${cell.rowIndex}")
                            .isEqualTo((cell.colIndex-leftColIndex)%grid9BlockSize + (cell.rowIndex-topRowIndex)*grid9BlockSize)
                    assertThat(subject.containsCell(cell)).isTrue()
                    assertThat(subject.containsCell(cell.colIndex, cell.rowIndex)).isTrue()
                }
            }
        }
    }

    @Test
    fun `containsCell - by Cell`() {
        for (cell in grid9Mock.cellList) {
            for (leftColIndex in listOf(0, 3, 6)) {
                for (topRowIndex in listOf(0, 3, 6)) {
                    val subject = Block(grid9Mock, leftColIndex, topRowIndex)
                    val shouldContainCellByIndex =
                            cell.colIndex in leftColIndex..leftColIndex+grid9BlockSize-1
                                && cell.rowIndex in topRowIndex..topRowIndex+grid9BlockSize-1
                    assertThat(subject.containsCell(cell))
                            .`as`("Expected shouldContainCellByIndex=$shouldContainCellByIndex for leftColIndex=$leftColIndex, topRowIndex=$topRowIndex, cell.colIndex=${cell.colIndex}, cell.rowIndex=${cell.rowIndex}")
                            .isEqualTo(shouldContainCellByIndex)
                }
            }
        }
    }

    @Test
    fun `containsCell - by indices`() {
        for (leftColIndex in listOf(0, 3, 6)) {
            for (topRowIndex in listOf(0, 3, 6)) {
                val subject = Block(grid9Mock, leftColIndex, topRowIndex)
                for (colIndex in 0..grid9GridSize - 1) {
                    for (rowIndex in 0..grid9GridSize - 1) {
                        val inBlock = colIndex in subject.leftColIndex..subject.rightColIndex
                                && rowIndex in subject.topRowIndex..subject.bottomRowIndex
                        assertThat(subject.containsCell(colIndex, rowIndex)).isEqualTo(inBlock)
                    }
                }
            }
        }
    }

    @Test
    fun testToString() {
        val subject = Block(grid9Mock, 6, 3)
        assertThat(subject.toString()).contains("${Block::class.simpleName}:", "[leftColIndex=${subject.leftColIndex}]", "[rightColIndex=${subject.rightColIndex}]", "[upperRowIndex=${subject.topRowIndex}], [bottomRowIndex=${subject.bottomRowIndex}]")
    }

}
