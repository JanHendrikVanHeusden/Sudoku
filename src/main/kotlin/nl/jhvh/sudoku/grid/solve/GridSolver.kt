package nl.jhvh.sudoku.grid.solve

import nl.jhvh.sudoku.grid.event.ValueEvent
import nl.jhvh.sudoku.grid.event.ValueEventListener
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.segment.GridSegment

class GridSolver(val grid: Grid): ValueEventListener {

    val segments: List<GridSegment> = grid.rowList + grid.colList + grid.blockList

    init {
        // TODO: subscribe myself to all event sources
    }

    fun solveGrid() {
//        val nonFixedByPrio
//                = grid.eventSources.filter { (it is NonFixedValue) }
//                .map { it as NonFixedValue }
//                .filter { !it.isSet }
//                .sortedBy { it.getValueCandidates().size }
//                .groupBy { it.getValueCandidates().size }
//        nonFixedByPrio.forEach {
//
//        }
    }

    override fun onEvent(valueEvent: ValueEvent) {
        solveGrid()
    }

}