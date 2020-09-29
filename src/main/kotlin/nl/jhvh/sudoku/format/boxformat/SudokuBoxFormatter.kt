package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.format.SudokuFormatterFactory
import nl.jhvh.sudoku.format.element.BlockFormatting
import nl.jhvh.sudoku.format.element.CellFormatting
import nl.jhvh.sudoku.format.element.CellValueFormatting
import nl.jhvh.sudoku.format.element.ColumnFormatting
import nl.jhvh.sudoku.format.element.GridFormatting
import nl.jhvh.sudoku.format.element.RowFormatting

/**
 * Formatter to format a grid or grid element using box drawing characters and numbers
 *  * Box drawing characters: ║ │ ═ ─ ╔ ╗ ╚ ╝ ╤ ╧ ╦ ╩ ╟ ╢ ╠ ╣ ╬ ┼ ╪ ╫ etc.
 *
 * Typically for console output, to inspect results of actions (grid construction, Sudoku solving,
 * testing etc.)
 */
class SudokuBoxFormatter(val boxFormatterFactory: SudokuFormatterFactory = BoxFormatterFactory.factoryInstance) :
        SudokuFormatter,
        CellValueFormatting by boxFormatterFactory.cellValueFormatterInstance,
        CellFormatting by boxFormatterFactory.cellFormatterInstance,
        ColumnFormatting by boxFormatterFactory.columnFormatterInstance,
        RowFormatting by boxFormatterFactory.rowFormatterInstance,
        BlockFormatting by boxFormatterFactory.blockFormatterInstance,
        GridFormatting by boxFormatterFactory.gridFormatterInstance
