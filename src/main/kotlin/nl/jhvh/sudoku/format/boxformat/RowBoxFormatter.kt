package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.concatEach
import nl.jhvh.sudoku.format.element.RowFormatter
import nl.jhvh.sudoku.grid.model.segment.Row

/**
 * Formatter to format a [Row] using box drawing characters and numbers. Stateless.
 *  * Box drawing characters: ║ │ ═ ─ ╔ ╗ ╚ ╝ ╤ ╧ ╦ ╩ ╟ ╢ ╠ ╣ ╬ ┼ ╪ ╫ etc.
 *
 * Typically for console output, to observe results of actions (grid construction, Sudoku solving,
 * testing etc.)
 */
@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") // parameter `row: Row` (instead of `element: Row`)
class RowBoxFormatter(private val cellFormatter: CellBoxFormatter) : RowFormatter, ElementBoxFormattable<Row> {

    override fun format(row: Row): FormattableList {
        val topBorder =
                listOf(getTopLeftEdge(row)) concatEach getTopBorder(row) concatEach listOf(getTopRightEdge(row))
        val valueWithLeftRightBorders =
                getLeftBorder(row) concatEach nakedFormat(row) concatEach getRightBorder(row)
        val bottomBorder =
                listOf(getBottomLeftEdge(row)) concatEach getBottomBorder(row) concatEach listOf(getBottomRightEdge(row))
        return FormattableList(topBorder + valueWithLeftRightBorders + bottomBorder)
    }

    override fun nakedFormat(row: Row): FormattableList {
        val leftCellsWithRightBorder = concatEach(*row.cells.toList().dropLast(1).map {
            cellFormatter.nakedFormat(it) concatEach cellFormatter.getRightBorder(it)
        }.toTypedArray())
        val rightCellNaked = cellFormatter.nakedFormat(row.cells.last())
        return FormattableList(leftCellsWithRightBorder concatEach rightCellNaked)
    }

    override fun getLeftBorder(row: Row): FormattableList {
        return cellFormatter.getLeftBorder(row.cells.first())
    }

    override fun getRightBorder(row: Row): FormattableList {
        return cellFormatter.getRightBorder(row.cells.last())
    }

    override fun getTopBorder(row: Row): FormattableList {
        val leftCellsTopBordersWithRightEdge = concatEach(*row.cells.toList().dropLast(1).map {
            cell -> cellFormatter.getTopBorder(cell).map { border -> border + cellFormatter.getTopRightEdge(cell)}
        }.toTypedArray())
        val rightCellTopBorder = cellFormatter.getTopBorder(row.cells.last()) as List<String>
        return FormattableList(leftCellsTopBordersWithRightEdge concatEach rightCellTopBorder)
    }

    override fun getBottomBorder(row: Row): FormattableList {
        val leftCellsBottomBordersWithRightEdge = concatEach(*row.cells.toList().dropLast(1).map {
            cell -> cellFormatter.getBottomBorder(cell).map { border -> border + cellFormatter.getBottomRightEdge(cell)}
        }.toTypedArray())
        val rightCellBottomBorder = cellFormatter.getBottomBorder(row.cells.last()) as List<String>
        return FormattableList(leftCellsBottomBordersWithRightEdge concatEach rightCellBottomBorder)
    }

    override fun getTopLeftEdge(row: Row): String = cellFormatter.getTopLeftEdge(row.cells.first())

    override fun getTopRightEdge(row: Row): String = cellFormatter.getTopRightEdge(row.cells.last())

    override fun getBottomLeftEdge(row: Row): String = cellFormatter.getBottomLeftEdge(row.cells.first())

    override fun getBottomRightEdge(row: Row): String = cellFormatter.getBottomRightEdge(row.cells.last())

}