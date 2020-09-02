package nl.jhvh.sudoku.format.element

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.grid.model.Grid

/** Stateless [Grid] formatter */
interface GridFormatter {

    /** @return A formatted, human readable or machine readable (e.g. HTML) [String] representation of a [Grid] */
    fun format(grid: Grid): FormattableList

}
