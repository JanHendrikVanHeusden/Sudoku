package nl.jhvh.sudoku.grid.event

import nl.jhvh.sudoku.grid.event.candidate.CellRemoveCandidatesEvent
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.model.segment.GridSegment

interface GridEventHandlable {

    fun handleSetCellValueEvent(gridEvent: SetCellValueEvent, segment: GridSegment)

    fun handleCellRemoveCandidatesEvent(gridEvent: CellRemoveCandidatesEvent, segment: GridSegment)

}