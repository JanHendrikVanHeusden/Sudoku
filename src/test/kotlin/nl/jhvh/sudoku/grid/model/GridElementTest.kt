package nl.jhvh.sudoku.grid.model

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

import io.mockk.clearMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.SudokuFormatter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GridElementTest {

    lateinit var gridMock: Grid

    @BeforeEach
    fun setUp() {
        gridMock = mockk(relaxed = true)
    }

    @Test
    fun getMaxValueLength() {
        for (valueLength in 1..10) {
            // given
            every { gridMock.maxValueLength } returns valueLength
            val gridElement = object: GridElement(gridMock) {
                override fun format(formatter: SudokuFormatter): FormattableList {
                    return mockk()
                }
                override fun toString() = "GridElement for testing"
            }
            clearMocks(gridMock, answers = false, recordedCalls = true, verificationMarks = true, exclusionRules = false)
            // when, then
            assertThat(gridElement.maxValueLength).isEqualTo(valueLength)
            verify (exactly = 1) {gridMock.maxValueLength}
            confirmVerified(gridMock)
        }
    }

}
