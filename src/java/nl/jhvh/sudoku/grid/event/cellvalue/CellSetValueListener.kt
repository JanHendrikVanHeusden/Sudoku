package nl.jhvh.sudoku.grid.event.cellvalue

import nl.jhvh.sudoku.grid.event.GridEventListener
import nl.jhvh.sudoku.grid.model.cell.Cell

/** Interface to define [CellSetValueListener]<[Cell], [CellSetValueEvent]> */
interface CellSetValueListener : GridEventListener<Cell, CellSetValueEvent> {

    override fun onEvent(setValueEvent: CellSetValueEvent)
}
