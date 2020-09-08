package nl.jhvh.sudoku.grid.solve

import nl.jhvh.sudoku.grid.event.GridEventType
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.segment.GridSegment

class GridSolver(val grid: Grid) {

    fun subscribeSegmentsToEvents() {
        val segments: List<GridSegment> = grid.rowList + grid.colList + grid.blockList
        for (eventType in GridEventType.values()) {
            segments.forEach { it.subscribe(it, eventType) }
        }
    }

    fun solveGrid() {
        subscribeSegmentsToEvents()

    }

}