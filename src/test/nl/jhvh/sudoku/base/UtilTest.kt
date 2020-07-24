package nl.jhvh.sudoku.base

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class UtilTest {

    @Test
    fun `test zero based IntArray`() {
        assertThat(incrementFromZero(3)).isEqualTo(listOf(0, 1, 2))
        assertThat(incrementFromZero(0)).isEmpty()
        assertThat(incrementFromZero(1)).isEqualTo(listOf(0))
        assertFailsWith<IllegalArgumentException> { incrementFromZero(-10)}
        assertThat(incrementFromZero(500))
                .startsWith(0)
                .endsWith(499)
                .hasSize(500)
    }
}
