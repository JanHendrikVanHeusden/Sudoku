package nl.jhvh.sudoku.grid.event

/** Base interface for events of interest, e.g. setting values, removing candidates, etc. */
interface ValueEventListener {

    fun onEvent(valueEvent: ValueEvent)

}