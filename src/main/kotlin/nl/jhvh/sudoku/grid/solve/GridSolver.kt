package nl.jhvh.sudoku.grid.solve

import nl.jhvh.sudoku.grid.event.GridEventType.SET_CELL_VALUE
import nl.jhvh.sudoku.grid.event.candidate.CellRemoveCandidatesEvent
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.model.segment.GridSegment
import nl.jhvh.sudoku.util.log

class GridSolver: GridSolvable {

    override fun handleCellSetValueEvent(gridEvent: SetCellValueEvent, segment: GridSegment) {
        log().trace { "$gridEvent handled by $segment" }
        segment.cells.forEach {
            if (it.cellValue === gridEvent.eventSource) {
                it.clearValueCandidates()
            } else {
                it.removeValueCandidate(gridEvent.newValue)
                // Done now, a value can be set only once, so it will not emit any SetCellValueEvent anymore.
                it.unsubscribe(segment, SET_CELL_VALUE)
            }
        }
    }

    override fun handleCellRemoveCandidatesEvent(gridEvent: CellRemoveCandidatesEvent, segment: GridSegment) {
        log().trace { "$gridEvent handled by $segment" }
//        TODO("Not implemented yet")
    }

}