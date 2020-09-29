package nl.jhvh.sudoku.format

import nl.jhvh.sudoku.format.element.BlockFormatting
import nl.jhvh.sudoku.format.element.CellFormatting
import nl.jhvh.sudoku.format.element.CellValueFormatting
import nl.jhvh.sudoku.format.element.ColumnFormatting
import nl.jhvh.sudoku.format.element.GridFormatting
import nl.jhvh.sudoku.format.element.RowFormatting

/**
 * Interface to support formatting of Sudoku elements using the [Visitor Pattern](https://en.wikipedia.org/wiki/Visitor_pattern#Java_example).
 *  * The [format] methods return a formatted, human readable or machine readable (e.g. HTML) [String] representation of the element input.
 *  * This interface represents the visitor in the classic Visitor Pattern.
 *     * It lists the 'accepting' methods for the supported Sudoku element types.
 */
interface SudokuFormatter:
        CellValueFormatting,
        CellFormatting,
        ColumnFormatting,
        RowFormatting,
        BlockFormatting,
        GridFormatting
