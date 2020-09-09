package nl.jhvh.sudoku.grid.event

import nl.jhvh.sudoku.grid.model.cell.CellValue

/** Base interface for events of interest with regard to [CellValue]s, e.g. setting values, removing candidates, etc. */
interface ValueEvent {

    val eventSource: CellValue

    val type: ValueEventType

    override fun toString(): String
}