package nl.jhvh.sudoku.grid.model.cell

import nl.jhvh.sudoku.base.CELL_MIN_VALUE
import nl.jhvh.sudoku.format.Formattable
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.event.cellvalue.CellSetValueEvent
import nl.jhvh.sudoku.grid.model.Grid

/** Simple value holder [Class] to represent the numeric or unknown value of a [Cell] and some related properties  */
sealed class CellValue(val cell: Cell) : Formattable {

    /** Simple value holder class to represent the fixed immutable numeric value of a [Cell]  */
    class FixedValue(cell: Cell, value: Int) : CellValue(cell) {
        init {
            this.value = value
        }
        override val isFixed: Boolean = true
    }

    /** Simple value holder class to represent the mutable numeric or unknown value of a [Cell]  */
    class MutableValue(cell: Cell) : CellValue(cell) {
        override val isFixed: Boolean = false
    }

    /** The mutable, numeric or unknown value of this [Cell]  */
    var value: Int? = VALUE_UNKNOWN
        protected set

    /**
     * Validate and set the numeric or unknown value of this [Cell].
     *
     * Validation here is a simple range validation.
     *  * It does **not** validate whether or not the [value] is correct regarding the solution of the Sudoku.
     * @param value The value to set the [Cell] to; `null` ([VALUE_UNKNOWN]) not allowed.
     * @throws IllegalArgumentException If the given value is not in the proper range for the [Grid].
     * @see .validateValueInRange
     */
    @Throws(IllegalArgumentException::class)
    protected fun setValue(value: Int) {
        validateRange(value)
        this.value = value
        val event = CellSetValueEvent(cell, value)
        this.cell.publish(event)
        this.cell.valueCandidates.clear()
    }

    /**
     * Business rule: cell values are numbers from 1 up to and including [Grid.maxValue]
     * @param value The value to validate
     * @throws IllegalArgumentException If the given value is not in the proper range for the [Grid].
     */
    @Throws(IllegalArgumentException::class)
    private fun validateRange(value: Int) {
        require(value >= CELL_MIN_VALUE) { "A cell value must be $CELL_MIN_VALUE or higher" }
        require(value <= this.cell.grid.maxValue) { "A cell value must be at most ${this.cell.grid.maxValue}" }
    }

    /** @return Whether the value of this [Cell] is fixed (`true`) or mutable (`false`) */
    abstract val isFixed: Boolean

    /** Technical [toString] method; for a functional representation, see [format]  */
    override fun toString(): String = "${this.javaClass.simpleName} [value=$value], isFixed()=$isFixed]"

    override fun format(formatter: SudokuFormatter): List<String> = formatter.format(this)

}
