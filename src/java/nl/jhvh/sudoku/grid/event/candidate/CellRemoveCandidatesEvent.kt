package nl.jhvh.sudoku.grid.event.candidate

import nl.jhvh.sudoku.grid.event.GridEvent
import nl.jhvh.sudoku.grid.event.GridEventType
import nl.jhvh.sudoku.grid.event.GridEventType.CELL_REMOVE_CANDIDATES
import nl.jhvh.sudoku.grid.model.cell.Cell

/** [GridEvent] to inform interested listeners of removal of value(s) from the [Cell.valueCandidates] */
class CellRemoveCandidatesEvent (override val eventSource: Cell, val oldValues: Set<Int>, val newValues: Set<Int>): GridEvent {

    override val type: GridEventType = CELL_REMOVE_CANDIDATES

    override fun toString(): String {
        return "${this.javaClass.simpleName}: (eventSource=$eventSource, oldValues=$oldValues, newValues=$newValues)"
    }

}
