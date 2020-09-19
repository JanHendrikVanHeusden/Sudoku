package nl.jhvh.sudoku.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class UtilTest {

    @Test
    fun incrementFromZero() {
        assertThat(incrementFromZero(5)).isEqualTo(listOf(0, 1, 2, 3, 4))
        assertThat(incrementFromZero(0)).isEmpty()
        assertThat(incrementFromZero(1)).isEqualTo(listOf(0))
        assertFailsWith<IllegalArgumentException> { incrementFromZero(-1) }
        assertFailsWith<IllegalArgumentException> { incrementFromZero(-10) }
        assertThat(incrementFromZero(500))
                .startsWith(0)
                .endsWith(499)
                .hasSize(500)

        val incrementFromZero_5 = incrementFromZero(5)
        assertThat(incrementFromZero_5 is MutableList).isTrue()
        val wouldBeMutableList = incrementFromZero_5 as MutableList
        // although it's type says it's mutable, you can not really modify it:
        assertFailsWith<UnsupportedOperationException> { wouldBeMutableList.add(14) }
    }

}
