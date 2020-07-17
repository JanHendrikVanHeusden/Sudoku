package nl.jhvh.sudoku.grid.event.cellvalue

import nl.jhvh.sudoku.grid.event.GridEvent
import nl.jhvh.sudoku.grid.model.cell.Cell

/** [GridEvent] to inform interested listeners of setting a [Cell]'s value */
/**
 *
 * @property eventSource The [Cell] that is the source of this [CellSetValueEvent]
 * @property newValue The value to which the [Cell] is set
 * @constructor
 */
class CellSetValueEvent (override val eventSource: Cell, val newValue: Int?) : GridEvent<Cell>
