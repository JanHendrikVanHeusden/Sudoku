package nl.jhvh.sudoku.grid

import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.format.boxformat.SudokuBoxFormatter
import nl.jhvh.sudoku.format.boxformat.withcandidates.SudokuWithCandidatesBoxFormatter

/** default [SudokuFormatter] instance that can be used in [toString] methods */
val defaultGridFormatter: SudokuFormatter = SudokuBoxFormatter()

/**
 * [SudokuFormatter] instance that can be used in cases where candidates should be displayed,
 * e.g. during grid solving or to display a non-solvable grid
 */
val gridWithCandidatesBoxFormatter: SudokuFormatter = SudokuWithCandidatesBoxFormatter()
