package nl.jhvh.sudoku.grid.solve

import nl.jhvh.sudoku.grid.event.GridEventType.SET_CELL_VALUE
import nl.jhvh.sudoku.grid.event.candidate.CellRemoveCandidatesEvent
import nl.jhvh.sudoku.grid.event.candidate.CellRemoveCandidatesEventHandler
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEventHandler
import nl.jhvh.sudoku.grid.model.segment.GridSegment
import nl.jhvh.sudoku.util.log

class GridSolver: GridSolvable, CellRemoveCandidatesEventHandler, SetCellValueEventHandler {

    override fun handleEvent(gridEvent: CellRemoveCandidatesEvent, listener: GridSegment) {
        log().trace { "$gridEvent handled by $listener" }
        // TODO("Not implemented yet")
    }

    override fun handleEvent(gridEvent: SetCellValueEvent, listener: GridSegment) {
        log().trace { "$gridEvent handled by $listener" }
        listener.cells.forEach {
            if (it.cellValue === gridEvent.eventSource) {
                it.clearValueCandidates()
            } else {
                it.removeValueCandidate(gridEvent.newValue)
                // Done now, a value can be set only once, so it will not emit any SetCellValueEvent anymore.
                it.unsubscribe(listener, SET_CELL_VALUE)
            }
        }
    }

}