package nl.jhvh.sudoku.format.element

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.grid.model.segment.Col

interface ColumnFormatter {

    /** @return A formatted, human readable or machine readable (e.g. HTML) [String] representation of a [Col] */
    fun format(col: Col): FormattableList

}
