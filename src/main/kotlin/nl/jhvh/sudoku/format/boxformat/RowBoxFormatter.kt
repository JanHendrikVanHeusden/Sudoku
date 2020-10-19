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
import nl.jhvh.sudoku.format.element.RowFormatting
import nl.jhvh.sudoku.grid.model.segment.Row
import nl.jhvh.sudoku.util.concatEach

/**
 * Formatter to format a [Row] using box drawing characters and numbers. Stateless.
 *  * Box drawing characters: ║ │ ═ ─ ╔ ╗ ╚ ╝ ╤ ╧ ╦ ╩ ╟ ╢ ╠ ╣ ╬ ┼ ╪ ╫ etc.
 *
 * Typically for console output, to observe results of actions (grid construction, Sudoku solving,
 * testing etc.)
 */
class RowBoxFormatter(private val cellBoxFormatter: CellBoxFormatter) : RowFormatting, GridElementBoxFormatter<Row> {

    override fun format(row: Row): FormattableList {
        val topBorder =
                listOf(getTopLeftEdge(row)) concatEach getTopBorder(row) concatEach listOf(getTopRightEdge(row))
        val valueWithLeftRightBorders =
                getLeftBorder(row) concatEach nakedFormat(row) concatEach getRightBorder(row)
        val bottomBorder =
                listOf(getBottomLeftEdge(row)) concatEach getBottomBorder(row) concatEach listOf(getBottomRightEdge(row))
        return FormattableList(topBorder + valueWithLeftRightBorders + bottomBorder)
    }

    override fun nakedFormat(element: Row): FormattableList {
        val leftCellsWithRightBorder = concatEach(*element.cells.toList().dropLast(1).map {
            cellBoxFormatter.nakedFormat(it) concatEach cellBoxFormatter.getRightBorder(it)
        }.toTypedArray())
        val rightCellNaked = cellBoxFormatter.nakedFormat(element.cells.last())
        return FormattableList(leftCellsWithRightBorder concatEach rightCellNaked)
    }

    override fun getLeftBorder(element: Row): FormattableList {
        return cellBoxFormatter.getLeftBorder(element.cells.first())
    }

    override fun getRightBorder(element: Row): FormattableList {
        return cellBoxFormatter.getRightBorder(element.cells.last())
    }

    override fun getTopBorder(element: Row): FormattableList {
        val leftCellsTopBordersWithRightEdge = concatEach(*element.cells.toList().dropLast(1).map {
            cell -> cellBoxFormatter.getTopBorder(cell).map { border -> border + cellBoxFormatter.getTopRightEdge(cell)}
        }.toTypedArray())
        val rightCellTopBorder = cellBoxFormatter.getTopBorder(element.cells.last()) as List<String>
        return FormattableList(leftCellsTopBordersWithRightEdge concatEach rightCellTopBorder)
    }

    override fun getBottomBorder(element: Row): FormattableList {
        val leftCellsBottomBordersWithRightEdge = concatEach(*element.cells.toList().dropLast(1).map {
            cell -> cellBoxFormatter.getBottomBorder(cell).map { border -> border + cellBoxFormatter.getBottomRightEdge(cell)}
        }.toTypedArray())
        val rightCellBottomBorder = cellBoxFormatter.getBottomBorder(element.cells.last()) as List<String>
        return FormattableList(leftCellsBottomBordersWithRightEdge concatEach rightCellBottomBorder)
    }

    override fun getTopLeftEdge(element: Row): String = cellBoxFormatter.getTopLeftEdge(element.cells.first())

    override fun getTopRightEdge(element: Row): String = cellBoxFormatter.getTopRightEdge(element.cells.last())

    override fun getBottomLeftEdge(element: Row): String = cellBoxFormatter.getBottomLeftEdge(element.cells.first())

    override fun getBottomRightEdge(element: Row): String = cellBoxFormatter.getBottomRightEdge(element.cells.last())

}
