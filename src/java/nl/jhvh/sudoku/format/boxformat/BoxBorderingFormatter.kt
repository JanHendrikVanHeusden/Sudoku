package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.grid.model.cell.Cell

/**
 * Formatter to format a grid or grid element using box drawing characters and numbers
 *  * Box drawing characters: ║ │ ═ ─ ╔ ╗ ╚ ╝ ╤ ╧ ╦ ╩ ╟ ╢ ╠ ╣ ╬ ┼ ╪ ╫ etc/
 *
 * Typically for console output, to observe results of actions (grid construction, Sudoku solving,
 * testing etc.)
 */
interface BoxBorderingFormatter<in G> {

    /**
     * Formats the content without outside borders.
     * Borders between [Cell]s (insofar applicable) are to be included.
     * @param element: G
     * @return FormattableList
     */
    fun nakedFormat(element: G): FormattableList

    /**
     * Formats the left border of the element, without the top and bottom edges
     * Borders between [Cell]s (insofar applicable) are to be included.
     * @param element: G
     * @return FormattableList
     */
    fun getLeftBorder(element: G): FormattableList

    /**
     * Formats the right border of the element, without the top and bottom edges
     * Borders between [Cell]s (insofar applicable) are to be included.
     * @param element: G
     * @return FormattableList
     */
    fun getRightBorder(element: G): FormattableList

    /**
     * Formats the top border of the element, without the left and right edges
     * Borders between [Cell]s (insofar applicable) are to be included.
     * @param element: G
     * @return FormattableList
     */
    fun getTopBorder(element: G): FormattableList

    /**
     * Formats the bottom border of the element, without the left and right edges
     * Borders between [Cell]s (insofar applicable) are to be included.
     * @param element: G
     * @return FormattableList
     */
    fun getBottomBorder(element: G): FormattableList

    /** @return The top left edge of the element */
    fun getTopLeftEdge(element: G): String

    /** @return The top right edge of the element */
    fun getTopRightEdge(element: G): String

    /** @return The bottom left edge of the element */
    fun getBottomLeftEdge(element: G): String

    /** @return The bottom right edge of the element */
    fun getBottomRightEdge(element: G): String
}
