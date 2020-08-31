package nl.jhvh.sudoku.grid.event

import nl.jhvh.sudoku.grid.model.GridElement

/** Base interface for events of interest with regard to [GridElement]s, e.g. setting values, removing candidates, etc. */
interface GridEvent {

    val eventSource: GridElement

    val type: GridEventType

    override fun toString(): String
}