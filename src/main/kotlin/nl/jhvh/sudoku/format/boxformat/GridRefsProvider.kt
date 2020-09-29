package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.grid.model.Grid

interface GridRefsProvider {

    fun format(grid: Grid): FormattableList

    fun getColRefs(formatResultWithoutRefs: FormattableList, grid: Grid): FormattableList

    fun getRowRefs(formatResultWithoutRefs: FormattableList, grid: Grid): FormattableList

}