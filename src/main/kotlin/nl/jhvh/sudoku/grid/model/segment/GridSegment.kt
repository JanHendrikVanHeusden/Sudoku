package nl.jhvh.sudoku.grid.model.segment

import nl.jhvh.sudoku.grid.event.GridEvent
import nl.jhvh.sudoku.grid.event.GridEventListener
import nl.jhvh.sudoku.grid.event.GridEventType.SET_CELL_VALUE
import nl.jhvh.sudoku.grid.event.candidate.CellRemoveCandidatesEvent
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.GridElement
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.util.log

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
        log().trace { "$this received event: $gridEvent" }
        when (gridEvent) {
            is SetCellValueEvent -> {
                grid.handleCellSetValueEvent(gridEvent, this)
            }
            is CellRemoveCandidatesEvent -> {
                grid.handleCellRemoveCandidatesEvent(gridEvent, this)
            }
            else -> {log().warn { "Unhandled event type: ${gridEvent.javaClass.simpleName}" }}
            // not expected to be called, so warning:
            // it would mean we subscribed to an event type that we are not willing to handle
        }
    }

    protected fun subscribeToSetValueEvents() {
        cells.forEach { it.cellValue.subscribe(this, SET_CELL_VALUE) }
    }

}
