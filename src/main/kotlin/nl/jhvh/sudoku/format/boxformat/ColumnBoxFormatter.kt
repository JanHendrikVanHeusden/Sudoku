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
import nl.jhvh.sudoku.format.element.ColumnFormatting
import nl.jhvh.sudoku.grid.model.segment.Col
import nl.jhvh.sudoku.util.concatEach

/**
 * Formatter to format a [Col] using box drawing characters and numbers. Stateless.
 *  * Box drawing characters: ║ │ ═ ─ ╔ ╗ ╚ ╝ ╤ ╧ ╦ ╩ ╟ ╢ ╠ ╣ ╬ ┼ ╪ ╫ etc.
 *
 * Typically for console output, to observe results of actions (grid construction, Sudoku solving,
 * testing etc.)
 */
class ColumnBoxFormatter(private val cellBoxFormatter: CellBoxFormatter) : ColumnFormatting, GridElementBoxFormatter<Col> {

    override fun format(col: Col): FormattableList {
        val topBorder =
                listOf(getTopLeftEdge(col)) concatEach getTopBorder(col) as List<String> concatEach listOf(getTopRightEdge(col))
        val valueWithLeftRightBorders =
                getLeftBorder(col) as List<String> concatEach nakedFormat(col) as List<String> concatEach getRightBorder(col) as List<String>
        val bottomBorder =
                listOf(getBottomLeftEdge(col)) concatEach getBottomBorder(col) as List<String> concatEach listOf(getBottomRightEdge(col))
        return FormattableList(topBorder + valueWithLeftRightBorders + bottomBorder)
    }

    override fun nakedFormat(element: Col): FormattableList {
        val topCellsWithBottomBorder = element.cells.toList().dropLast(1)
                .map {
                    cellBoxFormatter.nakedFormat(it) + cellBoxFormatter.getBottomBorder(it)
        }.flatten()
        val bottomCellNaked = cellBoxFormatter.nakedFormat(element.cells.last())
        return FormattableList(topCellsWithBottomBorder + bottomCellNaked)
    }

    override fun getLeftBorder(element: Col): FormattableList {
        val topCellsLeftBordersWithBottomEdge = element.cells.toList().dropLast(1)
                .map { cellBoxFormatter.getLeftBorder(it) + listOf(cellBoxFormatter.getBottomLeftEdge(it)) }.flatten()
        val leftBorderOfBottomCell = cellBoxFormatter.getLeftBorder(element.cells.last())
        return FormattableList(topCellsLeftBordersWithBottomEdge + leftBorderOfBottomCell)
    }

    override fun getRightBorder(element: Col): FormattableList {
        val topCellsRightBordersWithBottomEdge = element.cells.toList().dropLast(1)
                .map { cellBoxFormatter.getRightBorder(it) as List<String> + listOf(cellBoxFormatter.getBottomRightEdge(it)) }.flatten()
        val rightBorderOfBottomCell = cellBoxFormatter.getRightBorder(element.cells.last()) as List<String>
        return FormattableList(topCellsRightBordersWithBottomEdge + rightBorderOfBottomCell)
    }

    override fun getTopBorder(element: Col): FormattableList {
        return cellBoxFormatter.getTopBorder(element.cells.first())
    }

    override fun getBottomBorder(element: Col): FormattableList {
        return cellBoxFormatter.getBottomBorder(element.cells.last())
    }

    override fun getTopLeftEdge(element: Col): String = cellBoxFormatter.getTopLeftEdge(element.cells.first())

    override fun getTopRightEdge(element: Col): String = cellBoxFormatter.getTopRightEdge(element.cells.first())

    override fun getBottomLeftEdge(element: Col): String = cellBoxFormatter.getBottomLeftEdge(element.cells.last())

    override fun getBottomRightEdge(element: Col): String = cellBoxFormatter.getBottomRightEdge(element.cells.last())

}
