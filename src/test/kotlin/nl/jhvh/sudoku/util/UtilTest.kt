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

    @Test
    fun intRangeSet() {
        assertThat(intRangeSet(0, 4)).isEqualTo(sortedSetOf(0, 1, 2, 3, 4))
        assertThat(intRangeSet(2, 5)).isEqualTo(sortedSetOf(2, 3, 4, 5))
        assertThat(intRangeSet(0, 0)).isEqualTo(setOf(0))
        assertThat(intRangeSet(1, 1)).isEqualTo(setOf(1))
        assertThat(intRangeSet(5, 1)).isEmpty()
        assertThat(intRangeSet(0, 500))
                .startsWith(0)
                .endsWith(500)
                .hasSize(501)

        val intRangeSet2_5 = intRangeSet(2, 5)
        assertThat(intRangeSet2_5 is MutableSet).isTrue()
        val wouldBeMutableSet = intRangeSet2_5 as MutableSet
        // although it's type says it's mutable, you can not really modify it:
        assertFailsWith<UnsupportedOperationException> { wouldBeMutableSet.add(-1) }
    }
}
