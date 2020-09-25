package nl.jhvh.sudoku.format.boxformat.withcandidates

import nl.jhvh.sudoku.format.GridFormatterFactory
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
 * Typically for console output, to inspect results of actions (grid construction, Sudoku solving,
 * testing etc.)
 */
class SudokuWithCandidatesBoxFormatter(val formatterFactory: GridFormatterFactory = BoxFormatterWithCandidatesFactory.factoryInstance) :
        SudokuFormatter,
        CellValueFormatter by formatterFactory.cellValueFormatterInstance,
        CellFormatter by formatterFactory.cellBoxFormatterInstance,
        ColumnFormatter by formatterFactory.columnBoxFormatterInstance,
        RowFormatter by formatterFactory.rowBoxFormatterInstance,
        BlockFormatter by formatterFactory.blockBoxFormatterInstance,
        GridFormatter by formatterFactory.gridBoxFormatterInstance
