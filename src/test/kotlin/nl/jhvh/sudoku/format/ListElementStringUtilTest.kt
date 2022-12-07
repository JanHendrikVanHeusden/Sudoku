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

import nl.jhvh.sudoku.util.alignCenter
import nl.jhvh.sudoku.util.alignLeft
import nl.jhvh.sudoku.util.alignRight
import nl.jhvh.sudoku.util.concatAlignCenter
import nl.jhvh.sudoku.util.concatAlignLeft
import nl.jhvh.sudoku.util.concatAlignRight
import nl.jhvh.sudoku.util.concatEach
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertFailsWith

class ListElementStringUtilTest {

    @Test
    fun `test correct left alignment`() {
        assertThat(listOf("JetBrains", "Eclipse Foundation", "Apache", "CodeEnvy").alignLeft())
                .isEqualTo(listOf(
                        "JetBrains         ",
                        "Eclipse Foundation",
                        "Apache            ",
                        "CodeEnvy          "
                ))
        assertThat(listOf("JetBrains", "Eclipse Foundation", "Apache", "CodeEnvy").alignLeft(1, 3))
                .isEqualTo(listOf(
                        " JetBrains            ",
                        " Eclipse Foundation   ",
                        " Apache               ",
                        " CodeEnvy             "
                ))
        assertThat(listOf(1, 10, 100).alignLeft(2, 1))
            .isEqualTo(listOf(
                "  1   ",
                "  10  ",
                "  100 "
            ))
    }

    @Test
    fun `test correct right alignment`() {
        assertThat(listOf("JetBrains", "Eclipse Foundation", "Apache", "CodeEnvy").alignRight())
                .isEqualTo(listOf(
                        "         JetBrains",
                        "Eclipse Foundation",
                        "            Apache",
                        "          CodeEnvy"
                ))
        assertThat(listOf(1, 10, 100).alignRight(2, 1))
                .isEqualTo(listOf(
                        "    1 ",
                        "   10 ",
                        "  100 "
                ))
    }

    @Test
    fun `test correct center alignment`() {
        assertThat(listOf("JetBrains", "Eclipse Foundation", "Apache").alignCenter())
                .isEqualTo(listOf(
                        "    JetBrains     ",
                        "Eclipse Foundation",
                        "      Apache      "
                ))

        assertThat(listOf(1, 10, 100).alignCenter(2, 3))
                .isEqualTo(listOf(
                        "   1    ",
                        "  10    ",
                        "  100   "
                ))
    }

    @Test
    fun `test correct concatenation of equal sized String Lists`() {
        // given
        val list1 = mutableListOf("IntelliJ ", "Eclipse ", "NetBeans ", "CodeEnvy ")
        val list2 = mutableListOf("JetBrains", "Eclipse Foundation", "Apache", "CodeEnvy")

        // when
        // assert result
        assertThat(list1 concatEach list2).isEqualTo(listOf(
                "IntelliJ JetBrains",
                "Eclipse Eclipse Foundation",
                "NetBeans Apache",
                "CodeEnvy CodeEnvy"
        ))
        // assert that input lists are not affected
        assertThat(list1).isEqualTo(listOf("IntelliJ ", "Eclipse ", "NetBeans ", "CodeEnvy "))
        assertThat(list2).isEqualTo(listOf("JetBrains", "Eclipse Foundation", "Apache", "CodeEnvy"))
    }

    @Test
    fun `test correct concatenation and left alignment of equal sized String Lists`() {
        // given
        val list1 = listOf("JetBrains ", "Eclipse Foundation ", "Apache ")
        val list2 = listOf("IntelliJ IDEA", "Eclipse", "NetBeans")

        // when, then
        assertThat(list1 concatAlignLeft list2).isEqualTo(listOf(
                "JetBrains          IntelliJ IDEA",
                "Eclipse Foundation Eclipse      ",
                "Apache             NetBeans     "
        ))
    }

