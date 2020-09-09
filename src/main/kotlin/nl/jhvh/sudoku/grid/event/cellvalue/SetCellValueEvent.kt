package nl.jhvh.sudoku.grid.event.cellvalue

import nl.jhvh.sudoku.grid.event.ValueEvent
import nl.jhvh.sudoku.grid.event.ValueEventType
import nl.jhvh.sudoku.grid.event.ValueEventType.SET_CELL_VALUE
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.cell.CellValue

/**
 * [ValueEvent] to inform interested listeners of setting a [CellValue]'s value.
 * @property eventSource The [Cell] that is the source of this [SetCellValueEvent]
 * @property newValue The value to which the [Cell] is set
 */
class SetCellValueEvent (override val eventSource: CellValue, val newValue: Int) : ValueEvent {

    override val type: ValueEventType = SET_CELL_VALUE

    override fun toString(): String {
        return "${this.javaClass.simpleName}: (eventSource=$eventSource, newValue=$newValue)"
    }

}
