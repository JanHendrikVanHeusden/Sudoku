package nl.jhvh.sudoku.grid.model.cell

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkConstructor
import io.mockk.verify
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
    fun `assert that valueCandidates is thread safe`() {
        assertThat(Cell(mockk(relaxed = true), 2, 5).getValueCandidates().javaClass.name)
                .isEqualTo("java.util.concurrent.ConcurrentHashMap\$KeySetView")
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
            assertThat(subject.toString()).contains("Cell: ", "colIndex=2", "rowIndex=5", "cellValue=[$fixedValueToString]", "valueCandidates=[]")
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
            assertThat(subject.toString()).contains("Cell: ", "colIndex=6", "rowIndex=3", "cellValue=[$nonFixedValueToString]", "valueCandidates=[")
            verify (exactly = 1) { anyConstructed<NonFixedValue>().toString() }
        } finally {
            unmockkConstructor(NonFixedValue::class)
        }
    }

}
