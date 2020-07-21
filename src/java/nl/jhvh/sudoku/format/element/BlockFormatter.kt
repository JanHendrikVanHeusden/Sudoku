package nl.jhvh.sudoku.format.element

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.grid.model.segment.Block

interface BlockFormatter {

    /** @return A formatted, human readable or machine readable (e.g. HTML) [String] representation of a [Block] */
    fun format(block: Block): FormattableList

}
