package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.element.CellValueFormatter
import nl.jhvh.sudoku.grid.model.cell.CellValue
import nl.jhvh.sudoku.grid.model.cell.VALUE_UNKNOWN

const val fixedValueMarker: Char = '►'

class SimpleCellValueFormatter: CellValueFormatter {

    val minimalFormattedLength:Int = 3

    override fun format(cellValue: CellValue): FormattableList {
        val valueStr = if (cellValue.value == VALUE_UNKNOWN) " " else cellValue.value.toString()
        val formatted = (if (cellValue.isFixed) "$fixedValueMarker$valueStr" else " $valueStr").padEnd(minimalFormattedLength)
        // +1 to allow for the fixedValueMarker
        return FormattableList(listOf(formatted))
    }
}
