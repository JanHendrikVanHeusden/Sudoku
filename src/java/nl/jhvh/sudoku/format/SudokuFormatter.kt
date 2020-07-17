package nl.jhvh.sudoku.format

import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.cell.CellValue
import nl.jhvh.sudoku.grid.model.segment.Block
import nl.jhvh.sudoku.grid.model.segment.Col
import nl.jhvh.sudoku.grid.model.segment.Row

/**
 * Interface to support formatting of Sudoku elements using the [Visitor Pattern](https://en.wikipedia.org/wiki/Visitor_pattern#Java_example).
 *  * The [format] methods return a formatted, human readable or machine readable (e.g. HTML) [String] representation of the element input.
 *  * This interface represents the visitor in the classic Visitor Pattern.
 *     * It lists the 'accepting' methods for the supported Sudoku element types.
 */
interface SudokuFormatter {

    /** @return A formatted, human readable or machine readable (e.g. HTML) [String] representation of a [CellValue] */
    fun format(cellValue: CellValue): List<String>

    /** @return A formatted, human readable or machine readable (e.g. HTML) [String] representation of a [Cell] */
    fun format(cell: Cell): List<String>

    /** @return A formatted, human readable or machine readable (e.g. HTML) [String] representation of a [Col] */
    fun format(col: Col): List<String>

    /** @return A formatted, human readable [String] representation of a [Row] */
    fun format(row: Row): List<String>

    /** @return A formatted, human readable or machine readable (e.g. HTML) [String] representation of a [Block] */
    fun format(block: Block): List<String>

    /** @return A formatted, human readable or machine readable (e.g. HTML) [String] representation of a [Grid] */
    fun format(grid: Grid): List<String>

}

