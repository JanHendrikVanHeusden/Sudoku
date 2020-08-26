package nl.jhvh.sudoku.grid.event

import nl.jhvh.sudoku.grid.model.GridElement

/**
 * Base interface for events of interest with regard to [GridElement]s,
 * e.g. setting values, removing candidates, etc.
 * @param [S] The source element type of this [GridEvent]
 */
interface GridEvent<S : GridElement> {

    /** @return The source [GridElement] that emitted this [GridEvent] */
    val eventSource: S

}
