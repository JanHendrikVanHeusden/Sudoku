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

    @Test
    fun `test that FormattableList equals List of String with same content`() {
        val formattableList = FormattableList(listOf("Mary", "William", "Jane", "Harry"))
        // equal to itself
        assertThat(formattableList.equals(formattableList)).isTrue()
        // equal to another FormattableList with same content
        assertThat(FormattableList(listOf("Mary", "William", "Jane", "Harry")).equals(FormattableList(listOf("Mary", "William", "Jane", "Harry"))))
                .isTrue()
        // not equal to FormattableList with same elements but in different order
        assertThat(FormattableList(listOf("Mary", "William", "Jane", "Harry")).equals(FormattableList(listOf("William", "Mary", "Jane", "Harry"))))
                .isFalse()
        // equal to another List with same content...
        assertThat(FormattableList(listOf("Mary", "William", "Jane", "Harry")).equals(listOf("Mary", "William", "Jane", "Harry")))
                .isTrue()
        // ... and also the other way around
        assertThat(listOf("Mary", "William", "Jane", "Harry").equals(FormattableList(listOf("Mary", "William", "Jane", "Harry"))))
                .isTrue()
        // equal to ArrayList with same content, both sides
        assertThat(ArrayList(listOf("Mary", "William", "Jane", "Harry")).equals(FormattableList(listOf("Mary", "William", "Jane", "Harry"))))
                .isTrue()
        assertThat(FormattableList(listOf("Mary", "William", "Jane", "Harry")).equals(ArrayList(listOf("Mary", "William", "Jane", "Harry"))))
                .isTrue()
    }

    @Test
    fun `test that FormattableList hashCode is same as List of String with same content`() {
        val formattableList = FormattableList(listOf("Mary", "William", "Jane", "Harry"))
        // same hashCode when called again on itself
        assertThat(formattableList.hashCode() == formattableList.hashCode()).isTrue()
        // equal to hashcode of another FormattableList with same content
        assertThat(FormattableList(listOf("Mary", "William", "Jane", "Harry")).hashCode() == FormattableList(listOf("Mary", "William", "Jane", "Harry")).hashCode())
                .isTrue()
        // not equal to hashcode of FormattableList with same elements but in different order
        assertThat(FormattableList(listOf("Mary", "William", "Jane", "Harry")).hashCode() == FormattableList(listOf("William", "Mary", "Jane", "Harry")).hashCode())
                .isFalse()
        // equal to hashcode of another List with same content...
        assertThat(FormattableList(listOf("Mary", "William", "Jane", "Harry")).hashCode() == listOf("Mary", "William", "Jane", "Harry").hashCode())
                .isTrue()
        // equal to hashcode of ArrayList with same content, both sides
        assertThat(ArrayList(listOf("Mary", "William", "Jane", "Harry")).hashCode() == FormattableList(listOf("Mary", "William", "Jane", "Harry")).hashCode())
                .isTrue()
    }

}
