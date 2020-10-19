package nl.jhvh.sudoku.format

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

import nl.jhvh.sudoku.format.element.BlockFormatting
import nl.jhvh.sudoku.format.element.CellFormatting
import nl.jhvh.sudoku.format.element.CellValueFormatting
import nl.jhvh.sudoku.format.element.ColumnFormatting
import nl.jhvh.sudoku.format.element.GridFormatting
import nl.jhvh.sudoku.format.element.RowFormatting

/**
 * Interface to support formatting of Sudoku elements using the [Visitor Pattern](https://en.wikipedia.org/wiki/Visitor_pattern#Java_example).
 *  * The [format] methods return a formatted, human readable or machine readable (e.g. HTML) [String] representation of the element input.
 *  * This interface represents the visitor in the classic Visitor Pattern.
 *     * It lists the 'accepting' methods for the supported Sudoku element types.
 */
interface SudokuFormatter:
        CellValueFormatting,
        CellFormatting,
        ColumnFormatting,
        RowFormatting,
        BlockFormatting,
        GridFormatting
