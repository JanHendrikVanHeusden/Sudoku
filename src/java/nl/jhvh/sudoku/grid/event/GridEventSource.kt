package nl.jhvh.sudoku.grid.event

import nl.jhvh.sudoku.grid.model.GridElement

/**
 * Base interface for sources that may emit [GridEvent]s
 * @param S The source [GridElement] sub type
 * @param E The [GridEvent] sub type being emitted
*/
interface GridEventSource<S : GridElement, E : GridEvent<S>> {
    /**
     * Subscribe an [GridEventListener] for [GridEvent]s emitted by this [GridEventSource]
     * @param eventListener The [GridEventListener] interested in [GridEvent]s of this [GridEventSource]
     */
    fun subscribe(eventListener: GridEventListener<S, E>) {
        eventListeners.add(eventListener)
    }

    /**
     * Unsubscribe a [GridEventListener] for [GridEvent]s emitted by this [GridEventSource]
     * @param eventListener The [GridEventListener] not longer interested in [GridEvent]s of this [GridEventSource]
     */
    fun unsubscribe(eventListener: GridEventListener<S, E>) {
        eventListeners.remove(eventListener)
    }

    /**
     * Publish a [GridEvent] that a subscribing [GridEventListener] may react on
     * @param gridEvent The [GridEvent] being published
     */
    fun publish(gridEvent: E) {
        eventListeners.forEach { l: GridEventListener<S, E> -> l.onEvent(gridEvent) }
    }

    /** @return The [Set] of [GridEventListener]s that have subscribed to [GridEvent]s of this [GridEventSource] */
    val eventListeners: MutableSet<GridEventListener<S, E>>
}
