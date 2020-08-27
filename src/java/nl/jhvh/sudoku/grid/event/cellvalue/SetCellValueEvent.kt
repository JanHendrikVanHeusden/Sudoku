package nl.jhvh.sudoku.grid.event.cellvalue

import nl.jhvh.sudoku.grid.event.GridEvent
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.cell.CellValue

/**
 * [GridEvent] to inform interested listeners of setting a [CellValue]'s value
 * @property eventSource The [Cell] that is the source of this [SetCellValueEvent]
 * @property newValue The value to which the [Cell] is set
 * @constructor
 */
class SetCellValueEvent (override val eventSource: CellValue, val newValue: Int) : GridEvent {

    override fun toString(): String {
        return "${this.javaClass.simpleName}: (eventSource=$eventSource, newValue=$newValue)"
    }

}
