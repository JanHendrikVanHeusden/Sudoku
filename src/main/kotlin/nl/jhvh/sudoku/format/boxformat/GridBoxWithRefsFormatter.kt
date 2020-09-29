package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.element.GridFormatting
import nl.jhvh.sudoku.format.element.SudokuFormatting
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.util.concatEach

/**
 * Formatter to format a [Grid] using box drawing characters and numbers. Stateless.
 *  * Box drawing characters: ║ │ ═ ─ ╔ ╗ ╚ ╝ ╤ ╧ ╦ ╩ ╟ ╢ ╠ ╣ ╬ ┼ ╪ ╫ etc
 *
 * Typically for console output, to observe results of actions (grid construction, Sudoku solving,
 * testing etc.)
 * @param gridBoxFormatter The [SudokuFormatting] to be decorated by adding row references (A, B, C, ...)
 * at the left border, and column references (1, 2, 3, ...) at the top border.
 * * If the input is already a [GridBoxWithRefsFormatter], no further decoration is done (lenient behaviour).
 */
class GridBoxWithRefsFormatter(val gridBoxFormatter: GridBoxFormatter): GridRefsProvider, GridFormatting, GridElementBoxFormatter<Grid> by gridBoxFormatter {

    override fun format(grid: Grid): FormattableList {
        if (gridBoxFormatter is GridRefsProvider) {
            // if the gridBoxFormatter provides references already, we shouldn't try to add these once more
            return gridBoxFormatter.format(grid)
        }
        // add row refs to left of grid
        val formattedWithoutRefs = gridBoxFormatter.format(grid)
        val rowRefs = getRowRefs(formattedWithoutRefs, grid)
        val formattedWithRefs = (rowRefs concatEach formattedWithoutRefs).toMutableList()
        // add col refs above grid
        val rowRefWidth = rowRefs[0].length
        // colRefs returns a list with 1 entry only
        val colRefStr = "".padEnd(rowRefWidth, ' ') + getColRefs(formattedWithoutRefs, grid)[0]
        formattedWithRefs.add(0, colRefStr)

        return FormattableList(formattedWithRefs)
    }

    override fun getColRefs(formatResultWithoutRefs: FormattableList, grid: Grid): FormattableList {
        return FormattableList(listOf(getColRefString(formatResultWithoutRefs, grid)))
    }

    private fun getColRefString(formatResultWithoutRefs: FormattableList, grid: Grid): String {
        // determine width of each cell, based on formatted output. Subtract 1 for right border (single character, so width == 1)
        val formattedWidth = formatResultWithoutRefs[0].length - 1
        val cellWidth = formattedWidth / grid.gridSize
        val colRefPosition = cellWidth / 2

        val colRefs = StringBuilder(formatResultWithoutRefs.size)
        for (colIndex in 0 until grid.gridSize) {
            val colRef = grid.colList[colIndex].colRef
            for (i in 0..colRefPosition-colRef.length) {
                colRefs.append(" ")
            }
            colRefs.append(colRef)
            for (i in (colRefPosition + 1) until cellWidth) {
                colRefs.append(" ")
            }
        }
        return colRefs.toString()
    }

    override fun getRowRefs(formatResultWithoutRefs: FormattableList, grid: Grid): FormattableList {
        // determine width of each cell, based on formatted output. Subtract 1 for bottom (single character, so height == 1)
        val cellHeight = (formatResultWithoutRefs.size - 1) / grid.gridSize
        val rowRefPosition = cellHeight / 2
        val rowRefWidth = grid.rowList.last().rowRef.length + 1
        val rowRefs: MutableList<String> = mutableListOf()
        val blank = "".padStart(rowRefWidth, ' ')
        for (rowIndex in 0 until grid.gridSize) {
            for (i in 0 until rowRefPosition) {
                rowRefs.add(blank)
            }
            rowRefs.add((grid.rowList[rowIndex].rowRef + " ").padStart(rowRefWidth, ' '))
            for (i in (rowRefPosition + 1) until cellHeight) {
                rowRefs.add(blank)
            }
        }
        // add an extra line for the bottom
        rowRefs.add(blank)
        return FormattableList(rowRefs)
    }

}
