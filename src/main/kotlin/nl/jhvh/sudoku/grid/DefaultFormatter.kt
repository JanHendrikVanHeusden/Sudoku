package nl.jhvh.sudoku.grid

import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.format.boxformat.SudokuBoxFormatter

/** [SudokuFormatter] instance that can be used in [toString] methods */
val defaultGridToStringFormatter: SudokuFormatter = SudokuBoxFormatter()
