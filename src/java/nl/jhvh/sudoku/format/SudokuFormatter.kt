package nl.jhvh.sudoku.format

import nl.jhvh.sudoku.format.element.BlockFormatter
import nl.jhvh.sudoku.format.element.CellFormatter
import nl.jhvh.sudoku.format.element.CellValueFormatter
import nl.jhvh.sudoku.format.element.ColumnFormatter
import nl.jhvh.sudoku.format.element.GridFormatter
import nl.jhvh.sudoku.format.element.RowFormatter

/**
 * Interface to support formatting of Sudoku elements using the [Visitor Pattern](https://en.wikipedia.org/wiki/Visitor_pattern#Java_example).
 *  * The [format] methods return a formatted, human readable or machine readable (e.g. HTML) [String] representation of the element input.
 *  * This interface represents the visitor in the classic Visitor Pattern.
 *     * It lists the 'accepting' methods for the supported Sudoku element types.
 */
interface SudokuFormatter: CellValueFormatter, CellFormatter, ColumnFormatter, RowFormatter, BlockFormatter, GridFormatter
