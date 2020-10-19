package nl.jhvh.sudoku.grid.model.cell

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

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.event.ValueEventType
import nl.jhvh.sudoku.grid.model.cell.CellValue.FixedValue
import nl.jhvh.sudoku.grid.model.cell.CellValue.NonFixedValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CellValueTest {

    private lateinit var cellMock: Cell
    private val gridSize = 9
    private val maxValue = gridSize

    @BeforeEach
    fun setUp() {
        cellMock = mockk(relaxed = true)
        every {cellMock.grid.maxValue} returns maxValue
    }

    @Test
    fun `eventListeners should be thread safe`() {
        var cellValue: CellValue = FixedValue(cellMock, 3)
        // assert type of the map (Map<ValueEventType, Set<ValueEventListener>)
        assertThat(cellValue.eventListeners.javaClass.name)
                .isEqualTo("java.util.concurrent.ConcurrentHashMap")
        ValueEventType.values().forEach {
            // for each entry: assert type of the map value (Set<ValueEventListener>)
            assertThat(cellValue.eventListeners[it]!!.javaClass.name)
                    .isEqualTo("java.util.concurrent.ConcurrentHashMap\$KeySetView")
        }

        cellValue = NonFixedValue(cellMock)
        // assert type of the map (Map<ValueEventType, Set<ValueEventListener>)
        assertThat(cellValue.eventListeners.javaClass.name)
                .isEqualTo("java.util.concurrent.ConcurrentHashMap")
        ValueEventType.values().forEach {
            // for each entry: assert type of the map value (Set<ValueEventListener>)
            assertThat(cellValue.eventListeners[it]!!.javaClass.name)
                    .isEqualTo("java.util.concurrent.ConcurrentHashMap\$KeySetView")
        }
    }

    private fun testCellValueFormat(spiedSubject: CellValue, formatterMock: SudokuFormatter): FormattableList {
        // given - input values
        // when
        val formatted = spiedSubject.format(formatterMock)
        // then
        verify (exactly = 1) { spiedSubject.format(formatterMock) }
        verify (exactly = 1) { formatterMock.format(spiedSubject) }
        // confirm that no further calls were made
        confirmVerified(spiedSubject)
        confirmVerified(formatterMock)
        return formatted
    }

    @Test
    fun `format - NonFixedValue`() {
        val formatterMock: SudokuFormatter = mockk(relaxed = true)
        val spiedSubject = spyk(NonFixedValue(cellMock))
        val expected = FormattableList(listOf("formatted NonFixedValue"))
        every { formatterMock.format(spiedSubject) } returns expected
        assertThat(testCellValueFormat(spiedSubject, formatterMock)).isEqualTo(expected)
    }

    @Test
    fun `format - FixedValue`() {
        val formatterMock: SudokuFormatter = mockk(relaxed = true)
        val spiedSubject = spyk(FixedValue(cellMock, 2))
        val expected = FormattableList(listOf("formatted FixedValue"))
        every { formatterMock.format(spiedSubject) } returns expected
        assertThat(testCellValueFormat(spiedSubject, formatterMock)).isEqualTo(expected)
    }

}