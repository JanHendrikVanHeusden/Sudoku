package nl.jhvh.sudoku.util

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
