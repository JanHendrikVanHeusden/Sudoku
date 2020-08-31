package nl.jhvh.sudoku.grid.event

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentHashMap.newKeySet as ConcurrentHashSet

/** Base interface for sources that may emit [GridEvent]s */
interface GridEventSource {

    /**
     * The [Set] of [GridEventListener]s that have subscribed to [GridEvent]s of this [GridEventSource].
     * Should be synchronized to allow concurrent [unsubscribe].
     */
    val eventListeners: ConcurrentHashMap<GridEventType, MutableSet<GridEventListener>>

    /**
     * Subscribe on [GridEvent]s emitted by this [GridEventSource]
     * @param eventListener The interested [GridEventListener]
     * @param eventType The [GridEventType] to subscribe for
     */
    fun subscribe(eventListener: GridEventListener, eventType: GridEventType) {
        eventListeners.putIfAbsent(eventType, ConcurrentHashSet())
        eventListeners[eventType]?.add(eventListener)
    }

    /**
     * Unsubscribe of [GridEvent]s emitted by this [GridEventSource]
     * @param eventListener The [GridEventListener] that is not anymore interested
     * @param eventType The [GridEventType] to unsubscribe from
     */
    fun unsubscribe(eventListener: GridEventListener, eventType: GridEventType) {
        eventListeners[eventType]?.remove(eventListener)
    }

    /**
     * Publish a [GridEvent] to subscripted [GridEventListener]s
     * @param gridEvent The [GridEvent] being published
     */
    fun publish(gridEvent: GridEvent) {
        eventListeners[gridEvent.type]?.forEach { l: GridEventListener -> l.onEvent(gridEvent) }
    }

    override fun toString(): String
}