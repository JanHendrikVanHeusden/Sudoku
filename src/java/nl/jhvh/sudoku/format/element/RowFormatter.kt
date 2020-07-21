package nl.jhvh.sudoku.format.element

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.grid.model.segment.Row

interface RowFormatter {

    /** @return A formatted, human readable [String] representation of a [Row] */
    fun format(row: Row): FormattableList
}
