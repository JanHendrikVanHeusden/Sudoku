package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.format.element.BlockFormatter
import nl.jhvh.sudoku.format.element.CellFormatter
import nl.jhvh.sudoku.format.element.CellValueFormatter
import nl.jhvh.sudoku.format.element.ColumnFormatter
import nl.jhvh.sudoku.format.element.GridFormatter
import nl.jhvh.sudoku.format.element.RowFormatter

/**
 * Formatter to format a grid or grid element using box drawing characters and numbers
 *  * Box drawing characters: ║ │ ═ ─ ╔ ╗ ╚ ╝ ╤ ╧ ╦ ╩ ╟ ╢ ╠ ╣ ╬ ┼ ╪ ╫ etc.
 *
 * Typically for console output, to observe results of actions (grid construction, Sudoku solving,
 * testing etc.)
 */
class SudokuBoxFormatter(val formatterFactory: BoxFormatterFactory = BoxFormatterFactory.instance) :
        SudokuFormatter,
        CellValueFormatter by formatterFactory.simpleCellValueFormatterInstance,
        CellFormatter by formatterFactory.cellBoxFormatterInstance,
        ColumnFormatter by formatterFactory.columnBoxFormatterInstance,
        RowFormatter by formatterFactory.rowBoxFormatterInstance,
        BlockFormatter by formatterFactory.blockBoxFormatterInstance,
        GridFormatter by formatterFactory.gridBoxFormatterInstance
