package nl.jhvh.sudoku.grid.model.segment

import nl.jhvh.sudoku.grid.model.Grid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class BlockTest: AbstractGridSegmentTest() {

    override lateinit var gridMock: Grid // initialized in setUp() of AbstractGridSegmentTest
    override val blockSize = 3
    override val gridSize = 9

    @Test
    fun getRightColIndex() {
        for (leftColIndex in listOf(0, 3, 6)) {
            assertThat(Block(gridMock, leftColIndex, Random.nextInt(0, gridSize)).rightColIndex)
                    .isEqualTo(leftColIndex + blockSize - 1)
        }
    }

    @Test
    fun getBottomRowIndex() {
        for (topRowIndex in listOf(0, 3, 6)) {
            assertThat(Block(gridMock, Random.nextInt(0, gridSize), topRowIndex).bottomRowIndex)
                    .isEqualTo(topRowIndex + blockSize - 1)
        }
    }

    @Test
    fun getCellList() {
        for (leftColIndex in listOf(0, 3, 6)) {
            for (topRowIndex in listOf(0, 3, 6)) {
                val subject = Block(gridMock, leftColIndex, topRowIndex)
                subject.cellList.forEachIndexed { index, cell ->
                    assertThat(index)
                            .`as`("index should be $index for: leftColIndex=$leftColIndex, topRowIndex=$topRowIndex, cell.colIndex=${cell.colIndex}, cell.rowIndex=${cell.rowIndex}")
                            .isEqualTo((cell.colIndex-leftColIndex)%blockSize + (cell.rowIndex-topRowIndex)*blockSize)
                    assertThat(subject.containsCell(cell)).isTrue()
                    assertThat(subject.containsCell(cell.colIndex, cell.rowIndex)).isTrue()
                }
            }
        }
    }

    @Test
    fun `containsCell - by Cell`() {
        for (cell in gridMock.cellList) {
            for (leftColIndex in listOf(0, 3, 6)) {
                for (topRowIndex in listOf(0, 3, 6)) {
                    val subject = Block(gridMock, leftColIndex, topRowIndex)
                    val shouldContainCellByIndex =
                            cell.colIndex in leftColIndex..leftColIndex+blockSize-1
                                && cell.rowIndex in topRowIndex..topRowIndex+blockSize-1
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
                val subject = Block(gridMock, leftColIndex, topRowIndex)
                for (colIndex in 0..gridSize - 1) {
                    for (rowIndex in 0..gridSize - 1) {
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
        val subject = Block(gridMock, 6, 3)
        assertThat(subject.toString()).contains("${Block::class.simpleName}:", "[leftColIndex=${subject.leftColIndex}]", "[rightColIndex=${subject.rightColIndex}]", "[upperRowIndex=${subject.topRowIndex}], [bottomRowIndex=${subject.bottomRowIndex}]")
    }

}
