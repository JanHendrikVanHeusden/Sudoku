package nl.jhvh.sudoku.grid.model.segment

import nl.jhvh.sudoku.grid.GridTestBase
import nl.jhvh.sudoku.grid.model.Grid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class BlockTest: GridTestBase() {

    /** grid mock and cell mocks initialized in [GridTestBase.gridSetUp] */
    override lateinit var gridMock: Grid
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
    fun getCells() {
        for (leftColIndex in listOf(0, 3, 6)) {
            for (topRowIndex in listOf(0, 3, 6)) {
                val subject = Block(gridMock, leftColIndex, topRowIndex)
                subject.cells.forEachIndexed { index, cell ->
                    assertThat(index)
                            .`as`("index should be $index for: leftColIndex=$leftColIndex, topRowIndex=$topRowIndex, cell.colIndex=${cell.colIndex}, cell.rowIndex=${cell.rowIndex}")
                            .isEqualTo((cell.colIndex-leftColIndex)%blockSize + (cell.rowIndex-topRowIndex)*blockSize)
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
