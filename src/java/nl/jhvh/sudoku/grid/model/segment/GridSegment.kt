package nl.jhvh.sudoku.grid.model.segment

import nl.jhvh.sudoku.grid.event.GridEvent
import nl.jhvh.sudoku.grid.event.GridEventListener
import nl.jhvh.sudoku.grid.event.candidate.CellRemoveCandidatesEvent
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
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
abstract class GridSegment constructor(grid: Grid) : GridElement(grid), GridEventListener {

    abstract val cells: LinkedHashSet<Cell>

    override fun onEvent(gridEvent: GridEvent) {
        when (gridEvent) {
            is SetCellValueEvent -> {
                handleEvent(gridEvent)
            }
            is CellRemoveCandidatesEvent -> {
                handleEvent(gridEvent)
            }
            else -> throw NotImplementedError("Unimplemented type of gridEvent: $gridEvent (class: ${gridEvent.javaClass.simpleName})")
        }
    }

    private fun handleEvent(gridEvent: CellRemoveCandidatesEvent) {
        println("Handling event: $gridEvent")
        // TODO("Not implemented yet")
    }

    private fun handleEvent(gridEvent: SetCellValueEvent) {
            cells.forEach {
            if (it.cellValue === gridEvent.eventSource) {
                it.clearValueCandidates()
            } else {
                it.removeValueCandidate(gridEvent.newValue)
                // Done now, a value can be set only once, so will not emit any SetCellValueEvent events anymore.
                it.unsubscribe(this)
            }
        }
    }

    protected fun subscribeToSetValueEvents() {
        cells.forEach { it.cellValue.subscribe(this) }
    }

}
