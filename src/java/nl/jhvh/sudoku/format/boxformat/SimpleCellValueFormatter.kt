package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.element.CellValueFormatter
import nl.jhvh.sudoku.grid.model.cell.CellValue
import nl.jhvh.sudoku.grid.model.cell.VALUE_UNKNOWN
import org.apache.commons.lang3.StringUtils
import java.lang.Integer.max

const val fixedValueMarker: Char = '►'

class SimpleCellValueFormatter: CellValueFormatter {

    val minimalFormattedLength:Int = 3

    override fun format(cellValue: CellValue): FormattableList {
        // +1 to allow for the fixedValueMarker
        val minimalLength = max(cellValue.grid.maxValueLength+1, minimalFormattedLength)
        val valueStr = if (cellValue.value == VALUE_UNKNOWN) " " else cellValue.value.toString()
        val valueWithFixedMarkerIfNeeded = if (cellValue.isFixed) "$fixedValueMarker$valueStr" else " $valueStr"
        val formatted = StringUtils.center(valueWithFixedMarkerIfNeeded, minimalLength)
        return FormattableList(listOf(formatted))
    }
}
