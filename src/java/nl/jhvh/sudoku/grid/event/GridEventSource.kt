package nl.jhvh.sudoku.grid.event

/** Base interface for sources that may emit [GridEvent]s */
interface GridEventSource {

    /**
     * The [Set] of [GridEventListener]s that have subscribed to [GridEvent]s of this [GridEventSource].
     * Should be synchronized to allow concurrent [unsubscribe].
     */
    val eventListeners: MutableSet<GridEventListener>

    /**
     * Subscribe on [GridEvent]s emitted by this [GridEventSource]
     * @param eventListener The interested [GridEventListener]
     */
    fun subscribe(eventListener: GridEventListener) {
        eventListeners.add(eventListener)
    }

    /**
     * Unsubscribe of [GridEvent]s emitted by this [GridEventSource]
     * @param eventListener The [GridEventListener] that is not anymore interested
     */
    fun unsubscribe(eventListener: GridEventListener) {
        eventListeners.remove(eventListener)
    }

    /**
     * Publish a [GridEvent] to subscripted [GridEventListener]s
     * @param gridEvent The [GridEvent] being published
     */
    fun publish(gridEvent: GridEvent) {
        eventListeners.forEach { l: GridEventListener -> l.onEvent(gridEvent) }
    }

    override fun toString(): String
}