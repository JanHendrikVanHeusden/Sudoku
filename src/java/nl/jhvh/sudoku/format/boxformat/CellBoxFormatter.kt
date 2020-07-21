package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.bottomBorder
import nl.jhvh.sudoku.format.bottomLeftEdge
import nl.jhvh.sudoku.format.bottomRightEdge
import nl.jhvh.sudoku.format.concatEach
import nl.jhvh.sudoku.format.element.CellFormatter
import nl.jhvh.sudoku.format.leftBorder
import nl.jhvh.sudoku.format.rightBorder
import nl.jhvh.sudoku.format.topBorder
import nl.jhvh.sudoku.format.topLeftEdge
import nl.jhvh.sudoku.format.topRightEdge
import nl.jhvh.sudoku.grid.model.cell.Cell

/**
 * Simple formatting of a [Cell], typically for output to [System.out].
 *  * The height of the cell value of a formatted [Cell] is always 1, the heigth of the top and bottom borders is 1 each.
 *  * The width of a cell value varies with the width of the grid's highest possible cell value; for `3*3` block size the
 *    highest value 1 9, for `4*4` 16 etc.
 * Borders:
 *  * The height of a top or bottom border is always 1. The width varies by the width of the formatted value.
 *  * The width and height of left and richt borders are always 1.
 */
@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") // parameter cell: Cell (instead of element: cell)
class CellBoxFormatter: CellFormatter, BoxBorderingFormatter<Cell> {

    private val cellValueFormatter = SimpleCellValueFormatter()

    override fun format(cell: Cell): FormattableList {
        val topBorder =
                listOf(getTopLeftEdge(cell)) concatEach getTopBorder(cell) concatEach listOf(getTopRightEdge(cell))
        val valueWithLeftRightBorders =
                getLeftBorder(cell) as List<String> concatEach cellValueFormatter.format(cell.cellValue) concatEach getRightBorder(cell) as List<String>
        val bottomBorder =
                listOf(getBottomLeftEdge(cell)) concatEach getBottomBorder(cell) concatEach listOf(getBottomRightEdge(cell))
        return FormattableList(topBorder + valueWithLeftRightBorders + bottomBorder)
    }

    override fun nakedFormat(cell: Cell): FormattableList {
        return cellValueFormatter.format(cell.cellValue)
    }

    override fun getLeftBorder(cell: Cell): FormattableList {
        val leftBorderChar = cell.leftBorder()
        return FormattableList(nakedFormat(cell).map { leftBorderChar.toString() })
    }

    override fun getRightBorder(cell: Cell): FormattableList {
        val rightBorderChar = cell.rightBorder()
        return FormattableList(nakedFormat(cell).map { rightBorderChar.toString() })
    }

    override fun getTopBorder(cell: Cell): FormattableList {
        val topBorderChar = cell.topBorder()
        val naked = nakedFormat(cell)
        return FormattableList(listOf("".padEnd(naked[0].length, topBorderChar)))
    }

    override fun getBottomBorder(cell: Cell): FormattableList {
        val bottomBorderChar = cell.bottomBorder()
        val naked = nakedFormat(cell)
        return FormattableList(listOf("".padEnd(naked[0].length, bottomBorderChar)))
    }

    override fun getTopLeftEdge(cell: Cell): String = cell.topLeftEdge().toString()

    override fun getTopRightEdge(cell: Cell): String = cell.topRightEdge().toString()

    override fun getBottomLeftEdge(cell: Cell): String = cell.bottomLeftEdge().toString()

    override fun getBottomRightEdge(cell: Cell): String = cell.bottomRightEdge().toString()

}
