package nl.jhvh.sudoku.grid.model.cell

import nl.jhvh.sudoku.base.CELL_MIN_VALUE
import nl.jhvh.sudoku.format.Formattable
import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.event.cellvalue.CellSetValueEvent
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.GridElement
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

val VALUE_UNKNOWN: Int? = null

/** Simple value holder [Class] to represent the numeric or unknown value of a [Cell] and some related properties  */
sealed class CellValue(val cell: Cell) : Formattable, GridElement(cell.grid) {

    /**
     * The mutable, numeric or unknown value of this [Cell], observed (see [CellSetValueEvent]).
     *  * Should be volatile to make sure that no unneeded processing is done for a value that is set already.
     *    Kotlin does not allow the `@Volatile` annotation on an observable field however (so maybe implementation
     *    is volatile by nature? I could not really make sure when decompiling the code).
     *  * Not synchronized, typically the value is set from `null` to a non-`null` value ony once,
     *    and if so, always to the same value (and never de-/incremented)
     *    So no need for synchronization, even when 2 threads / coroutines would try this at the same time, the result will be the same.
     *
     *  Even if not volatile or synchronized, it would always be set to the same value (an never de-/incremented etc.),
     *  so atomic operations only, so concurrent setting is not really a problem
     */
    @Suppress("UNUSED_ANONYMOUS_PARAMETER")
    var value: Int?
            by Delegates.observable(VALUE_UNKNOWN) { _: KProperty<*>, oldValue: Int?, newValue: Int? ->
                // NB: setting to same value as before does not publish an event :-)
                this.cell.publish(CellSetValueEvent(cell, newValue!!))
            }
    protected set

    /**
     * Cell values are numbers from [CELL_MIN_VALUE] = 1 up to and including [Grid.maxValue]
     * @param value The value to validate
     * @throws IllegalArgumentException If the given value is not in the proper range for the [Grid].
     */
    @Throws(IllegalArgumentException::class)
    protected fun validateRange(value: Int) {
        require(value >= CELL_MIN_VALUE) { "A cell value must be $CELL_MIN_VALUE or higher but is $value" }
        require(value <= this.cell.grid.maxValue) { "A cell value must be at most ${this.cell.grid.maxValue} but is $value" }
    }

    /** Value holder class to represent the fixed immutable numeric value of a [Cell]  */
    class FixedValue(cell: Cell, value: Int) : CellValue(cell) {
        override val isFixed: Boolean = true

        init {
            validateRange(value)
            this.value = value
        }

        override fun setValue(value: Int) {
            require (value == this.value) { "Not allowed to change a fixed value! (value = $value)" }
        }
    }

    /** Simple value holder class to represent the non-fixed value of a [Cell]  */
    class NonFixedValue(cell: Cell) : CellValue(cell) {
        override val isFixed: Boolean = false

        override fun setValue(value: Int) {
            validateRange(value)
            this.value = value
        }
    }

    /** @return Whether the value of this [Cell] is fixed (`true`) or mutable (`false`) */
    abstract val isFixed: Boolean

    /** @return Whether a known value (`!=` [VALUE_UNKNOWN]) is set, either fixed or mutable */
    fun hasValue(): Boolean = this.value != VALUE_UNKNOWN

    abstract fun setValue(value: Int)

    /** Technical [toString] method; for a functional representation, see [format]  */
    override fun toString(): String = "${this.javaClass.simpleName} [value=$value], isFixed()=$isFixed]"

    override fun format(formatter: SudokuFormatter): FormattableList = formatter.format(this)

}
