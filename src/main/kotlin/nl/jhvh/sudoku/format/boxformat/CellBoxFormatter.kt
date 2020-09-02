package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.bottomBorderIsBlockBorder
import nl.jhvh.sudoku.format.bottomBorderIsGridBorder
import nl.jhvh.sudoku.format.concatEach
import nl.jhvh.sudoku.format.element.CellFormatter
import nl.jhvh.sudoku.format.leftBorderIsBlockBorder
import nl.jhvh.sudoku.format.leftBorderIsGridBorder
import nl.jhvh.sudoku.format.rightBorderIsBlockBorder
import nl.jhvh.sudoku.format.rightBorderIsGridBorder
import nl.jhvh.sudoku.format.simple.SimpleCellValueFormatter
import nl.jhvh.sudoku.format.topBorderIsBlockBorder
import nl.jhvh.sudoku.format.topBorderIsGridBorder
import nl.jhvh.sudoku.grid.model.cell.Cell

/**
 * Simple formatting of a [Cell], typically for output to [System.out]. Stateless.
 *  * The height of the cell value of a formatted [Cell] is always 1, the height of the top and bottom borders is 1 each.
 *  * The width of a cell value varies with the width of the grid's highest possible cell value; for `3*3` block size the
 *    highest value 1 9, for `4*4` 16 etc.
 * Borders:
 *  * The height of a top or bottom border is always 1. The width varies by the width of the formatted value.
 *  * The width and height of left and richt borders are always 1.
 *  * Box drawing characters: ║ │ ═ ─ ╔ ╗ ╚ ╝ ╤ ╧ ╦ ╩ ╟ ╢ ╠ ╣ ╬ ┼ ╪ ╫ etc.
 *
 * Typically for console output, to observe results of actions (grid construction, Sudoku solving,
 * testing etc.)
 */
@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") // parameter `cell: Cell` (instead of `element: cell`)
class CellBoxFormatter(private val cellValueFormatter: SimpleCellValueFormatter) : CellFormatter, ElementBoxFormattable<Cell> {

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

    companion object BorderChar {

        fun Cell.leftBorder(): Char {
            return when {
                (this.leftBorderIsGridBorder() || this.leftBorderIsBlockBorder()) -> `boxChar ║`
                else -> `boxChar │`
            }
        }

        fun Cell.rightBorder(): Char {
            return when {
                (this.rightBorderIsGridBorder() || this.rightBorderIsBlockBorder()) -> `boxChar ║`
                else -> `boxChar │`
            }
        }

        fun Cell.topBorder(): Char {
            return when {
                (this.topBorderIsGridBorder() || this.topBorderIsBlockBorder()) -> `boxChar ═`
                else -> `boxChar ─`
            }
        }

        fun Cell.bottomBorder(): Char {
            return when {
                (this.bottomBorderIsGridBorder() || this.bottomBorderIsBlockBorder()) -> `boxChar ═`
                else -> `boxChar ─`
            }
        }

        fun Cell.topLeftEdge(): Char {
            return when {
                (this.topBorderIsGridBorder() && this.leftBorderIsGridBorder()) -> `boxChar ╔`
                (this.topBorderIsGridBorder() && this.leftBorderIsBlockBorder()) -> `boxChar ╦`
                (this.topBorderIsGridBorder()) -> `boxChar ╤`

                (this.leftBorderIsGridBorder() && this.topBorderIsBlockBorder()) -> `boxChar ╠`
                (this.leftBorderIsGridBorder() && !this.topBorderIsBlockBorder()) -> `boxChar ╟`

                (this.topBorderIsBlockBorder() && this.leftBorderIsBlockBorder()) -> `boxChar ╬`
                (this.topBorderIsBlockBorder()) -> `boxChar ╪`
                (this.leftBorderIsBlockBorder()) -> `boxChar ╫`
                else -> `boxChar ┼`
            }
        }

        fun Cell.topRightEdge(): Char {
            return when {
                (this.topBorderIsGridBorder() && this.rightBorderIsGridBorder()) -> `boxChar ╗`
                (this.topBorderIsGridBorder() && this.rightBorderIsBlockBorder()) -> `boxChar ╦`
                (this.topBorderIsGridBorder()) -> `boxChar ╤`

                (this.rightBorderIsGridBorder() && this.topBorderIsBlockBorder()) -> `boxChar ╣`
                (this.rightBorderIsGridBorder()) -> `boxChar ╢`

                (this.topBorderIsBlockBorder() && this.rightBorderIsBlockBorder()) -> `boxChar ╬`
                (this.topBorderIsBlockBorder()) -> `boxChar ╪`
                (this.rightBorderIsBlockBorder()) -> `boxChar ╫`
                else -> `boxChar ┼`
            }
        }

        fun Cell.bottomLeftEdge(): Char {
            return when {
                (this.bottomBorderIsGridBorder() && this.leftBorderIsGridBorder()) -> `boxChar ╚`
                (this.bottomBorderIsGridBorder() && this.leftBorderIsBlockBorder()) -> `boxChar ╩`
                (this.bottomBorderIsGridBorder()) -> `boxChar ╧`

                (this.leftBorderIsGridBorder() && this.bottomBorderIsBlockBorder()) -> `boxChar ╠`
                (this.leftBorderIsGridBorder()) -> `boxChar ╟`

                (this.bottomBorderIsBlockBorder() && this.leftBorderIsBlockBorder()) -> `boxChar ╬`
                (this.bottomBorderIsBlockBorder()) -> `boxChar ╪`
                (this.leftBorderIsBlockBorder()) -> `boxChar ╫`
                else -> `boxChar ┼`
            }
        }

        fun Cell.bottomRightEdge(): Char {
            return when {
                (this.bottomBorderIsGridBorder() && this.rightBorderIsGridBorder()) -> `boxChar ╝`
                (this.bottomBorderIsGridBorder() && this.rightBorderIsBlockBorder()) -> `boxChar ╩`
                (this.bottomBorderIsGridBorder()) -> `boxChar ╧`

                (this.rightBorderIsGridBorder() && this.bottomBorderIsBlockBorder()) -> `boxChar ╣`
                (this.rightBorderIsGridBorder()) -> `boxChar ╢`

                (this.bottomBorderIsBlockBorder() && this.rightBorderIsBlockBorder()) -> `boxChar ╬`
                (this.bottomBorderIsBlockBorder()) -> `boxChar ╪`
                (this.rightBorderIsBlockBorder()) -> `boxChar ╫`
                else -> `boxChar ┼`
            }
        }

    }
}