    @Test
    fun `test correct concatenation and right alignment of equal sized String Lists`() {
        // given
        val list1 = listOf("JetBrains ", "Eclipse Foundation ", "Apache ")
        val list2 = listOf("IntelliJ IDEA", "Eclipse", "NetBeans")

        // when, then
        assertThat(list1 concatAlignRight list2).isEqualTo(listOf(
                "         JetBrains IntelliJ IDEA",
                "Eclipse Foundation       Eclipse",
                "            Apache      NetBeans"
        ))
    }

    @Test
    fun `test correct concatenation and center alignment of equal sized String Lists`() {
        // given
        val list1 = listOf("JetBrains ", "Eclipse Foundation ", "Apache ")
        val list2 = listOf("IntelliJ IDEA", "Eclipse", "NetBeans")

        // when, then
        assertThat(list1 concatAlignCenter list2).isEqualTo(listOf(
                "    JetBrains      IntelliJ IDEA",
                "Eclipse Foundation    Eclipse   ",
                "      Apache         NetBeans   "
        ))
    }

    @Test
    fun `test correct concatenation of equal sized non-String Lists`() {
        // given
        val list1 = listOf(1, 2, 3, 4)
        val list2 = listOf("JetBrains", "Eclipse Foundation", "Apache", "CodeEnvy")

        // when, then
        assertThat(list1 concatEach list2).isEqualTo(listOf(
                "1JetBrains",
                "2Eclipse Foundation",
                "3Apache",
                "4CodeEnvy"
        ))
    }

    @Test
    fun `test correct concatenation and left alignment of equal sized non-String Lists`() {
        // given
        val list1 = listOf(BigDecimal.valueOf(12345.54321), BigDecimal.valueOf(1), BigDecimal.TEN, BigDecimal.valueOf(1.toDouble()/16.toDouble()))
        val list2 = listOf(" IntelliJ IDEA", " Eclipse", " NetBeans", " CodeEnvy")

        // when, then
        assertThat(list1 concatAlignLeft list2).isEqualTo(listOf(
                "12345.54321 IntelliJ IDEA",
                "1           Eclipse      ",
                "10          NetBeans     ",
                "0.0625      CodeEnvy     "
        ))
    }

    @Test
    fun `test correct concatenation and right alignment of equal sized non-String Lists`() {
        // given
        val list1 = listOf(BigDecimal.valueOf(12345.54321), BigDecimal.valueOf(1), BigDecimal.TEN, BigDecimal.valueOf(1.toDouble()/16.toDouble()))
        val list2 = listOf(" IntelliJ IDEA", " Eclipse", " NetBeans", " CodeEnvy")

        // when, then
        assertThat(list1 concatAlignRight list2).isEqualTo(listOf(
                "12345.54321 IntelliJ IDEA",
                "          1       Eclipse",
                "         10      NetBeans",
                "     0.0625      CodeEnvy"
        ))
    }

    @Test
    fun `test IllegalArgumentException when Lists of different sizes`() {
        // given
        val list1 = listOf("IntelliJ ", "Eclipse ", "NetBeans ")
        val list2 = listOf("JetBrains", "Eclipse Foundation", "Apache", "CodeEnvy")

        // when, then
        let { assertFailsWith<IllegalArgumentException> { list1 concatEach list2 } }
                .also { assertThat(it.message)
                        .isEqualTo("Both collections must have equal sizes! Sizes: left=${list1.size}, right=${list2.size}") }
        let { assertFailsWith<IllegalArgumentException> { list1 concatAlignLeft  list2 } }
                .also { assertThat(it.message)
                        .isEqualTo("Both collections must have equal sizes! Sizes: left=${list1.size}, right=${list2.size}") }
    }

