package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.grid.model.cell.Cell

interface BoxBorderingFormatter<in G> {

    /**
     * [nakedFormat] formats the content without outside borders.
     * Borders between [Cell]s (insofar applicable) are to be included.
     * @param element: G
     * @return FormattableList
     */
    fun nakedFormat(element: G): FormattableList

    fun getLeftBorder(element: G): FormattableList

    fun getRightBorder(element: G): FormattableList

    fun getTopBorder(element: G): FormattableList

    fun getBottomBorder(element: G): FormattableList

    fun getTopLeftEdge(element: G): String

    fun getTopRightEdge(element: G): String

    fun getBottomLeftEdge(element: G): String

    fun getBottomRightEdge(element: G): String
}
