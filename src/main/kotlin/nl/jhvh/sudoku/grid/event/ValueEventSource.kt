package nl.jhvh.sudoku.grid.event

/* Copyright 2020 Jan-Hendrik van Heusden

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

import nl.jhvh.sudoku.util.log
import java.util.concurrent.ConcurrentHashMap

/** Base interface for sources that may emit [ValueEvent]s */
interface ValueEventSource {

    /**
     * The [Set] of [ValueEventListener]s that have subscribed to [ValueEvent]s of this [ValueEventSource].
     * Should be synchronized to allow concurrent [unsubscribe].
     */
    val eventListeners: ConcurrentHashMap<ValueEventType, MutableSet<ValueEventListener>>

    /** Initialize the [eventListeners] with thread safe [MutableSet]s */
    fun initEventListeners()

    /**
     * Subscribe on [ValueEvent]s emitted by this [ValueEventSource]
     * @param eventListener The interested [ValueEventListener]
     * @param eventType The [ValueEventType] to subscribe for
     */
    fun subscribe(eventListener: ValueEventListener, eventType: ValueEventType) {
        // Should be initialized with event types, so eventListeners[eventType] will always return a non-null Set
        val isAdded = eventListeners[eventType]!!.add(eventListener)
        if (isAdded) {
            log().trace { "$eventListener subscribed for eventType $eventType" }
        } else {
            log().trace { "Duplicate subscription of $eventListener is duplicate: subscribed already for eventType $eventType" }
        }
    }

    /**
     * Unsubscribe of [ValueEvent]s emitted by this [ValueEventSource]
     * @param eventListener The [ValueEventListener] that is not anymore interested
     * @param eventType The [ValueEventType] to unsubscribe from
     */
    fun unsubscribe(eventListener: ValueEventListener, eventType: ValueEventType) {
        val typedListeners = eventListeners[eventType]
        if (typedListeners.isNullOrEmpty()) {
            return
        }
        val isRemoved = typedListeners.remove(eventListener)
        if (isRemoved) {
            log().trace { "$eventListener unsubscribed for eventType $eventType" }
        }
    }

    /**
     * Publish a [ValueEvent] to subscripted [ValueEventListener]s
     * @param valueEvent The [ValueEvent] being published
     */
    fun publish(valueEvent: ValueEvent) {
        eventListeners[valueEvent.type]?.forEach { l: ValueEventListener -> l.onEvent(valueEvent) }
        log().trace { "Published event: $valueEvent to ${eventListeners.size} listeners" }
    }

    override fun toString(): String
}