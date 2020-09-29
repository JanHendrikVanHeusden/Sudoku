package nl.jhvh.sudoku.format.boxformat.withcandidates

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
class SudokuWithCandidatesBoxFormatter(val formatterFactory: SudokuFormatterFactory = BoxFormatterWithCandidatesFactory.factoryInstance) :
        SudokuFormatter,
        CellValueFormatting by formatterFactory.cellValueFormatterInstance,
        CellFormatting by formatterFactory.cellFormatterInstance,
        ColumnFormatting by formatterFactory.columnFormatterInstance,
        RowFormatting by formatterFactory.rowFormatterInstance,
        BlockFormatting by formatterFactory.blockFormatterInstance,
        GridFormatting by formatterFactory.gridFormatterInstance
