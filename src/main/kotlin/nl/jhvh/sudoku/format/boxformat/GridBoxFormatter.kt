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
import nl.jhvh.sudoku.format.element.GridFormatting
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.util.concatEach

/**
 * Formatter to format a [Grid] using box drawing characters and numbers. Stateless.
 *  * Box drawing characters: ║ │ ═ ─ ╔ ╗ ╚ ╝ ╤ ╧ ╦ ╩ ╟ ╢ ╠ ╣ ╬ ┼ ╪ ╫ etc
 *
 * Typically for console output, to observe results of actions (grid construction, Sudoku solving,
 * testing etc.)
 */
open class GridBoxFormatter(protected val rowBoxFormatter: RowBoxFormatter, protected val colBoxFormatter: ColumnBoxFormatter
) : GridFormatting, GridElementBoxFormatter<Grid> {

    override fun format(grid: Grid): FormattableList {
        val formattedRows = mutableListOf<String>()
        val bottomBorder: List<String>
        with (rowBoxFormatter) {
            grid.rowList.forEach {
                formattedRows += listOf(getTopLeftEdge(it)) concatEach getTopBorder(it) concatEach listOf(getTopRightEdge(it))
                formattedRows += (getLeftBorder(it) concatEach nakedFormat(it) concatEach getRightBorder(it))
            }
            bottomBorder = listOf(getBottomLeftEdge(grid.rowList.last())) concatEach getBottomBorder(grid.rowList.last()) concatEach listOf(getBottomRightEdge(grid.rowList.last()))
        }
        return FormattableList(formattedRows + bottomBorder)
    }

    override fun nakedFormat(element: Grid): FormattableList {
        val topRowsWithBottomBorder = element.rowList.dropLast(1).map {
            rowBoxFormatter.nakedFormat(it) + rowBoxFormatter.getBottomBorder(it)
        }.flatten()
        val bottomRowNaked = rowBoxFormatter.nakedFormat(element.rowList.last())
        return FormattableList(topRowsWithBottomBorder + bottomRowNaked)
    }

    override fun getLeftBorder(element: Grid): FormattableList {
        return colBoxFormatter.getLeftBorder(element.colList.first())
    }

    override fun getRightBorder(element: Grid): FormattableList {
        return colBoxFormatter.getRightBorder(element.colList.last())
    }

    override fun getTopBorder(element: Grid): FormattableList {
        return rowBoxFormatter.getTopBorder(element.rowList.first())
    }

    override fun getBottomBorder(element: Grid): FormattableList {
        return rowBoxFormatter.getBottomBorder(element.rowList.last())
    }

    override fun getTopLeftEdge(element: Grid): String = rowBoxFormatter.getTopLeftEdge(element.rowList.first())

    override fun getTopRightEdge(element: Grid): String = rowBoxFormatter.getTopRightEdge(element.rowList.first())

    override fun getBottomLeftEdge(element: Grid): String = rowBoxFormatter.getBottomLeftEdge(element.rowList.last())

    override fun getBottomRightEdge(element: Grid): String = rowBoxFormatter.getBottomRightEdge(element.rowList.last())
}
