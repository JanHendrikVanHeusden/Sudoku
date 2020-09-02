package nl.jhvh.sudoku.grid.event.candidate

import nl.jhvh.sudoku.grid.model.segment.GridSegment

interface CellRemoveCandidatesEventHandler {

    fun handleEvent(gridEvent: CellRemoveCandidatesEvent, listener: GridSegment)

}
