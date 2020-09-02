package nl.jhvh.sudoku.grid.event.cellvalue

import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.Grid.GridBuilder
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.cell.CellRef
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * Tests for [Grid] initialization.
 * Is not a unit test but rather a component integration test, as it tests the results
 * of the listener actions all over the[Cell]s in the [Grid].
 */
internal class CellValueListenerTest {
    /**
     * Test method for the whole cycle of setting values, firing the appropriate [SetCellValueEvent]s
     * by the [Cell]s (in their role as [GridEventSource], and having these observed and handled
     * by the [GridSegment]s (in their role as [GridEventListener]s).
     *
     * In this method, the setting of values is checked after the Grid was built, so the [Cell.getValueCandidates()]
     * are checked after fixing the values up to the [GridBuilder.build].
     */
    @Test
    fun `test Cell value listeners`() {
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
         //         println("("+ x + "," + y + ")\t\t" + grid.findCell(x, y).getValueCandidates());
         //     }
         // }
        assertThat(grid.findCell(CellRef(0, 0)).getValueCandidates()).isEmpty()
        assertThat(grid.findCell(CellRef(0, 0)).getValueCandidates()).isEqualTo(setOf<Int>())
        assertThat(grid.findCell(CellRef(1, 0)).getValueCandidates()).isEqualTo(setOf(1, 4, 5, 6, 7, 9))
        assertThat(grid.findCell(CellRef(2, 0)).getValueCandidates()).isEqualTo(setOf(1, 3, 4, 5, 6, 7, 9))
        assertThat(grid.findCell(CellRef(3, 0)).getValueCandidates()).isEqualTo(setOf(1, 4, 6, 7, 8, 9))
        assertThat(grid.findCell(CellRef(4, 0)).getValueCandidates()).isEqualTo(setOf(1, 4, 5, 6, 7, 8))
        assertThat(grid.findCell(CellRef(5, 0)).getValueCandidates()).isEqualTo(setOf(1, 5, 6, 7, 8, 9))
        assertThat(grid.findCell(CellRef(6, 0)).getValueCandidates()).isEqualTo(setOf(1, 3, 4, 5, 6, 7, 8, 9))
        assertThat(grid.findCell(CellRef(7, 0)).getValueCandidates()).isEqualTo(setOf(1, 3, 4, 5, 6, 7, 8, 9))
        assertThat(grid.findCell(CellRef(8, 0)).getValueCandidates()).isEqualTo(setOf(1, 3, 4, 5, 6, 8, 9))
        assertThat(grid.findCell(CellRef(0, 1)).getValueCandidates()).isEqualTo(setOf(3, 4, 5, 6, 7, 9))
        assertThat(grid.findCell(CellRef(1, 1)).getValueCandidates()).isEqualTo(setOf(1, 4, 5, 6, 7, 9))
        assertThat(grid.findCell(CellRef(2, 1)).getValueCandidates()).isEmpty()
        assertThat(grid.findCell(CellRef(3, 1)).getValueCandidates()).isEqualTo(setOf(1, 2, 4, 6, 7, 9))
        assertThat(grid.findCell(CellRef(4, 1)).getValueCandidates()).isEqualTo(setOf(1, 2, 4, 5, 6, 7))
        assertThat(grid.findCell(CellRef(5, 1)).getValueCandidates()).isEqualTo(setOf(1, 2, 5, 6, 7, 9))
        assertThat(grid.findCell(CellRef(6, 1)).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7, 9))
        assertThat(grid.findCell(CellRef(7, 1)).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7, 9))
        assertThat(grid.findCell(CellRef(8, 1)).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 9))
        assertThat(grid.findCell(CellRef(0, 2)).getValueCandidates()).isEqualTo(setOf(4, 5, 6, 7, 9))
        assertThat(grid.findCell(CellRef(1, 2)).getValueCandidates()).isEqualTo(setOf(1, 4, 5, 6, 7, 9))
        assertThat(grid.findCell(CellRef(2, 2)).getValueCandidates()).isEqualTo(setOf(1, 4, 5, 6, 7, 9))
        assertThat(grid.findCell(CellRef(3, 2)).getValueCandidates()).isEqualTo(setOf(1, 2, 4, 6, 7, 8, 9))
        assertThat(grid.findCell(CellRef(4, 2)).getValueCandidates()).isEqualTo(setOf(1, 2, 4, 5, 6, 7, 8))
        assertThat(grid.findCell(CellRef(5, 2)).getValueCandidates()).isEmpty()
        assertThat(grid.findCell(CellRef(6, 2)).getValueCandidates()).isEqualTo(setOf(1, 2, 4, 5, 6, 7, 8, 9))
        assertThat(grid.findCell(CellRef(7, 2)).getValueCandidates()).isEqualTo(setOf(1, 2, 4, 5, 6, 7, 8, 9))
        assertThat(grid.findCell(CellRef(8, 2)).getValueCandidates()).isEqualTo(setOf(1, 2, 4, 5, 6, 8, 9))
        assertThat(grid.findCell(CellRef(0, 3)).getValueCandidates()).isEqualTo(setOf(3, 4, 5, 6, 7, 8, 9))
        assertThat(grid.findCell(CellRef(1, 3)).getValueCandidates()).isEqualTo(setOf(1, 2, 4, 5, 6, 7, 8, 9))
        assertThat(grid.findCell(CellRef(2, 3)).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7, 9))
        assertThat(grid.findCell(CellRef(3, 3)).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 6, 7, 8))
        assertThat(grid.findCell(CellRef(4, 3)).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7, 8))
        assertThat(grid.findCell(CellRef(5, 3)).getValueCandidates()).isEqualTo(setOf(1, 2, 5, 6, 7, 8))
        assertThat(grid.findCell(CellRef(6, 3)).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7, 8, 9))
        assertThat(grid.findCell(CellRef(7, 3)).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7, 8, 9))
        assertThat(grid.findCell(CellRef(8, 3)).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 8, 9))
        assertThat(grid.findCell(CellRef(0, 4)).getValueCandidates()).isEqualTo(setOf(3, 4, 5, 6, 7, 8))
        assertThat(grid.findCell(CellRef(1, 4)).getValueCandidates()).isEqualTo(setOf(1, 2, 4, 5, 6, 7, 8))
        assertThat(grid.findCell(CellRef(2, 4)).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7))
        assertThat(grid.findCell(CellRef(3, 4)).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 6, 7, 8))
        assertThat(grid.findCell(CellRef(4, 4)).getValueCandidates()).isEmpty()
        assertThat(grid.findCell(CellRef(5, 4)).getValueCandidates()).isEqualTo(setOf(1, 2, 5, 6, 7, 8))
        assertThat(grid.findCell(CellRef(6, 4)).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7, 8))
        assertThat(grid.findCell(CellRef(7, 4)).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7, 8))
        assertThat(grid.findCell(CellRef(8, 4)).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 8))
        assertThat(grid.findCell(CellRef(0, 5)).getValueCandidates()).isEqualTo(setOf(3, 4, 5, 6, 7, 8, 9))
        assertThat(grid.findCell(CellRef(1, 5)).getValueCandidates()).isEqualTo(setOf(1, 2, 4, 5, 6, 7, 8, 9))
        assertThat(grid.findCell(CellRef(2, 5)).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7, 9))
        assertThat(grid.findCell(CellRef(3, 5)).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 6, 7, 8))
        assertThat(grid.findCell(CellRef(4, 5)).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7, 8))
        assertThat(grid.findCell(CellRef(5, 5)).getValueCandidates()).isEqualTo(setOf(1, 2, 5, 6, 7, 8))
        assertThat(grid.findCell(CellRef(6, 5)).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7, 8, 9))
        assertThat(grid.findCell(CellRef(7, 5)).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 7, 8, 9))
        assertThat(grid.findCell(CellRef(8, 5)).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 8, 9))
        assertThat(grid.findCell(CellRef(0, 6)).getValueCandidates()).isEmpty()
        assertThat(grid.findCell(CellRef(1, 6)).getValueCandidates()).isEqualTo(setOf(2, 5, 6, 7, 8, 9))
        assertThat(grid.findCell(CellRef(2, 6)).getValueCandidates()).isEqualTo(setOf(2, 5, 6, 7, 9))
        assertThat(grid.findCell(CellRef(3, 6)).getValueCandidates()).isEqualTo(setOf(2, 3, 6, 7, 8, 9))
        assertThat(grid.findCell(CellRef(4, 6)).getValueCandidates()).isEqualTo(setOf(2, 3, 6, 7, 8))
        assertThat(grid.findCell(CellRef(5, 6)).getValueCandidates()).isEmpty()
        assertThat(grid.findCell(CellRef(6, 6)).getValueCandidates()).isEqualTo(setOf(2, 3, 5, 6, 8, 9))
        assertThat(grid.findCell(CellRef(7, 6)).getValueCandidates()).isEqualTo(setOf(2, 3, 5, 6, 8, 9))
        assertThat(grid.findCell(CellRef(8, 6)).getValueCandidates()).isEqualTo(setOf(2, 3, 5, 6, 8, 9))
        assertThat(grid.findCell(CellRef(0, 7)).getValueCandidates()).isEqualTo(setOf(4, 5, 6, 7, 8, 9))
        assertThat(grid.findCell(CellRef(1, 7)).getValueCandidates()).isEqualTo(setOf(2, 4, 5, 6, 7, 8, 9))
        assertThat(grid.findCell(CellRef(2, 7)).getValueCandidates()).isEqualTo(setOf(2, 4, 5, 6, 7, 9))
        assertThat(grid.findCell(CellRef(3, 7)).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 6, 7, 8, 9))
        assertThat(grid.findCell(CellRef(4, 7)).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 6, 7, 8))
        assertThat(grid.findCell(CellRef(5, 7)).getValueCandidates()).isEqualTo(setOf(1, 2, 6, 7, 8, 9))
        assertThat(grid.findCell(CellRef(6, 7)).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 8, 9))
        assertThat(grid.findCell(CellRef(7, 7)).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 8, 9))
        assertThat(grid.findCell(CellRef(8, 7)).getValueCandidates()).isEqualTo(setOf(1, 2, 3, 4, 5, 6, 8, 9))
        assertThat(grid.findCell(CellRef(0, 8)).getValueCandidates()).isEqualTo(setOf(4, 6, 8, 9))
        assertThat(grid.findCell(CellRef(1, 8)).getValueCandidates()).isEmpty()
        assertThat(grid.findCell(CellRef(2, 8)).getValueCandidates()).isEqualTo(setOf(2, 4, 6, 9))
        assertThat(grid.findCell(CellRef(3, 8)).getValueCandidates()).isEmpty()
        assertThat(grid.findCell(CellRef(4, 8)).getValueCandidates()).isEqualTo(setOf(1, 2, 6, 8))
        assertThat(grid.findCell(CellRef(5, 8)).getValueCandidates()).isEqualTo(setOf(1, 2, 6, 8, 9))
        assertThat(grid.findCell(CellRef(6, 8)).getValueCandidates()).isEqualTo(setOf(1, 2, 4, 6, 8, 9))
        assertThat(grid.findCell(CellRef(7, 8)).getValueCandidates()).isEqualTo(setOf(1, 2, 4, 6, 8, 9))
        assertThat(grid.findCell(CellRef(8, 8)).getValueCandidates()).isEmpty()
    }
}
