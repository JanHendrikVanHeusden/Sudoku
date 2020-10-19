package nl.jhvh.sudoku.format.boxformat.withcandidates

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
