package nl.jhvh.sudoku.format.element

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.grid.model.cell.Cell

/** [Cell] formatter */
interface CellFormatting {

    /** @return A formatted, human readable or machine readable (e.g. HTML) [String] representation of a [Cell] */
    fun format(cell: Cell): FormattableList

}
