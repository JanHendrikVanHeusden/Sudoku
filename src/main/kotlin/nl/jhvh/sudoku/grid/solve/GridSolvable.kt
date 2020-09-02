package nl.jhvh.sudoku.grid.solve

import nl.jhvh.sudoku.grid.event.candidate.CellRemoveCandidatesEvent
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.model.segment.GridSegment

interface GridSolvable {

    fun handleCellSetValueEvent(gridEvent: SetCellValueEvent, segment: GridSegment)

    fun handleCellRemoveCandidatesEvent(gridEvent: CellRemoveCandidatesEvent, segment: GridSegment)

}