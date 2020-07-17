package nl.jhvh.test.sudoku.format

import nl.jhvh.sudoku.format.concatAlignEach
import nl.jhvh.sudoku.format.concatEach
import nl.jhvh.sudoku.format.toTextLines
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException
import java.math.BigDecimal
import kotlin.test.assertFailsWith

class ListElementConcatTest {

    @Test
    fun `test correct concatenation of equal sized String Lists`() {
        // given
        val list1 = listOf("IntelliJ ", "Eclipse ", "NetBeans ", "CodeEnvy ")
        val list2 = listOf("JetBrains", "Eclipse Foundation", "Apache", "CodeEnvy")

        // when, then
        assertThat(list1 concatEach list2).isEqualTo(listOf(
                "IntelliJ JetBrains",
                "Eclipse Eclipse Foundation",
                "NetBeans Apache",
                "CodeEnvy CodeEnvy"
        ))
    }

    @Test
    fun `test correct concatenation and alignment of equal sized String Lists`() {
        // given
        val list1 = listOf("JetBrains ", "Eclipse Foundation ", "Apache ")
        val list2 = listOf("IntelliJ IDEA", "Eclipse", "NetBeans")

        // when, then
        assertThat(list1 concatAlignEach list2).isEqualTo(listOf(
                "JetBrains          IntelliJ IDEA",
                "Eclipse Foundation Eclipse      ",
                "Apache             NetBeans     "
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
    fun `test correct concatenation and alignment of equal sized non-String Lists`() {
        // given
        val list1 = listOf(BigDecimal.valueOf(12345.54321), BigDecimal.valueOf(1), BigDecimal.TEN, BigDecimal.valueOf(1.toDouble()/16.toDouble()))
        val list2 = listOf(" IntelliJ IDEA", " Eclipse", " NetBeans", " CodeEnvy")

        // when, then
        assertThat(list1 concatAlignEach list2).isEqualTo(listOf(
                "12345.54321 IntelliJ IDEA",
                "1           Eclipse      ",
                "10          NetBeans     ",
                "0.0625      CodeEnvy     "
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
        let { assertFailsWith<IllegalArgumentException> { list1 concatAlignEach  list2 } }
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
    fun `test correct concatenation and alignment of equal sized non-String Lists with vararg`() {
        // given
        val list1 = listOf(BigDecimal.valueOf(12345.54321), BigDecimal.valueOf(1), BigDecimal.TEN, BigDecimal.valueOf(1.toDouble()/16.toDouble()))
        val list2 = listOf(" ", " ", " ", " ")
        val list3 = listOf(1, 2, 3, 4)
        val list4 = listOf(" IntelliJ IDEA", " Eclipse", " NetBeans", " CodeEnvy")

        // when, then
        assertThat(concatAlignEach(list1, list2, list3, list4)).isEqualTo(listOf(
                "12345.54321 1 IntelliJ IDEA",
                "1           2 Eclipse      ",
                "10          3 NetBeans     ",
                "0.0625      4 CodeEnvy     "
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
        let { assertFailsWith<IllegalArgumentException> { concatAlignEach(list1, list2, list3) } }
                .also { assertThat(it.message)
                        .isEqualTo("Both collections must have equal sizes! Sizes: left=${list1.size}, right=${list2.size}") }
    }

    @Test
    fun `test correct output as text lines`() {
        val lineSep = System.lineSeparator()
        assertThat(listOf("JetBrains", "Eclipse Foundation", "Apache", "CodeEnvy").toTextLines())
                .isEqualTo("JetBrains${lineSep}Eclipse Foundation${lineSep}Apache${lineSep}CodeEnvy$lineSep")
        assertThat(listOf(1, 2, 3, 4, 5).toTextLines())
                .isEqualTo("1${lineSep}2${lineSep}3${lineSep}4${lineSep}5$lineSep")
    }
}
