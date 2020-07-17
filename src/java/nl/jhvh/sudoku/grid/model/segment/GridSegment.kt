package nl.jhvh.sudoku.grid.model.segment

import nl.jhvh.sudoku.grid.event.cellvalue.CellSetValueListener
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.GridElement
import nl.jhvh.sudoku.grid.model.cell.Cell

/**
 * A [GridSegment] is an abstraction (super class) of the collections of [Cell]s that each, when solved,
 * contain all defined values of the Sudoku.
 *
 * Concrete subclasses are [Col] (column), [Row] and [Block].
 *
 * A functional synonym for [GridSegment] is **Group**.
 */
abstract class GridSegment protected constructor(grid: Grid) : GridElement(grid), CellSetValueListener {

    abstract val cellList: List<Cell>
}
