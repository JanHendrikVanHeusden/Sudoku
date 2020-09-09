package nl.jhvh.sudoku.grid.solve

import nl.jhvh.sudoku.grid.event.cellvalue.CellRemoveCandidatesEvent
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.model.segment.GridSegment

interface SegmentValueEventHandlable {

    fun handleSetCellValueEvent(valueEvent: SetCellValueEvent, segment: GridSegment)

    fun handleRemoveCandidatesEvent(valueEvent: CellRemoveCandidatesEvent, segment: GridSegment)

}