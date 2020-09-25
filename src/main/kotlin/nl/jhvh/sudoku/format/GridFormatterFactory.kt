package nl.jhvh.sudoku.format

import nl.jhvh.sudoku.format.boxformat.BlockBoxFormatter
import nl.jhvh.sudoku.format.boxformat.CellBoxFormatter
import nl.jhvh.sudoku.format.boxformat.ColumnBoxFormatter
import nl.jhvh.sudoku.format.boxformat.GridBoxFormatter
import nl.jhvh.sudoku.format.boxformat.RowBoxFormatter
import nl.jhvh.sudoku.format.simple.SimpleCellValueFormatter

interface GridFormatterFactory {
    val cellValueFormatterInstance: SimpleCellValueFormatter
    val cellBoxFormatterInstance: CellBoxFormatter
    val rowBoxFormatterInstance: RowBoxFormatter
    val columnBoxFormatterInstance: ColumnBoxFormatter
    val blockBoxFormatterInstance: BlockBoxFormatter
    val gridBoxFormatterInstance: GridBoxFormatter

}