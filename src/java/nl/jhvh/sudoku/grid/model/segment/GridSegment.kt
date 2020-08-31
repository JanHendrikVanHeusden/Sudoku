package nl.jhvh.sudoku.grid.model.segment

import nl.jhvh.sudoku.grid.event.GridEvent
import nl.jhvh.sudoku.grid.event.GridEventListener
import nl.jhvh.sudoku.grid.event.GridEventType.SET_CELL_VALUE
import nl.jhvh.sudoku.grid.event.candidate.CellRemoveCandidatesEvent
import nl.jhvh.sudoku.grid.event.candidate.CellRemoveCandidatesEventHandler
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEventHandler
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.GridElement
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.solve.GridSolver

/**
 * A [GridSegment] is an abstraction (super class) of the collections of [Cell]s that each, when solved,
 * contain all defined values of the Sudoku.
 *
 * Concrete subclasses are [Col] (column), [Row] and [Block].
 *
 * A functional synonym for [GridSegment] is **Group**.
 */
abstract class GridSegment constructor(grid: Grid) : GridElement(grid), GridEventListener,
        SetCellValueEventHandler by GridSolver(), CellRemoveCandidatesEventHandler by GridSolver() {

    abstract val cells: LinkedHashSet<Cell>

    override fun onEvent(gridEvent: GridEvent) {
        when (gridEvent) {
            is SetCellValueEvent -> {
                handleEvent(gridEvent, this)
            }
            is CellRemoveCandidatesEvent -> {
                handleEvent(gridEvent, this)
            }
            else -> throw NotImplementedError("Unimplemented type of gridEvent: $gridEvent (class: ${gridEvent.javaClass.simpleName})")
        }
    }

    protected fun subscribeToSetValueEvents() {
        cells.forEach { it.cellValue.subscribe(this, SET_CELL_VALUE) }
    }

}
