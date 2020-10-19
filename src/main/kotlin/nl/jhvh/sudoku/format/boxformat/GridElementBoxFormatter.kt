package nl.jhvh.sudoku.format.boxformat

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

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.element.SudokuFormatting
import nl.jhvh.sudoku.grid.model.GridStructural
import nl.jhvh.sudoku.grid.model.cell.Cell

/**
 * Formatter to format a grid or grid element using box drawing characters and numbers
 *  * Box drawing characters: ║ │ ═ ─ ╔ ╗ ╚ ╝ ╤ ╧ ╦ ╩ ╟ ╢ ╠ ╣ ╬ ┼ ╪ ╫ etc.
 *
 * Typically for console output, to observe results of actions (grid construction, Sudoku solving,
 * testing etc.)
 */
interface GridElementBoxFormatter<in E: GridStructural>: SudokuFormatting {

    /**
     * Formats the content without outside borders.
     * Borders between [Cell]s (insofar applicable) are to be included.
     * @param element: E
     * @return FormattableList
     */
    fun nakedFormat(element: E): FormattableList

    /**
     * Formats the left border of the element, without the top and bottom edges
     * @param element: E
     * @return FormattableList
     */
    fun getLeftBorder(element: E): FormattableList

    /**
     * Formats the right border of the element, without the top and bottom edges
     * @param element: E
     * @return FormattableList
     */
    fun getRightBorder(element: E): FormattableList

    /**
     * Formats the top border of the element, without the left and right edges
     * @param element: E
     * @return FormattableList
     */
    fun getTopBorder(element: E): FormattableList

    /**
     * Formats the bottom border of the element, without the left and right edges
     * @param element: E
     * @return FormattableList
     */
    fun getBottomBorder(element: E): FormattableList

    /** @return The top left edge of the element */
    fun getTopLeftEdge(element: E): String

    /** @return The top right edge of the element */
    fun getTopRightEdge(element: E): String

    /** @return The bottom left edge of the element */
    fun getBottomLeftEdge(element: E): String

    /** @return The bottom right edge of the element */
    fun getBottomRightEdge(element: E): String
}
