package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.concatEach
import nl.jhvh.sudoku.format.element.ColumnFormatter
import nl.jhvh.sudoku.grid.model.segment.Col

/**
 * Formatter to format a [Col] using box drawing characters and numbers
 *  * Box drawing characters: ║ │ ═ ─ ╔ ╗ ╚ ╝ ╤ ╧ ╦ ╩ ╟ ╢ ╠ ╣ ╬ ┼ ╪ ╫ etc.
 *
 * Typically for console output, to observe results of actions (grid construction, Sudoku solving,
 * testing etc.)
 */
@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") // parameter `col: Col` (instead of `element: Col`)
class ColumnBoxFormatter: ColumnFormatter, BoxBorderingFormatter<Col> {

    private val cellFormatter = CellBoxFormatter()

    override fun format(col: Col): FormattableList {
        val topBorder =
                listOf(getTopLeftEdge(col)) concatEach getTopBorder(col) as List<String> concatEach listOf(getTopRightEdge(col))
        val valueWithLeftRightBorders =
                getLeftBorder(col) as List<String> concatEach nakedFormat(col) as List<String> concatEach getRightBorder(col) as List<String>
        val bottomBorder =
                listOf(getBottomLeftEdge(col)) concatEach getBottomBorder(col) as List<String> concatEach listOf(getBottomRightEdge(col))
        return FormattableList(topBorder + valueWithLeftRightBorders + bottomBorder)
    }

    override fun nakedFormat(col: Col): FormattableList {
        val topCellsWithBottomBorder = col.cellList.dropLast(1)
                .map {cellFormatter.nakedFormat(it) + cellFormatter.getBottomBorder(it)
        }.flatten()
        val bottomCellNaked = cellFormatter.nakedFormat(col.cellList.last())
        return FormattableList(topCellsWithBottomBorder + bottomCellNaked)
    }

    override fun getLeftBorder(col: Col): FormattableList {
        val topCellsLeftBordersWithBottomEdge = col.cellList.dropLast(1)
                .map { cellFormatter.getLeftBorder(it) + listOf(cellFormatter.getBottomLeftEdge(it)) }.flatten()
        val leftBorderOfBottomCell = cellFormatter.getLeftBorder(col.cellList.last())
        return FormattableList(topCellsLeftBordersWithBottomEdge + leftBorderOfBottomCell)
    }

    override fun getRightBorder(col: Col): FormattableList {
        val topCellsRightBordersWithBottomEdge = col.cellList.dropLast(1)
                .map { cellFormatter.getRightBorder(it) as List<String> + listOf(cellFormatter.getBottomRightEdge(it)) }.flatten()
        val rightBorderOfBottomCell = cellFormatter.getRightBorder(col.cellList.last()) as List<String>
        return FormattableList(topCellsRightBordersWithBottomEdge + rightBorderOfBottomCell)
    }

    override fun getTopBorder(col: Col): FormattableList {
        return cellFormatter.getTopBorder(col.cellList.first())
    }

    override fun getBottomBorder(col: Col): FormattableList {
        return cellFormatter.getBottomBorder(col.cellList.last())
    }

    override fun getTopLeftEdge(col: Col): String = cellFormatter.getTopLeftEdge(col.cellList.first())

    override fun getTopRightEdge(col: Col): String = cellFormatter.getTopRightEdge(col.cellList.first())

    override fun getBottomLeftEdge(col: Col): String = cellFormatter.getBottomLeftEdge(col.cellList.last())

    override fun getBottomRightEdge(col: Col): String = cellFormatter.getBottomRightEdge(col.cellList.last())

}
