package nl.jhvh.sudoku.grid.event.cellvalue

import nl.jhvh.sudoku.grid.event.ValueEventType
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.Grid.GridBuilder
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.cell.CellRef
import nl.jhvh.sudoku.grid.model.cell.CellValue.FixedValue
import nl.jhvh.sudoku.grid.model.cell.CellValue.NonFixedValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * Tests for [Grid] initialization.
 *
 * It is a component integration test, it tests the results of the listener actions all over the[Cell]s in the [Grid].
 */
internal class CellValueListenerTest {
    /**
     * Test method for the whole cycle of setting values, firing the appropriate [SetCellValueEvent]s
     * by the [CellValue]s (in their role as [ValueEventSource], and having these observed and handled
     * by the [GridSegment]s (in their role as [ValueEventListener]s).
     *
     * In this method, the setting of values is checked after the Grid was built,
     * so the [NonFixedValue.getValueCandidates()] are checked after fixing the values up to the [GridBuilder.build].
     */
    @Test
    fun `candidate values in the segments should have been removed based on their fixed cellvalues`() {
        val grid = GridBuilder()
                .fix(CellRef(0, 0), 2)
                .fix(CellRef(2, 1), 8)
                .fix(CellRef(5, 2), 3)
                .fix(CellRef(0, 6), 1)
                .fix(CellRef(8, 8), 7)
                .fix(CellRef(1, 8), 3)
                .fix(CellRef(3, 8), 5)
                .fix(CellRef(5, 6), 4)
                .fix(CellRef(4, 4), 9)
                .build()
        // for (y in 0 until grid.gridSize) {
        //     for (x in 0 until grid.gridSize) {
        //         val cellValue = grid.findCell(x, y).cellValue
        //         println("("+ x + "," + y + ")\t\t" + if (cellValue is NonFixedValue) cellValue.getValueCandidates().toString() else "[]");
        //     }
        // }
        with(grid.findCell(CellRef(0, 0)).cellValue) {
            assertThat(this).isExactlyInstanceOf(FixedValue::class.java)
            assertThat(this.eventListeners[ValueEventType.SET_CELL_VALUE]).isEmpty()
        }
        assertThat((grid.findCell(CellRef(1, 0)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 4, 5, 6, 7, 9))
        assertThat((grid.findCell(CellRef(2, 0)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 3, 4, 5, 6, 7, 9))
        assertThat((grid.findCell(CellRef(3, 0)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 4, 6, 7, 8, 9))
        assertThat((grid.findCell(CellRef(4, 0)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 4, 5, 6, 7, 8))
        assertThat((grid.findCell(CellRef(5, 0)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 5, 6, 7, 8, 9))
        assertThat((grid.findCell(CellRef(6, 0)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 3, 4, 5, 6, 7, 8, 9))
        assertThat((grid.findCell(CellRef(7, 0)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 3, 4, 5, 6, 7, 8, 9))
        assertThat((grid.findCell(CellRef(8, 0)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 3, 4, 5, 6, 8, 9))
        assertThat((grid.findCell(CellRef(0, 1)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(3, 4, 5, 6, 7, 9))
        assertThat((grid.findCell(CellRef(1, 1)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 4, 5, 6, 7, 9))
        with(grid.findCell(CellRef(2, 1)).cellValue) {
            assertThat(this).isExactlyInstanceOf(FixedValue::class.java)
            assertThat(this.eventListeners[ValueEventType.SET_CELL_VALUE]).isEmpty()
        }
        assertThat((grid.findCell(CellRef(3, 1)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 4, 6, 7, 9))
        assertThat((grid.findCell(CellRef(4, 1)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 4, 5, 6, 7))
        assertThat((grid.findCell(CellRef(5, 1)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 5, 6, 7, 9))
        assertThat((grid.findCell(CellRef(6, 1)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7, 9))
        assertThat((grid.findCell(CellRef(7, 1)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7, 9))
        assertThat((grid.findCell(CellRef(8, 1)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 9))
        assertThat((grid.findCell(CellRef(0, 2)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(4, 5, 6, 7, 9))
        assertThat((grid.findCell(CellRef(1, 2)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 4, 5, 6, 7, 9))
        assertThat((grid.findCell(CellRef(2, 2)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 4, 5, 6, 7, 9))
        assertThat((grid.findCell(CellRef(3, 2)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 4, 6, 7, 8, 9))
        assertThat((grid.findCell(CellRef(4, 2)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 4, 5, 6, 7, 8))
        with(grid.findCell(CellRef(5, 2)).cellValue) {
            assertThat(this).isExactlyInstanceOf(FixedValue::class.java)
            assertThat(this.eventListeners[ValueEventType.SET_CELL_VALUE]).isEmpty()
        }
        assertThat((grid.findCell(CellRef(6, 2)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 4, 5, 6, 7, 8, 9))
        assertThat((grid.findCell(CellRef(7, 2)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 4, 5, 6, 7, 8, 9))
        assertThat((grid.findCell(CellRef(8, 2)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 4, 5, 6, 8, 9))
        assertThat((grid.findCell(CellRef(0, 3)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(3, 4, 5, 6, 7, 8, 9))
        assertThat((grid.findCell(CellRef(1, 3)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 4, 5, 6, 7, 8, 9))
        assertThat((grid.findCell(CellRef(2, 3)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7, 9))
        assertThat((grid.findCell(CellRef(3, 3)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 6, 7, 8))
        assertThat((grid.findCell(CellRef(4, 3)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7, 8))
        assertThat((grid.findCell(CellRef(5, 3)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 5, 6, 7, 8))
        assertThat((grid.findCell(CellRef(6, 3)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7, 8, 9))
        assertThat((grid.findCell(CellRef(7, 3)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7, 8, 9))
        assertThat((grid.findCell(CellRef(8, 3)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 8, 9))
        assertThat((grid.findCell(CellRef(0, 4)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(3, 4, 5, 6, 7, 8))
        assertThat((grid.findCell(CellRef(1, 4)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 4, 5, 6, 7, 8))
        assertThat((grid.findCell(CellRef(2, 4)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7))
        assertThat((grid.findCell(CellRef(3, 4)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 6, 7, 8))
        with(grid.findCell(CellRef(4, 4)).cellValue) {
            assertThat(this).isExactlyInstanceOf(FixedValue::class.java)
            assertThat(this.eventListeners[ValueEventType.SET_CELL_VALUE]).isEmpty()
        }
        assertThat((grid.findCell(CellRef(5, 4)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 5, 6, 7, 8))
        assertThat((grid.findCell(CellRef(6, 4)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7, 8))
        assertThat((grid.findCell(CellRef(7, 4)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7, 8))
        assertThat((grid.findCell(CellRef(8, 4)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 8))
        assertThat((grid.findCell(CellRef(0, 5)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(3, 4, 5, 6, 7, 8, 9))
        assertThat((grid.findCell(CellRef(1, 5)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 4, 5, 6, 7, 8, 9))
        assertThat((grid.findCell(CellRef(2, 5)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7, 9))
        assertThat((grid.findCell(CellRef(3, 5)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 6, 7, 8))
        assertThat((grid.findCell(CellRef(4, 5)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7, 8))
        assertThat((grid.findCell(CellRef(5, 5)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 5, 6, 7, 8))
        assertThat((grid.findCell(CellRef(6, 5)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7, 8, 9))
        assertThat((grid.findCell(CellRef(7, 5)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7, 8, 9))
        assertThat((grid.findCell(CellRef(8, 5)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 8, 9))
        with(grid.findCell(CellRef(0, 6)).cellValue) {
            assertThat(this).isExactlyInstanceOf(FixedValue::class.java)
            assertThat(this.eventListeners[ValueEventType.SET_CELL_VALUE]).isEmpty()
        }
        assertThat((grid.findCell(CellRef(1, 6)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(2, 5, 6, 7, 8, 9))
        assertThat((grid.findCell(CellRef(2, 6)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(2, 5, 6, 7, 9))
        assertThat((grid.findCell(CellRef(3, 6)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(2, 3, 6, 7, 8, 9))
        assertThat((grid.findCell(CellRef(4, 6)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(2, 3, 6, 7, 8))
        with(grid.findCell(CellRef(5, 6)).cellValue) {
            assertThat(this).isExactlyInstanceOf(FixedValue::class.java)
            assertThat(this.eventListeners[ValueEventType.SET_CELL_VALUE]).isEmpty()
        }
        assertThat((grid.findCell(CellRef(6, 6)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(2, 3, 5, 6, 8, 9))
        assertThat((grid.findCell(CellRef(7, 6)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(2, 3, 5, 6, 8, 9))
        assertThat((grid.findCell(CellRef(8, 6)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(2, 3, 5, 6, 8, 9))
        assertThat((grid.findCell(CellRef(0, 7)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(4, 5, 6, 7, 8, 9))
        assertThat((grid.findCell(CellRef(1, 7)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(2, 4, 5, 6, 7, 8, 9))
        assertThat((grid.findCell(CellRef(2, 7)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(2, 4, 5, 6, 7, 9))
        assertThat((grid.findCell(CellRef(3, 7)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 6, 7, 8, 9))
        assertThat((grid.findCell(CellRef(4, 7)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 6, 7, 8))
        assertThat((grid.findCell(CellRef(5, 7)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 6, 7, 8, 9))
        assertThat((grid.findCell(CellRef(6, 7)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 8, 9))
        assertThat((grid.findCell(CellRef(7, 7)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 8, 9))
        assertThat((grid.findCell(CellRef(8, 7)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 8, 9))
        assertThat((grid.findCell(CellRef(0, 8)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(4, 6, 8, 9))
        with(grid.findCell(CellRef(1, 8)).cellValue) {
            assertThat(this).isExactlyInstanceOf(FixedValue::class.java)
            assertThat(this.eventListeners[ValueEventType.SET_CELL_VALUE]).isEmpty()
        }
        assertThat((grid.findCell(CellRef(2, 8)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(2, 4, 6, 9))
        assertThat(grid.findCell(CellRef(3, 8)).cellValue).isExactlyInstanceOf(FixedValue::class.java)
        assertThat((grid.findCell(CellRef(4, 8)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 6, 8))
        assertThat((grid.findCell(CellRef(5, 8)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 6, 8, 9))
        assertThat((grid.findCell(CellRef(6, 8)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 4, 6, 8, 9))
        assertThat((grid.findCell(CellRef(7, 8)).cellValue as NonFixedValue).getValueCandidates()).isEqualTo(setOf(1, 2, 4, 6, 8, 9))
        with(grid.findCell(CellRef(8, 8)).cellValue) {
            assertThat(this).isExactlyInstanceOf(FixedValue::class.java)
            assertThat(this.eventListeners[ValueEventType.SET_CELL_VALUE]).isEmpty()
        }
    }
}
