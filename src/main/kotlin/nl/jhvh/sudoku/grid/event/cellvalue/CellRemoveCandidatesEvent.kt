package nl.jhvh.sudoku.grid.event.cellvalue

import nl.jhvh.sudoku.grid.event.ValueEvent
import nl.jhvh.sudoku.grid.event.ValueEventType
import nl.jhvh.sudoku.grid.event.ValueEventType.CELL_REMOVE_CANDIDATES
import nl.jhvh.sudoku.grid.model.cell.CellValue.NonFixedValue

/** [ValueEvent] to inform interested listeners of removal of value(s) from the [NonFixedValue.valueCandidates] */
class CellRemoveCandidatesEvent (override val eventSource: NonFixedValue, val oldValues: Set<Int>, val newValues: Set<Int>): ValueEvent {

    override val type: ValueEventType = CELL_REMOVE_CANDIDATES

    override fun toString(): String {
        return "${this.javaClass.simpleName}: (eventSource=$eventSource, oldValues=$oldValues, newValues=$newValues)"
    }

}
