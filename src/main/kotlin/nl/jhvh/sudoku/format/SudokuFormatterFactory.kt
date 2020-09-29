package nl.jhvh.sudoku.format

import nl.jhvh.sudoku.format.element.BlockFormatting
import nl.jhvh.sudoku.format.element.CellFormatting
import nl.jhvh.sudoku.format.element.CellValueFormatting
import nl.jhvh.sudoku.format.element.ColumnFormatting
import nl.jhvh.sudoku.format.element.GridFormatting
import nl.jhvh.sudoku.format.element.RowFormatting

interface SudokuFormatterFactory {
    val cellValueFormatterInstance: CellValueFormatting
    val cellFormatterInstance: CellFormatting
    val rowFormatterInstance: RowFormatting
    val columnFormatterInstance: ColumnFormatting
    val blockFormatterInstance: BlockFormatting
    val gridFormatterInstance: GridFormatting
}