    @Test
    fun `test correct concatenation of equal sized non-String Lists with vararg`() {
        // given
        val list1 = listOf(BigDecimal.valueOf(12345.54321), BigDecimal.valueOf(1), BigDecimal.TEN, BigDecimal.valueOf(1.toDouble()/16.toDouble()))
        val list2 = listOf(" ", " ", " ", " ")
        val list3 = listOf(1, 2, 3, 4)
        val list4 = listOf(" IntelliJ IDEA", " Eclipse", " NetBeans", " CodeEnvy")

        // when, then
        assertThat(concatEach(list1, list2, list3, list4)).isEqualTo(listOf(
                "12345.54321 1 IntelliJ IDEA",
                "1 2 Eclipse",
                "10 3 NetBeans",
                "0.0625 4 CodeEnvy"
        ))
    }

    @Test
    fun `test correct concatenation and left alignment of equal sized non-String Lists with vararg`() {
        // given
        val list1 = listOf(BigDecimal.valueOf(12345.54321), BigDecimal.valueOf(1), BigDecimal.TEN, BigDecimal.valueOf(1.toDouble()/16.toDouble()))
        val list2 = listOf(" ", " ", " ", " ")
        val list3 = listOf(1, 2, 3, 4)
        val list4 = listOf(" IntelliJ IDEA", " Eclipse", " NetBeans", " CodeEnvy")

        // when, then
        assertThat(concatAlignLeft(list1, list2, list3, list4)).isEqualTo(listOf(
                "12345.54321 1 IntelliJ IDEA",
                "1           2 Eclipse      ",
                "10          3 NetBeans     ",
                "0.0625      4 CodeEnvy     "
        ))
    }

    @Test
    fun `test correct concatenation and center alignment of equal sized String Lists with vararg`() {
        // given
        val list1 = listOf(BigDecimal.valueOf(12345.54321), BigDecimal.valueOf(1), BigDecimal.TEN, BigDecimal.valueOf(1.toDouble()/16.toDouble()))
        val list2 = listOf(" ", " ", " ", " ")
        val list3 = listOf(1, 2, 3, 4)
        val list4 = listOf(" IntelliJ IDEA", " Eclipse", " NetBeans", " CodeEnvy")

        // when, then
        assertThat(concatAlignCenter(list1, list2, list3, list4)).isEqualTo(listOf(
                "12345.54321 1 IntelliJ IDEA",
                "     1      2    Eclipse   ",
                "    10      3   NetBeans   ",
                "  0.0625    4   CodeEnvy   "
        ))
    }

    @Test
    fun `test correct concatenation and right alignment of equal sized String Lists with vararg`() {
        // given
        val list1 = listOf(BigDecimal.valueOf(12345.54321), BigDecimal.valueOf(1), BigDecimal.TEN, BigDecimal.valueOf(1.toDouble()/16.toDouble()))
        val list2 = listOf(" ", " ", " ", " ")
        val list3 = listOf(1, 2, 3, 4)
        val list4 = listOf(" IntelliJ IDEA", " Eclipse", " NetBeans", " CodeEnvy")

        // when, then
        assertThat(concatAlignRight(list1, list2, list3, list4)).isEqualTo(listOf(
                "12345.54321 1 IntelliJ IDEA",
                "          1 2       Eclipse",
                "         10 3      NetBeans",
                "     0.0625 4      CodeEnvy"
        ))
    }

    @Test
    fun `test IllegalArgumentException when Lists of different sizes with vararg`() {
        // given
        val list1 = listOf(1, 2, 3, 4)
        val list2 = listOf("IntelliJ ", "Eclipse ", "NetBeans ")
        val list3 = listOf("JetBrains", "Eclipse Foundation", "Apache", "CodeEnvy")

        // when, then
        let { assertFailsWith<IllegalArgumentException> { concatEach(list1, list2, list3) } }
                .also { assertThat(it.message)
                        .isEqualTo("Both collections must have equal sizes! Sizes: left=${list1.size}, right=${list2.size}") }
        let { assertFailsWith<IllegalArgumentException> { concatAlignLeft(list1, list2, list3) } }
                .also { assertThat(it.message)
                        .isEqualTo("Both collections must have equal sizes! Sizes: left=${list1.size}, right=${list2.size}") }
    }

}
