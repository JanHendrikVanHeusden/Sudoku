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
import io.mockk.mockkConstructor
import io.mockk.spyk
import io.mockk.unmockkConstructor
import io.mockk.verify
import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.cell.CellValue.FixedValue
import nl.jhvh.sudoku.grid.model.cell.CellValue.NonFixedValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CellTest {

    private lateinit var grid9Mock: Grid

    @BeforeEach
    fun setUp() {
        grid9Mock = mockk(relaxed = true)
        every { grid9Mock.blockSize } returns 3
        every { grid9Mock.gridSize } returns 9
        every { grid9Mock.maxValue } returns 9
    }

    @Test
    fun `getCellValue - not fixed`() {
        val subject = Cell(grid9Mock, 2, 5)
        assertThat(subject.cellValue.value).isNull()
        assertThat(subject.cellValue).isInstanceOf(NonFixedValue::class.java)
        subject.cellValue.setValue(1)
        assertThat(subject.cellValue.value).isEqualTo(1)
    }

    @Test
    fun `getCellValue - fixed`() {
        val subject = Cell(grid9Mock, 8, 4, fixedValue = 7)
        assertThat(subject.cellValue.value).isEqualTo(7)
        assertThat(subject.cellValue).isInstanceOf(FixedValue::class.java)
    }

    @Test
    fun `toString - fixed value Cell`() {
        val fixedValueToString = "fixedValueToString"
        try {
            mockkConstructor(FixedValue::class)
            every {anyConstructed<FixedValue>().toString()} returns fixedValueToString
            val subject = Cell(grid9Mock, 2, 5, fixedValue = 7)
            assertThat(subject.toString()).contains("Cell: ", "colIndex=2", "rowIndex=5", "cellValue=[$fixedValueToString]")
            verify (exactly = 1) { anyConstructed<FixedValue>().toString() }
        } finally {
            unmockkConstructor(FixedValue::class)
        }
    }

    @Test
    fun `toString - non-fixed value Cell`() {
        val nonFixedValueToString = "nonFixedValueToString"
        try {
            mockkConstructor(NonFixedValue::class)
            every {anyConstructed<NonFixedValue>().toString()} returns nonFixedValueToString
            val subject = Cell(grid9Mock, 6, 3)
            assertThat(subject.toString()).contains("Cell: ", "colIndex=6", "rowIndex=3", "cellValue=[$nonFixedValueToString]")
            verify (exactly = 1) { anyConstructed<NonFixedValue>().toString() }
        } finally {
            unmockkConstructor(NonFixedValue::class)
        }
    }

    @Test
    fun format() {
        // given
        val spiedSubject = spyk(Cell(grid9Mock, 2, 5))
        val formatterMock: SudokuFormatter = mockk(relaxed = true)
        val expected = FormattableList(listOf("formatted Cell"))
        every { formatterMock.format(spiedSubject) } returns expected
        // when, then
        assertThat(spiedSubject.format(formatterMock)).isEqualTo(expected)
        verify (exactly = 1) { spiedSubject.format(formatterMock) }
        verify (exactly = 1) { formatterMock.format(spiedSubject) }
        // confirm that no further calls were made
        confirmVerified(spiedSubject, formatterMock)
    }
}
