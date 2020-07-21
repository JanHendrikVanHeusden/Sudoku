package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.format.element.BlockFormatter
import nl.jhvh.sudoku.format.element.CellFormatter
import nl.jhvh.sudoku.format.element.CellValueFormatter
import nl.jhvh.sudoku.format.element.ColumnFormatter
import nl.jhvh.sudoku.format.element.GridFormatter
import nl.jhvh.sudoku.format.element.RowFormatter

class SudokuBoxFormatter : SudokuFormatter,
        CellValueFormatter by SimpleCellValueFormatter(),
        CellFormatter by CellBoxFormatter(),
        ColumnFormatter by ColumnBoxFormatter(),
        RowFormatter by RowBoxFormatter(),
        BlockFormatter by BlockBoxFormatter(),
        GridFormatter by GridBoxFormatter()
