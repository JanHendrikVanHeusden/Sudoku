package nl.jhvh.sudoku.grid.event.cellvalue

import nl.jhvh.sudoku.grid.model.segment.GridSegment

interface SetCellValueEventHandler {

    fun handleEvent(gridEvent: SetCellValueEvent, listener: GridSegment)

}
