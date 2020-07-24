package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.Formattable.FormattableList
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/** Unit tests for [FormattableList] */
class FormattableListTest {

    @Test
    fun `test FormattableList toString`() {
        val formattableList = FormattableList(listOf("Mary", "William", "Jane", "Harry"))
        val lineSep = System.lineSeparator()
        assertThat(formattableList.toString()).isEqualTo("Mary${lineSep}William${lineSep}Jane${lineSep}Harry")
    }

    @Test
    fun `test FormattableList is not mutable`() {
        val formattableList = FormattableList(emptyList())
        @Suppress("USELESS_IS_CHECK")
        assertThat(formattableList is List<String>)
                .`as`("Should extend List<String>")
                .isTrue()
        val stringListClass: Class<List<String>> = listOf("").javaClass
        assertThat(stringListClass.isAssignableFrom(formattableList.javaClass))
                .`as`("Should be a List<String>")
                .isFalse()
        val mutableStringListClass: Class<MutableList<String>> = mutableListOf("").javaClass
        assertThat(mutableStringListClass.isAssignableFrom(formattableList.javaClass))
                .`as`("Should not be a MutableList")
                .isFalse()
    }
}
