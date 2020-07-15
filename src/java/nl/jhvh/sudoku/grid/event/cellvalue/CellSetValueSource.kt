package nl.jhvh.sudoku.grid.event.cellvalue

import nl.jhvh.sudoku.grid.event.GridEventListener
import nl.jhvh.sudoku.grid.event.GridEventSource
import nl.jhvh.sudoku.grid.model.cell.Cell

/** Interface for [GridEventSource]s ([Cell]s) that want to emit [CellSetValueEvent]s */
interface CellSetValueSource : GridEventSource<Cell, CellSetValueEvent> {

    /** @return The [Set] of [GridEventListener]s that have subscribed to [CellSetValueEvent]s of this [Cell] */
    override val eventListeners: MutableSet<GridEventListener<Cell, CellSetValueEvent>>
}
