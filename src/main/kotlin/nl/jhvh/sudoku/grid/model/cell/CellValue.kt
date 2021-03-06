package nl.jhvh.sudoku.grid.model.cell

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

import nl.jhvh.sudoku.base.CELL_MIN_VALUE
import nl.jhvh.sudoku.format.Formattable
import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.event.ValueEventListener
import nl.jhvh.sudoku.grid.event.ValueEventSource
import nl.jhvh.sudoku.grid.event.ValueEventType
import nl.jhvh.sudoku.grid.event.cellvalue.CellRemoveCandidatesEvent
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.GridElement
import nl.jhvh.sudoku.grid.model.validateValueRange
import nl.jhvh.sudoku.grid.solve.GridNotSolvableException
import nl.jhvh.sudoku.util.log
import nl.jhvh.sudoku.util.requireAndLog
import java.util.Collections.unmodifiableSet
import java.util.concurrent.ConcurrentHashMap
import kotlin.properties.Delegates
import kotlin.reflect.KProperty
import java.util.concurrent.ConcurrentHashMap.newKeySet as ConcurrentHashSet

val VALUE_UNKNOWN: Int? = null

/** Value holder [Class] to represent the numeric or unknown value of a [Cell] and some related properties  */
sealed class CellValue(val cell: Cell) : Formattable, GridElement(cell.grid), ValueEventSource {

    final override val eventListeners: ConcurrentHashMap<ValueEventType, MutableSet<ValueEventListener>> = ConcurrentHashMap()

    final override fun initEventListeners() {
        ValueEventType.values().forEach {
            eventListeners.putIfAbsent(it, ConcurrentHashSet())
        }
    }

    init {
        initEventListeners()
    }

    /**
     * The mutable, numeric or unknown value of this [Cell], observed in case of for non-fixed value.
     *  * Should be volatile to make sure that no unneeded processing is done for a value that is set already.
     *    Kotlin does not allow the `@Volatile` annotation on an observable field however (so maybe implementation
     *    is volatile by nature? I could not really make sure when decompiling the code).
     *  * Not synchronized, typically the value is set from `null` to a non-`null` value ony once,
     *    and if so, always to the same value (and never updated)
     *    So no need for synchronization, even when 2 threads / coroutines would try this at the same time, the result will be the same.
     *
     *  Even if not volatile or synchronized, it would always be set to the same value (an never de-/incremented etc.),
     *  so atomic operations only, so concurrent setting is not really a problem
     */
    abstract var value: Int?
        protected set

    /** @return Whether a known value (`!=` [VALUE_UNKNOWN]) is set, either fixed or mutable */
    abstract val isSet: Boolean

    /** @return Whether the value of this [Cell] is fixed (`true`) or mutable (`false`) */
    abstract val isFixed: Boolean

    abstract fun setValue(value: Int)

    /**
     * Cell values are numbers from [CELL_MIN_VALUE] = 1 up to and including [Grid.maxValue]
     * @param value The value to validate
     * @throws IllegalArgumentException If the given value is not in the proper range for the [Grid].
     */
    @Throws(IllegalArgumentException::class)
    protected fun validateRange(value: Int) {
        try {
            validateValueRange(value, this.cell.grid.maxValue)
        } catch (e: IllegalArgumentException) {
            log().warn {e.message}
            throw e
        }
    }

    /** Value holder class to represent the fixed immutable numeric value of a [Cell]  */
    class FixedValue(cell: Cell, value: Int) : CellValue(cell) {

        override val isFixed: Boolean = true

        // Although it will never change and not be null, due to override it must be var (not val) and nullable
        override var value: Int? = value

        init {
            validateRange(value)
        }

        override fun setValue(value: Int) {
            requireAndLog(value == this.value) { "Not allowed to change a fixed value! (fixed value = ${this.value}, new value = $value, cellRef = ${CellRef(cell.colIndex, cell.rowIndex)})" }
        }

        override val isSet: Boolean = true

    }

    /** Simple value holder class to represent the non-fixed value of a [Cell]  */
    class NonFixedValue(cell: Cell) : CellValue(cell) {

        override val isFixed: Boolean = false

        @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        override var value: Int?
                by Delegates.observable(VALUE_UNKNOWN) { _: KProperty<*>, oldValue: Int?, newValue: Int? ->
//                    synchronized(grid) {
                        if (newValue != oldValue) {
                            let { SetCellValueEvent(this, newValue!!) }
                                    .also { log().trace { "'$this' - about to publish event: $it" } }
                                    .also { publish(it) }
                        }
//                    }
                }

