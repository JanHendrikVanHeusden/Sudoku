package nl.jhvh.sudoku.grid.event.candidate

import nl.jhvh.sudoku.grid.event.GridEventListener
import nl.jhvh.sudoku.grid.event.GridEventSource
import nl.jhvh.sudoku.grid.model.cell.Cell

/** Interface for [GridEventSource]s ([Cell]s) that want to emit [CellRemoveCandidatesEvent]s */
interface CellRemoveCandidatesSource : GridEventSource<Cell, CellRemoveCandidatesEvent> {

    /** @return The [Set] of [GridEventListener]s that have subscribed to [CellRemoveCandidatesEvent]s of this [Cell] */
    override val eventListeners: MutableSet<GridEventListener<Cell, CellRemoveCandidatesEvent>>
}
