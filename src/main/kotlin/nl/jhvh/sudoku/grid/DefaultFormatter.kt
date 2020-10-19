package nl.jhvh.sudoku.grid

/* Copyright 2020 Jan-Hendrik van Heusden

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

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
