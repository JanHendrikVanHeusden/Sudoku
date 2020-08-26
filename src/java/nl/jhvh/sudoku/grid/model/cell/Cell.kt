package nl.jhvh.sudoku.grid.model.cell

import nl.jhvh.sudoku.base.CELL_MIN_VALUE
import nl.jhvh.sudoku.base.intRangeSet
import nl.jhvh.sudoku.format.Formattable
import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.event.GridEventListener
import nl.jhvh.sudoku.grid.event.candidate.CellRemoveCandidatesEvent
import nl.jhvh.sudoku.grid.event.candidate.CellRemoveCandidatesSource
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.GridElement
import nl.jhvh.sudoku.grid.model.cell.CellValue.FixedValue
import nl.jhvh.sudoku.grid.model.cell.CellValue.NonFixedValue
import java.util.concurrent.ConcurrentHashMap

/**
 * A [Cell] in a Sudoku represents a single square in a [Grid]
 *  * On construction, the [Cell] can either be empty, or filled with a single numeric value; see also [CellValue]
 *  * The [Cell] is aware of it's position within the [Grid], indicated by [colIndex] (X-axis) and [rowIndex] (Y-axis)
 */
class Cell(grid: Grid, val colIndex: Int, val rowIndex: Int, val fixedValue: Int? = null): GridElement(grid), CellRemoveCandidatesSource, Formattable {

    val isFixed: Boolean = fixedValue != null

    override val eventListeners: MutableSet<GridEventListener<Cell, CellRemoveCandidatesEvent>> = mutableSetOf()

    val cellValue: CellValue = if (isFixed) FixedValue(this, fixedValue!!) else NonFixedValue(this)

    // preferring ConcurrentHashMap.newKeySet over synchronizedSet(mutableSetOf())
    // synchronizedSet gives better read consistency, but slightly worse write performance,
    // and worse: it may throw ConcurrentModificationException when iterating over it while updated concurrently
    private val valueCandidates: MutableSet<Int> = ConcurrentHashMap.newKeySet(if (isFixed) 0 else grid.gridSize)

    init {
        if (!isFixed) {
            valueCandidates.addAll(intRangeSet(CELL_MIN_VALUE, grid.maxValue))
        }
    }
    fun getValueCandidates(): Set<Int> = valueCandidates

    fun removeValueCandidate(value: Int): Boolean {
        let { valueCandidates.remove(value) }
                .also {removed ->
                    if (removed) {
                        publish(CellRemoveCandidatesEvent(this, setOf(value)))
                    }
                }
                .also { return it }
    }
    fun clearValueCandidates() {
        val oldValues = HashSet(valueCandidates)
        if (oldValues.isNotEmpty()) {
            valueCandidates.clear()
            publish(CellRemoveCandidatesEvent(this, oldValues))
        }
    }

    /** Technical [toString] method; for a functional representation, see [format]  */
    override fun toString(): String = "${this.javaClass.simpleName}: colIndex=$colIndex, rowIndex=$rowIndex, cellValue=[$cellValue], valueCandidates=${getValueCandidates()}"

    override fun format(formatter: SudokuFormatter): FormattableList = formatter.format(this)
}

