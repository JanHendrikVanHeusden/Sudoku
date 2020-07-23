package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.concatEach
import nl.jhvh.sudoku.format.element.GridFormatter
import nl.jhvh.sudoku.grid.model.Grid

/**
 * Formatter to format a [Grid] using box drawing characters and numbers
 *  * Box drawing characters: ║ │ ═ ─ ╔ ╗ ╚ ╝ ╤ ╧ ╦ ╩ ╟ ╢ ╠ ╣ ╬ ┼ ╪ ╫ etc
 *
 * Typically for console output, to observe results of actions (grid construction, Sudoku solving,
 * testing etc.)
 */
@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") // parameter `grid: Grid` (instead of `grid: Grid`)
class GridBoxFormatter(private val rowFormatter: RowBoxFormatter, private val colFormatter: ColumnBoxFormatter
) : GridFormatter, ElementBoxFormattable<Grid> {

    override fun format(grid: Grid): FormattableList {

        val formattedRows = mutableListOf<String>()
        val bottomBorder: List<String>
        with (rowFormatter) {
            grid.rowList.forEach {
                formattedRows += listOf(getTopLeftEdge(it)) concatEach getTopBorder(it) concatEach listOf(getTopRightEdge(it))
                formattedRows += (getLeftBorder(it) concatEach nakedFormat(it) concatEach getRightBorder(it))
            }
            bottomBorder = listOf(getBottomLeftEdge(grid.rowList.last())) concatEach getBottomBorder(grid.rowList.last()) concatEach listOf(getBottomRightEdge(grid.rowList.last()))
        }
        return FormattableList(formattedRows + bottomBorder)
    }

    override fun nakedFormat(grid: Grid): FormattableList {
        val topRowsWithBottomBorder = grid.rowList.dropLast(1).map {
            rowFormatter.nakedFormat(it) + rowFormatter.getBottomBorder(it)
        }.flatten()
        val bottomRowNaked = rowFormatter.nakedFormat(grid.rowList.last())
        return FormattableList(topRowsWithBottomBorder + bottomRowNaked)
    }

    override fun getLeftBorder(grid: Grid): FormattableList {
        return colFormatter.getLeftBorder(grid.colList.first())
    }

    override fun getRightBorder(grid: Grid): FormattableList {
        return colFormatter.getRightBorder(grid.colList.last())
    }

    override fun getTopBorder(grid: Grid): FormattableList {
        return rowFormatter.getTopBorder(grid.rowList.first())
    }

    override fun getBottomBorder(grid: Grid): FormattableList {
        return rowFormatter.getBottomBorder(grid.rowList.last())
    }

    override fun getTopLeftEdge(grid: Grid): String = rowFormatter.getTopLeftEdge(grid.rowList.first())

    override fun getTopRightEdge(grid: Grid): String = rowFormatter.getTopRightEdge(grid.rowList.first())

    override fun getBottomLeftEdge(grid: Grid): String = rowFormatter.getBottomLeftEdge(grid.rowList.last())

    override fun getBottomRightEdge(grid: Grid): String = rowFormatter.getBottomRightEdge(grid.rowList.last())
}
