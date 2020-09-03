package nl.jhvh.sudoku.grid.model.cell

import nl.jhvh.sudoku.base.CELL_MIN_VALUE
import nl.jhvh.sudoku.format.Formattable
import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.event.candidate.CellRemoveCandidatesEvent
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.GridElement
import nl.jhvh.sudoku.grid.model.cell.CellValue.FixedValue
import nl.jhvh.sudoku.grid.model.cell.CellValue.NonFixedValue
import nl.jhvh.sudoku.util.intRangeSet
import java.util.concurrent.ConcurrentHashMap.newKeySet as ConcurrentHashSet

/**
 * A [Cell] in a Sudoku represents a single square in a [Grid]
 *  * On construction, the [Cell] can either be empty, or filled with a single numeric value; see also [CellValue]
 *  * The [Cell] is aware of it's position within the [Grid], indicated by [colIndex] (X-axis) and [rowIndex] (Y-axis)
 */
class Cell(grid: Grid, val colIndex: Int, val rowIndex: Int, val fixedValue: Int? = null): GridElement(grid), Formattable {

    val isFixed: Boolean = fixedValue != null

    // preferring ConcurrentHashMap.newKeySet over synchronizedSet(mutableSetOf())
    // synchronizedSet gives better read consistency, but slightly worse write performance,
    // and more important: synchronizedSet may throw ConcurrentModificationException when iterating over it while updated concurrently
    private val valueCandidates: MutableSet<Int> = ConcurrentHashSet(if (isFixed) 0 else grid.gridSize)

    init {
        if (!isFixed) {
            valueCandidates.addAll(intRangeSet(CELL_MIN_VALUE, grid.maxValue))
        }
    }
    fun getValueCandidates(): Set<Int> = valueCandidates

    fun removeValueCandidate(value: Int): Boolean {
        if (valueCandidates.contains(value)) {
            val oldValues = HashSet(valueCandidates)
            if (valueCandidates.remove(value)) {
                publish(CellRemoveCandidatesEvent(this, oldValues, valueCandidates))
                return true
            }
        }
        return false
    }

    fun clearValueCandidates() {
        if (valueCandidates.isEmpty()) {
            return
        }
        val oldValues = HashSet(valueCandidates)
        valueCandidates.clear()
        publish(CellRemoveCandidatesEvent(this, oldValues, valueCandidates))
    }

    val cellValue: CellValue = if (isFixed) FixedValue(this, fixedValue!!) else NonFixedValue(this)

    /** Technical [toString] method; for a functional representation, see [format]  */
    override fun toString(): String = "${this.javaClass.simpleName}: colIndex=$colIndex, rowIndex=$rowIndex, cellValue=[$cellValue], valueCandidates=${getValueCandidates()}"

    override fun format(formatter: SudokuFormatter): FormattableList = formatter.format(this)
}

