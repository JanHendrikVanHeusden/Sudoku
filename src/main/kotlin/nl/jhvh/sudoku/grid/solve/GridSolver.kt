package nl.jhvh.sudoku.grid.solve

import nl.jhvh.sudoku.grid.event.candidate.CellRemoveCandidatesEvent
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.model.segment.GridSegment
import nl.jhvh.sudoku.util.log

class GridSolver: GridSolvable {

    /**
     * Handle [SetCellValueEvent]s where the [SetCellValueEvent.eventSource] is a [CellValue] within the given [segment].
     * @throws IllegalStateException when the [SetCellValueEvent.eventSource] is not a [CellValue] within the [segment];
     *                               this would indicate an unexpected subscription that we are not prepared to handle
     */
    @Throws(IllegalStateException::class)
    override fun handleSetCellValueEvent(gridEvent: SetCellValueEvent, segment: GridSegment) {
        log().trace { "$gridEvent handled by $segment" }
        check (segment.cells.contains(gridEvent.eventSource.cell)) {
            "${SetCellValueEvent::class.simpleName} should be handled only by a ${GridSegment::class.simpleName}s containing the eventSource!" +
                    " gridEvent=$gridEvent, segment=$segment" }
        segment.cells.forEach {
            if (it.cellValue === gridEvent.eventSource) {
                it.clearValueCandidates()
            } else {
                it.removeValueCandidate(gridEvent.newValue)
                // Done now, a value can be set only once, so it will not emit any SetCellValueEvent anymore.
                it.unsubscribe(segment, gridEvent.type)
            }
        }
    }

    override fun handleCellRemoveCandidatesEvent(gridEvent: CellRemoveCandidatesEvent, segment: GridSegment) {
        log().trace { "$gridEvent handled by $segment" }
        val eventSource = gridEvent.eventSource
        when (eventSource.getValueCandidates().size) {
            0 -> {
                if (!eventSource.cellValue.isSet) {
                    throw GridNotSolvableException("Grid is not solvable: no candidate values anymore in $eventSource ")
                }
            }
            1 -> {
                // allow null because the valueCandidates might be cleared by a concurrent thread / coroutine
                val cellValue = eventSource.getValueCandidates().firstOrNull()
                if (cellValue != null) {
                    eventSource.cellValue.setValue(cellValue)
                }
            }
            else -> {
                // TODO("Not implemented yet")
            }
        }
    }

}