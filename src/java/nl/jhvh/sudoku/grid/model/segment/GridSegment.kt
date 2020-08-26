package nl.jhvh.sudoku.grid.model.segment

import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueListener
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.GridElement
import nl.jhvh.sudoku.grid.model.cell.Cell

/**
 * A [GridSegment] is an abstraction (super class) of the collections of [Cell]s that each, when solved,
 * contain all defined values of the Sudoku.
 *
 * Concrete subclasses are [Col] (column), [Row] and [Block].
 *
 * A functional synonym for [GridSegment] is **Group**.
 */
abstract class GridSegment constructor(grid: Grid) : GridElement(grid), SetCellValueListener {

    abstract val cells: LinkedHashSet<Cell>

    override fun onEvent(gridEvent: SetCellValueEvent) {
            cells.forEach {
            if (it.cellValue === gridEvent.eventSource) {
                it.clearValueCandidates()
            } else {
                it.removeValueCandidate(gridEvent.newValue)
                // Consider if it is better (performance wise) to unsubscribe the listener.
                // Note however that no setValueEvent will ever come from a cell anymore once it's value is set.
                // So probably it does not make much sense, maybe it adds load rather than gaining performance.
                //
                // NB !!
                // If unsubscribe is implemented, the collection of listeners MUST be made thread safe!

                // it.unsubscribe(this)
            }
        }
    }

    protected fun subscribeToSetValueEvents() {
        cells.forEach { it.cellValue.subscribe(this) }
    }

}
