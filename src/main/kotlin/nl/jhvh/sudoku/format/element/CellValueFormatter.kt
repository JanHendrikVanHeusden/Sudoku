package nl.jhvh.sudoku.format.element

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.grid.model.cell.CellValue

/** Stateless [CellValue] formatter */
interface CellValueFormatter {

    /** @return A formatted, human readable or machine readable (e.g. HTML) [String] representation of a [CellValue] */
    fun format(cellValue: CellValue): FormattableList
}
