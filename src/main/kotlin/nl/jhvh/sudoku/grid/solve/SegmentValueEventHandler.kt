package nl.jhvh.sudoku.grid.solve

import nl.jhvh.sudoku.grid.event.cellvalue.CellRemoveCandidatesEvent
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.model.cell.CellValue
import nl.jhvh.sudoku.grid.model.cell.CellValue.NonFixedValue
import nl.jhvh.sudoku.grid.model.segment.GridSegment
import nl.jhvh.sudoku.util.log

class SegmentValueEventHandler: SegmentValueEventHandlable {

    /**
     * Handle [SetCellValueEvent]s where the [SetCellValueEvent.eventSource] is a [CellValue] within the given [segment].
     * @throws IllegalStateException when the [SetCellValueEvent.eventSource] is not a [CellValue] within the [segment];
     *                               this would indicate an unexpected situation that we are not prepared to handle
     */
    @Throws(IllegalStateException::class)
    override fun handleSetCellValueEvent(valueEvent: SetCellValueEvent, segment: GridSegment) {
        log().trace { "$valueEvent handled by $segment" }
        check (segment.cells.contains(valueEvent.eventSource.cell)) {
            "${SetCellValueEvent::class.simpleName} should be handled only by a ${GridSegment::class.simpleName}s containing the eventSource!" +
                    " valueEvent=$valueEvent, segment=$segment" }
        segment.cells.forEach {
            if (it.cellValue is NonFixedValue) {
                if (it.cellValue === valueEvent.eventSource) {
                    it.cellValue.clearValueCandidates()
                } else {
                    it.cellValue.removeValueCandidate(valueEvent.newValue)
                }
            }
        }
    }

    override fun handleRemoveCandidatesEvent(valueEvent: CellRemoveCandidatesEvent, segment: GridSegment) {
        log().trace { "$valueEvent handled by $segment" }
        val eventSource = valueEvent.eventSource
        val updatedValueCandidates = eventSource.getValueCandidates()
        when (updatedValueCandidates.size) {
            0 -> {
                if (!eventSource.isSet) {
                    throw GridNotSolvableException("Grid is not solvable: no candidate values anymore in $eventSource ")
                }
            }
            1 -> {
                // allow null because the valueCandidates might be cleared by a concurrent thread / coroutine
                val intValue = updatedValueCandidates.firstOrNull()
                if (intValue != null) {
                    eventSource.setValue(intValue)
                }
            }
            else -> {
                // TODO("Not implemented yet")
            }
        }
    }

}