package nl.jhvh.sudoku.base

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class DimensionTest {

    @Test
    fun testGridSize() {
        // regular values
        assertThat(gridSize(blockSize = 2)).isEqualTo(4)
        assertThat(gridSize(blockSize = 3)).isEqualTo(9)
        assertThat(gridSize(blockSize = 5)).isEqualTo(25)
        assertThat(gridSize(blockSize = 10)).isEqualTo(100)
        assertThat(gridSize(blockSize = 100)).isEqualTo(10_000)
        assertThat(gridSize(blockSize = MAX_BLOCK_SIZE)).isEqualTo(MAX_BLOCK_SIZE * MAX_BLOCK_SIZE)

        // Not allowable values
        assertFailsWith<IllegalArgumentException> { gridSize(blockSize = -1) }
        assertFailsWith<IllegalArgumentException> { gridSize(blockSize = 0) }
        assertFailsWith<IllegalArgumentException> { gridSize(blockSize = 1) }
        assertFailsWith<IllegalArgumentException> { gridSize(blockSize = -3) }
        assertFailsWith<IllegalArgumentException> { gridSize(blockSize = MAX_BLOCK_SIZE+1) }
        assertFailsWith<IllegalArgumentException> { gridSize(blockSize = Int.MAX_VALUE) }
        assertFailsWith<IllegalArgumentException> { gridSize(blockSize = Int.MIN_VALUE) }
        assertFailsWith<IllegalArgumentException> { gridSize(blockSize = 0) }
    }

    @Test
    fun testMaxValue() {
        assertThat(maxValue(blockSize = 2)).isEqualTo(4)
        assertThat(maxValue(blockSize = 3)).isEqualTo(9)
        assertThat(maxValue(blockSize = 5)).isEqualTo(25)
        assertThat(maxValue(blockSize = 10)).isEqualTo(100)
        assertThat(maxValue(blockSize = 100)).isEqualTo(10_000)
        assertThat(maxValue(blockSize = MAX_BLOCK_SIZE)).isEqualTo(MAX_BLOCK_SIZE * MAX_BLOCK_SIZE)

        // Not allowable values
        assertFailsWith<IllegalArgumentException> { maxValue(blockSize = 0) }
        assertFailsWith<IllegalArgumentException> { maxValue(blockSize = -1) }
        assertFailsWith<IllegalArgumentException> { maxValue(blockSize = -3) }
        assertFailsWith<IllegalArgumentException> { maxValue(blockSize = MAX_BLOCK_SIZE+1) }
        assertFailsWith<IllegalArgumentException> { maxValue(blockSize = Int.MAX_VALUE) }
        assertFailsWith<IllegalArgumentException> { maxValue(blockSize = Int.MIN_VALUE) }
        assertFailsWith<IllegalArgumentException> { maxValue(blockSize = 0) }
    }
}