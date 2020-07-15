package nl.jhvh.sudoku.grid.event.candidate

import nl.jhvh.sudoku.grid.event.GridEventListener
import nl.jhvh.sudoku.grid.model.cell.Cell

/** Interface to define [GridEventListener]<[Cell], [CellRemoveCandidatesEvent]> */
interface CellRemoveCandidatesListener : GridEventListener<Cell, CellRemoveCandidatesEvent> {

    override fun onEvent(gridEvent: CellRemoveCandidatesEvent)
}
