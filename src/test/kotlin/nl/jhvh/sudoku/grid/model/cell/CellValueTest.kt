package nl.jhvh.sudoku.grid.model.cell

import io.mockk.every
import io.mockk.mockk
import nl.jhvh.sudoku.grid.model.cell.CellValue.FixedValue
import nl.jhvh.sudoku.grid.model.cell.CellValue.NonFixedValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CellValueTest {

    private lateinit var cellMock: Cell
    private val gridSize = 9

    @BeforeEach
    fun setUp() {
        cellMock = mockk(relaxed = true)
        every {cellMock.grid.maxValue} returns gridSize
    }

    @Test
    fun `eventListeners should be thread safe`() {
        var cellValue: CellValue = FixedValue(cellMock, 3)
        assertThat(cellValue.eventListeners.javaClass.name)
                .isEqualTo("java.util.concurrent.ConcurrentHashMap")
        cellValue = NonFixedValue(cellMock)
        assertThat(cellValue.eventListeners.javaClass.name)
                .isEqualTo("java.util.concurrent.ConcurrentHashMap")
    }

}