        private fun validateCandidate(value: Int) {
            if (!getValueCandidates().contains(value)) {
                throw GridNotSolvableException("Grid is not solvable: trying to set cell to value '$value'" +
                        " but this value is not present anymore in value candidates of ${cell}")
            }
        }

        override fun setValue(value: Int) {
            if (value != this.value) {
                validateRange(value)
//                synchronized(grid) {
                    validateCandidate(value)
                    this.value = value
//                }
            }
        }

        override val isSet: Boolean
            get() = //synchronized(grid) {
                value != VALUE_UNKNOWN
//            }

        // preferring ConcurrentHashMap.newKeySet over synchronizedSet(mutableSetOf())
        // synchronizedSet gives better read consistency, but slightly worse write performance,
        // and more important: synchronizedSet may throw ConcurrentModificationException when iterating over it while updated concurrently
        /** The [MutableSet] of remaining possible values if the [NonFixedValue] is not solved yet; empty if it was solved. */
        private val valueCandidateSet: MutableSet<Int> = ConcurrentHashSet(if (isFixed) 0 else grid.gridSize)

        init {
            valueCandidateSet.addAll(CELL_MIN_VALUE..grid.maxValue)
        }

        // Read only view on valueCandidates
        // NB: not really immutable, with explicit cast (to MutableSet etc.) one could still mutate it's contents, theoretically.
        //     Returning it as a Set (so formal type is read only) should be enough to discourage external mutation:
        //     it would be too much hassle ( & too much performance penalty) to create a new immutableSet on every get.
        fun getValueCandidates(): Set<Int> {
//            synchronized(grid) {
                return valueCandidateSet
//            }
        }

        /**
         * Removes the given [values] from the [valueCandidateSet], if present.
         * If so, a [CellRemoveCandidatesEvent] is published.
         *  * Method [removeValueCandidate] is lenient for being called multiple times, and does not normally publish
         *    an event when nothing is removed. In race conditions, this may happen incidentally.
         *    No synchronization or guarding mechanism is provided to prevent this, listeners should be lenient for such conditions.
         */
        fun removeValueCandidate(vararg values: Int): Boolean {
            if (values.isEmpty()) {
                return false
            }
            // only publish those actually present in the candidates
//            synchronized(grid) {
                val removeFromCandidates = values.filter { getValueCandidates().contains(it) }.toSet()
                if (removeFromCandidates.isNotEmpty()) {
                    valueCandidateSet.removeAll(removeFromCandidates)
                    publish(CellRemoveCandidatesEvent(this, removeFromCandidates))
                    return true
                }
//            }
            return false
        }

        /**
         * If not empty, all content of [valueCandidateSet] are removed, except the [value] if it was set (solved) already.
         * If [valueCandidateSet] was not empty, a [CellRemoveCandidatesEvent] is published.
         *  * Method [removeValueCandidate] is lenient for being called multiple times, and normally it does not publish
         *    an event when nothing is removed.
         *  * However, the method is not synchronized or otherwise guarded (deliberately),
         *    so in race conditions a [CellRemoveCandidatesEvent] might be published even if [valueCandidates] was already
         *    emptied concurrently.
         */
        fun clearValueCandidatesOnValueSet() {
//            synchronized(grid) {
                // using getValueCandidates() instead of valueCandidates for testability
                if (getValueCandidates().isEmpty()) {
                    return
                }
                // When solved, the valueCandidates list still holds the solved value.
                // NB: when the cell value can not be solved (means: unsolvable grid, invalid Sudoku ! ),
                //     the valueCandidates list will be empty.
                @Suppress("UNCHECKED_CAST") // value is nullable. But still safe: left side is Set of not nullable
                val removedValues = (getValueCandidates() - value) as Set<Int>
                valueCandidateSet.removeAll(removedValues)
                publish(CellRemoveCandidatesEvent(this, unmodifiableSet(removedValues)))
//            }
        }

        /** Technical [toString] method; for a functional representation, see [format]  */
        override fun toString(): String = super.toString() + ", valueCandidates=${valueCandidateSet}"
    }

    /** Technical [toString] method; for a functional representation, see [format]  */
    override fun toString(): String = "${this.javaClass.simpleName} [value=$value], isFixed()=$isFixed]}, colIndex=${cell.colIndex}, rowIndex=${cell.rowIndex}"

    override fun format(formatter: SudokuFormatter): FormattableList = formatter.format(this)

}
