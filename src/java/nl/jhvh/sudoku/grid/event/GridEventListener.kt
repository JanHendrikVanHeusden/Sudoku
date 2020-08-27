package nl.jhvh.sudoku.grid.event

/** Base interface for events of interest with for grid elements, e.g. setting values, removing candidates, etc. */
interface GridEventListener {

    fun onEvent(gridEvent: GridEvent)

}