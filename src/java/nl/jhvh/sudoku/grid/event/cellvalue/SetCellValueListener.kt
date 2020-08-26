package nl.jhvh.sudoku.grid.event.cellvalue

import nl.jhvh.sudoku.grid.event.GridEventListener
import nl.jhvh.sudoku.grid.model.cell.CellValue

interface SetCellValueListener : GridEventListener<CellValue, SetCellValueEvent> {

    override fun onEvent(gridEvent: SetCellValueEvent)
}
