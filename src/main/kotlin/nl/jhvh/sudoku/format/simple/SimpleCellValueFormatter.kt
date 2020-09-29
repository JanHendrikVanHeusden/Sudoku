package nl.jhvh.sudoku.format.simple

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.element.CellValueFormatting
import nl.jhvh.sudoku.grid.model.cell.CellValue
import nl.jhvh.sudoku.grid.model.cell.VALUE_UNKNOWN
import org.apache.commons.lang3.StringUtils
import java.lang.Integer.max

const val fixedValueMarker: Char = '►'

/**
 * Basic stateless [CellValue] formatter for textual output, typically to the console.
 * To be used in other (typically text based) formatters.
 */
open class SimpleCellValueFormatter: CellValueFormatting {

    val minimalFormattedLength:Int = 3

    /**
     * A basic text format representation of the [cellValue].
     * The resulting [String] may include one or more of the following characters: `space`, [fixedValueMarker] and digit.
     * @param cellValue CellValue
     * @return The formatted output as a [FormattableList], with a single [String]
     */
    override fun format(cellValue: CellValue): FormattableList {
        // +1 to allow for the fixedValueMarker
        val minimalLength = max(cellValue.grid.maxValueLength+1, minimalFormattedLength)
        val valueStr = if (cellValue.value == VALUE_UNKNOWN) " " else cellValue.value.toString()
        val valueWithFixedMarkerIfNeeded = if (cellValue.isFixed) "$fixedValueMarker$valueStr" else " $valueStr"
        val formatted = StringUtils.center(valueWithFixedMarkerIfNeeded, minimalLength)
        return FormattableList(listOf(formatted))
    }
}
