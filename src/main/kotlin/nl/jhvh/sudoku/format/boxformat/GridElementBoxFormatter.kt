package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.element.SudokuFormatting
import nl.jhvh.sudoku.grid.model.GridStructural
import nl.jhvh.sudoku.grid.model.cell.Cell

/**
 * Formatter to format a grid or grid element using box drawing characters and numbers
 *  * Box drawing characters: ║ │ ═ ─ ╔ ╗ ╚ ╝ ╤ ╧ ╦ ╩ ╟ ╢ ╠ ╣ ╬ ┼ ╪ ╫ etc.
 *
 * Typically for console output, to observe results of actions (grid construction, Sudoku solving,
 * testing etc.)
 */
interface GridElementBoxFormatter<in E: GridStructural>: SudokuFormatting {

    /**
     * Formats the content without outside borders.
     * Borders between [Cell]s (insofar applicable) are to be included.
     * @param element: E
     * @return FormattableList
     */
    fun nakedFormat(element: E): FormattableList

    /**
     * Formats the left border of the element, without the top and bottom edges
     * @param element: E
     * @return FormattableList
     */
    fun getLeftBorder(element: E): FormattableList

    /**
     * Formats the right border of the element, without the top and bottom edges
     * @param element: E
     * @return FormattableList
     */
    fun getRightBorder(element: E): FormattableList

    /**
     * Formats the top border of the element, without the left and right edges
     * @param element: E
     * @return FormattableList
     */
    fun getTopBorder(element: E): FormattableList

    /**
     * Formats the bottom border of the element, without the left and right edges
     * @param element: E
     * @return FormattableList
     */
    fun getBottomBorder(element: E): FormattableList

    /** @return The top left edge of the element */
    fun getTopLeftEdge(element: E): String

    /** @return The top right edge of the element */
    fun getTopRightEdge(element: E): String

    /** @return The bottom left edge of the element */
    fun getBottomLeftEdge(element: E): String

    /** @return The bottom right edge of the element */
    fun getBottomRightEdge(element: E): String
}
