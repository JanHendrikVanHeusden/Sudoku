package nl.jhvh.sudoku.grid.model.cell

import nl.jhvh.sudoku.base.CELL_MIN_VALUE
import nl.jhvh.sudoku.format.Formattable
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.event.cellvalue.CellSetValueEvent
import nl.jhvh.sudoku.grid.model.Grid
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/** Simple value holder [Class] to represent the numeric or unknown value of a [Cell] and some related properties  */
sealed class CellValue(val cell: Cell) : Formattable {

    /** The mutable, numeric or unknown value of this [Cell], observed (see [CellSetValueEvent] */
    protected var value: Int?
            by Delegates.observable(VALUE_UNKNOWN) { _: KProperty<*>, _: Int?, newValue: Int? ->
                this.cell.publish(CellSetValueEvent(cell, newValue!!))
            }

    /** Simple value holder class to represent the fixed immutable numeric value of a [Cell]  */
    class FixedValue(cell: Cell, value: Int) : CellValue(cell) {
        override val isFixed: Boolean = true

        init {
            validateRange(value)
            this.value = value
        }

        /**
         * Cell values are numbers from [CELL_MIN_VALUE] = 1 up to and including [Grid.maxValue]
         * @param value The value to validate
         * @throws IllegalArgumentException If the given value is not in the proper range for the [Grid].
         */
        @Throws(IllegalArgumentException::class)
        private fun validateRange(value: Int) {
            require(value >= CELL_MIN_VALUE) { "A cell value must be $CELL_MIN_VALUE or higher" }
            require(value <= this.cell.grid.maxValue) { "A cell value must be at most ${this.cell.grid.maxValue}" }
        }

    }

    /** Simple value holder class to represent the unknown value of a [Cell]  */
    class UnknownValue(cell: Cell) : CellValue(cell) {
        override val isFixed: Boolean = false
    }

    /** @return Whether the value of this [Cell] is fixed (`true`) or mutable (`false`) */
    abstract val isFixed: Boolean

    /** Technical [toString] method; for a functional representation, see [format]  */
    override fun toString(): String = "${this.javaClass.simpleName} [value=$value], isFixed()=$isFixed]"

    override fun format(formatter: SudokuFormatter): List<String> = formatter.format(this)

}
