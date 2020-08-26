package nl.jhvh.sudoku.grid.event.cellvalue

import nl.jhvh.sudoku.grid.event.GridEventListener
import nl.jhvh.sudoku.grid.event.GridEventSource
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.cell.CellValue

interface SetCellValueSource : GridEventSource<CellValue, SetCellValueEvent> {

    /** @return The [Set] of [GridEventListener]s that have subscribed to [SetCellValueEvent]s of this [Cell] */
    override val eventListeners: MutableSet<GridEventListener<CellValue, SetCellValueEvent>>
}
