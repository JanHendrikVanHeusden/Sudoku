package nl.jhvh.sudoku.grid.solve

import nl.jhvh.sudoku.grid.event.candidate.CellRemoveCandidatesEvent
import nl.jhvh.sudoku.grid.event.candidate.CellRemoveCandidatesEventHandler
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEventHandler
import nl.jhvh.sudoku.grid.model.segment.GridSegment

interface GridSolvable: CellRemoveCandidatesEventHandler, SetCellValueEventHandler {

    override fun handleEvent(gridEvent: CellRemoveCandidatesEvent, listener: GridSegment)

    override fun handleEvent(gridEvent: SetCellValueEvent, listener: GridSegment)

}