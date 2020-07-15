package nl.jhvh.sudoku.grid.event.cellvalue

import nl.jhvh.sudoku.grid.event.GridEvent
import nl.jhvh.sudoku.grid.model.cell.Cell

/** [GridEvent] to inform interested listeners of setting a [Cell]'s value */
class CellSetValueEvent (

        /** The [Cell] that is the source of this [CellSetValueEvent] */
        override val eventSource: Cell,

        /** The value to which the [Cell] is set  */
        val newValue: Int) : GridEvent<Cell>
