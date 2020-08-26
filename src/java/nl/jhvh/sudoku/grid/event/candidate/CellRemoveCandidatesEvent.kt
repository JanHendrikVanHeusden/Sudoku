package nl.jhvh.sudoku.grid.event.candidate

import nl.jhvh.sudoku.grid.event.GridEvent
import nl.jhvh.sudoku.grid.model.cell.Cell

/**
 * [GridEvent] to inform interested listeners of removal of value(s) from the [Cell.valueCandidates]
 * @property eventSource The [Cell] that is the source of this [CellRemoveCandidatesEvent]
 * @property removedCandidates The values being removed from the [Cell.valueCandidates]
 * @constructor
 */
class CellRemoveCandidatesEvent (override val eventSource: Cell, val removedCandidates: Set<Int>) : GridEvent<Cell> {

    override fun toString(): String {
        return "${this.javaClass.simpleName}: (eventSource=$eventSource, removedCandidates=$removedCandidates)"
    }

}
