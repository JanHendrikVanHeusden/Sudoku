package nl.jhvh.sudoku.grid.event

import nl.jhvh.sudoku.grid.model.GridElement

/**
 * Base interface for events of interest with of [GridElement]s, e.g. setting values, removing candidates, etc.
 * @param S The source element type of this [GridEvent]
 * @param E The [GridEvent] (sub)type of this event
*/
interface GridEventListener<S : GridElement, E : GridEvent<S>> {

    /**
     * Method to react on / handle the [GridEvent]
     * @param gridEvent The [GridEvent] to react on
     */
    fun onEvent(gridEvent: E)
}
