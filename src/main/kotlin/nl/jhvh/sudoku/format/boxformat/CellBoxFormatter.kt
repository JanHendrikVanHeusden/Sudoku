package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.bottomBorderIsBlockBorder
import nl.jhvh.sudoku.format.bottomBorderIsGridBorder
import nl.jhvh.sudoku.format.element.CellFormatting
import nl.jhvh.sudoku.format.element.CellValueFormatting
import nl.jhvh.sudoku.format.leftBorderIsBlockBorder
import nl.jhvh.sudoku.format.leftBorderIsGridBorder
import nl.jhvh.sudoku.format.rightBorderIsBlockBorder
import nl.jhvh.sudoku.format.rightBorderIsGridBorder
import nl.jhvh.sudoku.format.topBorderIsBlockBorder
import nl.jhvh.sudoku.format.topBorderIsGridBorder
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.util.concatEach

/**
 * Simple formatting of a [Cell], typically for output to [System.out]. Stateless.
 *  * The height of the cell value of a formatted [Cell] is always 1, the height of the top and bottom borders is 1 each.
 *  * The width of a cell value varies with the width of the grid's highest possible cell value; for `3*3` block size the
 *    highest value 1 9, for `4*4` 16 etc.
 * Borders:
 *  * The height of a top or bottom border is always 1. The width varies by the width of the formatted value.
 *  * The width and height of left and right borders are always 1.
 *  * Box drawing characters: ║ │ ═ ─ ╔ ╗ ╚ ╝ ╤ ╧ ╦ ╩ ╟ ╢ ╠ ╣ ╬ ┼ ╪ ╫ etc.
 *
 * Typically for console output, to observe results of actions (grid construction, Sudoku solving,
 * testing etc.)
 */
class CellBoxFormatter(private val cellValueFormatter: CellValueFormatting) : CellFormatting, GridElementBoxFormatter<Cell> {

    override fun format(cell: Cell): FormattableList {
        val topBorder =
                listOf(getTopLeftEdge(cell)) concatEach getTopBorder(cell) concatEach listOf(getTopRightEdge(cell))
        val valueWithLeftRightBorders =
                getLeftBorder(cell) as List<String> concatEach cellValueFormatter.format(cell.cellValue) concatEach getRightBorder(cell) as List<String>
        val bottomBorder =
                listOf(getBottomLeftEdge(cell)) concatEach getBottomBorder(cell) concatEach listOf(getBottomRightEdge(cell))
        return FormattableList(topBorder + valueWithLeftRightBorders + bottomBorder)
    }

    override fun nakedFormat(element: Cell): FormattableList {
        return cellValueFormatter.format(element.cellValue)
    }

    override fun getLeftBorder(element: Cell): FormattableList {
        val leftBorderChar = element.leftBorder()
        return FormattableList(nakedFormat(element).map { leftBorderChar.toString() })
    }

    override fun getRightBorder(element: Cell): FormattableList {
        val rightBorderChar = element.rightBorder()
        return FormattableList(nakedFormat(element).map { rightBorderChar.toString() })
    }

    override fun getTopBorder(element: Cell): FormattableList {
        val topBorderChar = element.topBorder()
        val naked = nakedFormat(element)
        return FormattableList(listOf("".padEnd(naked[0].length, topBorderChar)))
    }

    override fun getBottomBorder(element: Cell): FormattableList {
        val bottomBorderChar = element.bottomBorder()
        val naked = nakedFormat(element)
        return FormattableList(listOf("".padEnd(naked[0].length, bottomBorderChar)))
    }

    override fun getTopLeftEdge(element: Cell): String = element.topLeftEdge().toString()

    override fun getTopRightEdge(element: Cell): String = element.topRightEdge().toString()

    override fun getBottomLeftEdge(element: Cell): String = element.bottomLeftEdge().toString()

    override fun getBottomRightEdge(element: Cell): String = element.bottomRightEdge().toString()

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
