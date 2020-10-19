package nl.jhvh.sudoku.grid.model.segment

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

import io.mockk.CapturingSlot
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.spyk
import io.mockk.unmockkObject
import io.mockk.verify
import nl.jhvh.sudoku.format.Formattable
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.GridWithCellsTestBase
import nl.jhvh.sudoku.grid.model.cell.CellRef.CellRefCalculation
import nl.jhvh.sudoku.grid.model.cell.CellRef.CellRefCalculation.indexToColRef
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.random.Random

/** grid mock and cell mocks initialized in [GridWithCellsTestBase.gridSetUp] */
internal class ColTest: GridWithCellsTestBase(blockSize = 3) {

    @Test
    fun getColRef() {
        val colIndexCapturer: CapturingSlot<Int> = slot()
        // given
        for (colIndex in 0 until gridSize)
        try {
            mockkObject(CellRefCalculation)
            every { CellRefCalculation.indexToColRef(capture(colIndexCapturer)) } answers { (colIndexCapturer.captured+1).toString() }
            val subject = Col(gridMock, colIndex)
            // when
            val colRef = subject.colRef
            // then
            assertThat(colRef).isEqualTo((colIndex+1).toString())
            verify(exactly = 1) { CellRefCalculation.indexToColRef(colIndex) }
            confirmVerified(CellRefCalculation)
        } finally {
            unmockkObject(CellRefCalculation)
        }
    }

    @Test
    fun getCells() {
        for (colIndex in 0 until gridSize) {
            val subject = Col(gridMock, colIndex)
            val cellSet = subject.cells
            assertThat(cellSet).hasSize(gridSize)
            cellSet.forEachIndexed { index, cell ->
                assertThat(cell.colIndex).isEqualTo(colIndex)
                assertThat(cell.rowIndex).isEqualTo(index)
            }
        }
    }

    @Test
    fun testToString() {
        val colIndex = Random.nextInt(0, gridSize)
        val subject = Col(gridMock, colIndex)
        assertThat(subject.toString()).contains("${Col::class.simpleName}: ", "[colIndex=$colIndex]", "[colRef=${indexToColRef(colIndex)}]")
    }

    @Test
    fun format() {
        // given
        val spiedSubject = spyk(Col(gridMock, 4))
        val formatterMock: SudokuFormatter = mockk(relaxed = true)
        val expected = Formattable.FormattableList(listOf("formatted Column"))
        every { formatterMock.format(spiedSubject) } returns expected
        // when, then
        assertThat(spiedSubject.format(formatterMock)).isEqualTo(expected)
        verify (exactly = 1) { spiedSubject.format(formatterMock) }
        verify (exactly = 1) { formatterMock.format(spiedSubject) }
        // confirm that no further calls were made
        confirmVerified(spiedSubject, formatterMock)
    }

}
