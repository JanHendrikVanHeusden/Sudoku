package nl.jhvh.sudoku.grid.solve

import nl.jhvh.sudoku.grid.event.cellvalue.CellRemoveCandidatesEvent
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent

interface ValueEventHandlable {

    fun handleSetCellValueEvent(event: SetCellValueEvent)

    fun handleRemoveCandidatesEvent(event: CellRemoveCandidatesEvent)
}